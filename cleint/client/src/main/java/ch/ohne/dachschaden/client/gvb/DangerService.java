package ch.ohne.dachschaden.client.gvb;

import ch.ohne.dachschaden.client.AiService;
import ch.ohne.dachschaden.client.Danger;
import ch.ohne.dachschaden.client.Severity;
import ch.ohne.dachschaden.client.adminBuilding.AdminBuilding;
import ch.ohne.dachschaden.client.exceptions.DangerDataNotFoundException;
import ch.ohne.dachschaden.client.exceptions.ExternalGvbServiceException;
import ch.ohne.dachschaden.client.geoAdmin.EgidService;
import jakarta.validation.constraints.NotBlank;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;

@Service
@Validated
@Slf4j
public class DangerService {

    private final EgidService egidService;
    private final AiService aiService;
    private final GvbClient gvbClient;

    public DangerService(EgidService egidService, AiService aiService, GvbClient gvbClient) {
        this.egidService = egidService;
        this.aiService = aiService;
        this.gvbClient = gvbClient;
    }

    /** Returns a list of potential dangers for a given EGID, sorted by severity (desc). */
    public List<Danger> getDangers(@NotBlank String egid) {
        try {
            log.debug("Query GVB Naturgefahren for EGID={}", egid);
            GvbQueryResponse resp = gvbClient.queryByEgid(egid);

            if (resp == null || resp.features() == null || resp.features().isEmpty()) {
                throw new DangerDataNotFoundException("Keine Naturgefahren-Daten gefunden.");
            }

            var attrs = resp.features().getFirst().attributes(); // one building expected
            List<Danger> dangers = buildDangersFromAttributes(attrs);
            dangers.removeIf(d -> d.getSeverity() == Severity.NONE);
            dangers.sort(Comparator.comparing(Danger::getSeverity).reversed());
            return dangers;

        } catch (DangerDataNotFoundException e) {
            log.info("No danger data for EGID {}: {}", egid, e.getMessage());
            return List.of(); // or rethrow, depending on your API design
        } catch (RuntimeException e) {
            log.error("Error calling GVB for EGID {}: {}", egid, e.getMessage(), e);
            throw new ExternalGvbServiceException("Fehler beim externen GVB-Dienst", e);
        }
    }

    /** Delegates AI explanation of a given danger name. */
    public String getDangerExplanation(@NotBlank String danger) {
        return aiService.getDangerExplanation(danger);
    }

    /** AI-based recommendations for a building + selected danger. */
    public List<String> getRecommendations(@NotBlank String egid, @NotBlank String danger, @NotBlank String address) {
        AdminBuilding building = egidService.findBuildingByEgid(egid);
        return List.of(aiService.getDangerSolutions(building, danger, address).split("\\+"));
    }

    // ---------------- Mapping helpers ----------------

    private List<Danger> buildDangersFromAttributes(GvbQueryResponse.Attributes a) {
        List<Danger> out = new ArrayList<>();
        if (a == null) return out;

        // 1) Oberflächenabfluss
        Severity runoffSev = severityFromRunoff(a.OBERFLAECHENABFLUSS_TEXT_DE(), a.OBERFLAECHENABFLUSS());
        out.add(new Danger(
                "Oberflächenabfluss",
                runoffSev,
                safeDetail(a.OBERFLAECHENABFLUSS_TEXT_DE(), "Tiefe unbekannt")
        ));

        // 2) Hochwasser – Fliessgewässer
        Severity riverSev = severityFromGermanHazardText(a.FLIESSGEWAESSER_TEXT_DE(), a.HOCHWASSER_FLIESSGEWAESSER());
        out.add(new Danger(
                "Hochwasser (Fliessgewässer)",
                riverSev,
                safeDetail(a.FLIESSGEWAESSER_TEXT_DE(), "keine Angabe")
        ));

        // 3) Hochwasser – Seen
        Severity lakeSev = severityFromGermanHazardText(a.SEEN_TEXT_DE(), a.HOCHWASSER_SEEN());
        out.add(new Danger(
                "Hochwasser (Seen)",
                lakeSev,
                safeDetail(a.SEEN_TEXT_DE(), "keine Angabe")
        ));

        // 4) Hagel
        Severity hailSev = severityFromHail(a.HAGEL_TEXT(), a.HAGEL());
        out.add(new Danger(
                "Hagel",
                hailSev,
                safeDetail(a.HAGEL_TEXT(), "Durchmesser unbekannt")
        ));

        // 5) Sturm
        Severity windSev = severityFromWind(a.STURM_TEXT(), a.STURM());
        String windDetails = a.STURM_TEXT() != null ? a.STURM_TEXT()
                : (a.STURM() != null ? Math.round(a.STURM() * 3.6) + " km/h (aus m/s berechnet)" : "keine Angabe");
        out.add(new Danger(
                "Sturm",
                windSev,
                windDetails
        ));

        return out;
    }

    private static String safeDetail(String t, String fallback) {
        return (t == null || t.isBlank()) ? fallback : t;
    }

    private static final Pattern NUM = Pattern.compile("(\\d+(?:\\.\\d+)?)");

    private static Severity severityFromGermanHazardText(String txt, Integer cls) {
        if (txt != null) {
            String s = txt.toLowerCase();
            if (s.contains("keine")) return Severity.NONE;
            if (s.contains("gering") || s.contains("niedrig")) return Severity.LOW;
            if (s.contains("mittel") || s.contains("mässig") || s.contains("massig")) return Severity.MEDIUM;
            if (s.contains("hoch") || s.contains("erheblich")) return Severity.HIGH;
        }
        if (cls != null) {
            if (cls <= 0) return Severity.NONE;
            if (cls == 1) return Severity.LOW;
            if (cls == 2) return Severity.MEDIUM;
            return Severity.HIGH;
        }
        return Severity.NONE;
    }

    private static Severity severityFromRunoff(String txt, Integer cls) {
        if (txt != null) {
            String s = txt.replace(",", ".").toLowerCase();
            if (s.contains("keine")) return Severity.NONE;
            if (s.contains(">") || s.contains("≥")) {
                if (s.contains("30")) return Severity.HIGH;
            }
            var m = NUM.matcher(s);
            if (m.find()) {
                double cm = Double.parseDouble(m.group(1));
                if (cm < 10) return Severity.LOW;
                if (cm < 30) return Severity.MEDIUM;
                return Severity.HIGH;
            }
        }
        if (cls != null) {
            if (cls <= 0) return Severity.NONE;
            if (cls == 1) return Severity.LOW;
            if (cls == 2) return Severity.MEDIUM;
            return Severity.HIGH;
        }
        return Severity.NONE;
    }

    private static Severity severityFromHail(String txt, Integer cls) {
        if (txt != null) {
            var m = NUM.matcher(txt.replace(",", "."));
            if (m.find()) {
                double cm = Double.parseDouble(m.group(1));
                if (cm < 2.0) return Severity.LOW;
                if (cm < 4.0) return Severity.MEDIUM;
                return Severity.HIGH;
            }
        }
        if (cls != null) {
            if (cls <= 1) return Severity.NONE;
            if (cls == 2) return Severity.LOW;
            if (cls == 3) return Severity.MEDIUM;
            return Severity.HIGH;
        }
        return Severity.NONE;
    }

    private static Severity severityFromWind(String txt, Double val) {
        if (txt != null) {
            var m = Pattern.compile("(\\d+(?:\\.\\d+)?)\\s*km/h")
                    .matcher(txt.replace(",", "."));
            if (m.find()) {
                double kmh = Double.parseDouble(m.group(1));
                return windSeverityByKmh(kmh);
            }
            if (txt.toLowerCase().contains("keine")) return Severity.NONE;
        }
        if (val != null) {
            double kmh = val * 3.6;
            return windSeverityByKmh(kmh);
        }
        return Severity.NONE;
    }

    private static Severity windSeverityByKmh(double kmh) {
        if (kmh < 70) return Severity.NONE;
        if (kmh < 90) return Severity.LOW;
        if (kmh < 120) return Severity.MEDIUM;
        return Severity.HIGH;
    }
}

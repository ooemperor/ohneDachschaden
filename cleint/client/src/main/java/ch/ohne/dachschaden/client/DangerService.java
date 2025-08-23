package ch.ohne.dachschaden.client;

import ch.ohne.dachschaden.client.adminBuilding.AdminBuilding;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class DangerService {

    private final EgidService egidService;
    private final AiService aiService;
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    public DangerService(
            EgidService egidService,
            AiService aiService
    ) {
        this.egidService = egidService;
        this.aiService = aiService;
    }
    /**
     * Returns a list of potential dangers for a given address.
     * For now, this returns fake data. Prepared code for a future API call is included below as comments.
     */
    public List<Danger> getDangers(String egid) {
        try {
            System.out.println(egid);
            // Gleichheitszeichen korrekt kodieren
            String url = "https://webgis.gvb.ch/server/rest/services/natur/GEBAEUDE_NATURGEFAHREN_BE_DE_FR/MapServer/1/query" +
                    "?where=GWR_EGID=" + egid +
                    "&outFields=OBJECTID,GWR_EGID,BEGID,OBERFLAECHENABFLUSS,HOCHWASSER_SEEN,HOCHWASSER_FLIESSGEWAESSER,HAGEL,STURM,HAUSNUMMER,STRNAME,PLZ,ORTSCHAFT,ADRESSE,ADRESSE_POPUP,OBERFLAECHENABFLUSS_TEXT_DE,OBERFLAECHENABFLUSS_TEXT_FR,FLIESSGEWAESSER_TEXT_DE,FLIESSGEWAESSER_TEXT_FR,SEEN_TEXT_DE,SEEN_TEXT_FR,HAGEL_TEXT,STURM_TEXT,SHAPE" +
                    "&returnGeometry=true" +
                    "&f=pjson";
            String response = restTemplate.getForObject(url, String.class);
            JsonNode root = objectMapper.readTree(response);
            List<Danger> dangers = new ArrayList<>();
            for (Danger danger: buildDangersFromResponse(root)) {
                if (danger.getSeverity() != Severity.NONE) {
                    dangers.add(danger);
                }
            }
            dangers.sort(Comparator.comparing(Danger::getSeverity).reversed());
            return dangers;
        } catch (Exception e) {
            System.out.println(e);
            return new ArrayList<>();
        }
    }

    /**
     * Returns an Explanation as a String of the Danger (What is it, what can it do, how can this happen) from ChatGPT
     */
    public String getDangerExplanation(String danger) {

        return aiService.getDangerExplanation(danger);
    }

    /**
     * Returns a list of recommendations for a given address and selected danger (danger).
     * For now, this returns fake data. Prepared code for a future API call is included below as comments.
     */
    public List<String> getRecommendations(String egid, String danger, String address) {
        AdminBuilding building = egidService.findBuildingByEgid(egid);
        return List.of(aiService.getDangerSolutions(building, danger, address).split(";"));
    }

    private List<Danger> buildDangersFromResponse(JsonNode root) {
        List<Danger> out = new ArrayList<>();
        if (root == null || !root.path("features").isArray() || root.path("features").size() == 0) {
            return out;
        }
        // In der Regel genau 1 Gebäude → nimm das erste Feature
        JsonNode attrs = root.path("features").get(0).path("attributes");

        // 1) Oberflächenabfluss
        String runoffText = text(attrs, "OBERFLAECHENABFLUSS_TEXT_DE"); // z.B. "0 - 10 cm"
        Integer runoffClass = intOrNull(attrs, "OBERFLAECHENABFLUSS");   // z.B. 1
        Severity runoffSev = severityFromRunoff(runoffText, runoffClass);
        out.add(new Danger(
                "Oberflächenabfluss",
                runoffSev,
                safeDetail(runoffText, "Tiefe unbekannt")
        ));

        // 2) Hochwasser – Fliessgewässer
        String riverText = text(attrs, "FLIESSGEWAESSER_TEXT_DE"); // z.B. "keine Gefährdung"
        Integer riverClass = intOrNull(attrs, "HOCHWASSER_FLIESSGEWAESSER");
        Severity riverSev = severityFromGermanHazardText(riverText, riverClass);
        out.add(new Danger(
                "Hochwasser (Fliessgewässer)",
                riverSev,
                safeDetail(riverText, "keine Angabe")
        ));

        // 3) Hochwasser – Seen
        String lakeText = text(attrs, "SEEN_TEXT_DE");
        Integer lakeClass = intOrNull(attrs, "HOCHWASSER_SEEN");
        Severity lakeSev = severityFromGermanHazardText(lakeText, lakeClass);
        out.add(new Danger(
                "Hochwasser (Seen)",
                lakeSev,
                safeDetail(lakeText, "keine Angabe")
        ));

        // 4) Hagel
        String hailText = text(attrs, "HAGEL_TEXT");       // z.B. "4 - 5 cm"
        Integer hailClass = intOrNull(attrs, "HAGEL");     // z.B. 4
        Severity hailSev = severityFromHail(hailText, hailClass);
        out.add(new Danger(
                "Hagel",
                hailSev,
                safeDetail(hailText, "Durchmesser unbekannt")
        ));

        // 5) Sturm
        String windText = text(attrs, "STURM_TEXT");       // z.B. "<= 144 km/h"
        Double windVal = doubleOrNull(attrs, "STURM");     // z.B. 34.265... (m/s)
        Severity windSev = severityFromWind(windText, windVal);
        String windDetails = windText != null ? windText
                : (windVal != null ? Math.round(windVal * 3.6) + " km/h (aus m/s berechnet)" : "keine Angabe");
        out.add(new Danger(
                "Sturm",
                windSev,
                windDetails
        ));

        // Optional: Filtere NONE, falls du nur relevante Gefahren zeigen willst
        // out.removeIf(d -> d.getSeverity() == Severity.NONE);
        return out;
    }

//////////////////////////////
// Helpers (Parsing & Mapping)
    //////////////////////////////
    private static String text(JsonNode n, String field) {
        JsonNode v = n.get(field);
        return (v != null && !v.isNull()) ? v.asText(null) : null;
    }
    private static Integer intOrNull(JsonNode n, String field) {
        JsonNode v = n.get(field);
        return (v != null && v.isInt()) ? v.asInt() : null;
    }
    private static Double doubleOrNull(JsonNode n, String field) {
        JsonNode v = n.get(field);
        return (v != null && v.isNumber()) ? v.asDouble() : null;
    }
    private static String safeDetail(String t, String fallback) {
        return (t == null || t.isBlank()) ? fallback : t;
    }

    private static Severity severityFromGermanHazardText(String txt, Integer cls) {
        // generische Auswertung von "keine Gefährdung", "gering", "mittel/mässig", "erheblich/hoch"
        if (txt != null) {
            String s = txt.toLowerCase();
            if (s.contains("keine")) return Severity.NONE;
            if (s.contains("gering") || s.contains("niedrig")) return Severity.LOW;
            if (s.contains("mittel") || s.contains("mässig") || s.contains("massig")) return Severity.MEDIUM;
            if (s.contains("hoch") || s.contains("erheblich")) return Severity.HIGH;
        }
        // Fallback: Klassenzahl 0–3 o.ä.
        if (cls != null) {
            if (cls <= 0) return Severity.NONE;
            if (cls == 1) return Severity.LOW;
            if (cls == 2) return Severity.MEDIUM;
            return Severity.HIGH; // >=3
        }
        return Severity.NONE;
    }

    private static Severity severityFromRunoff(String txt, Integer cls) {
        // Typtische Klassen nach Wasserhöhe; Text ist meist "0 - 10 cm", "10 - 30 cm", "> 30 cm"
        if (txt != null) {
            String s = txt.replace(",", ".").toLowerCase();
            if (s.contains("keine")) return Severity.NONE;
            if (s.contains(">") || s.contains("≥")) {
                // Wenn ein Grenzwert >30 cm drinsteht, nimm HIGH
                if (s.contains("30")) return Severity.HIGH;
            }
            // Parse erste Zahl, grobe Heuristik
            java.util.regex.Matcher m = java.util.regex.Pattern.compile("(\\d+(?:\\.\\d+)?)").matcher(s);
            if (m.find()) {
                double cm = Double.parseDouble(m.group(1));
                if (cm < 10) return Severity.LOW;
                if (cm < 30) return Severity.MEDIUM;
                return Severity.HIGH;
            }
        }
        // Fallback auf Klassenwert (falls vorhanden)
        if (cls != null) {
            if (cls <= 0) return Severity.NONE;
            if (cls == 1) return Severity.LOW;
            if (cls == 2) return Severity.MEDIUM;
            return Severity.HIGH; // >=3
        }
        return Severity.NONE;
    }

    private static Severity severityFromHail(String txt, Integer cls) {
        // Text wie "4 - 5 cm" → führende Zahl parsen
        if (txt != null) {
            java.util.regex.Matcher m = java.util.regex.Pattern.compile("(\\d+(?:\\.\\d+)?)").matcher(txt.replace(",", "."));
            if (m.find()) {
                double cm = Double.parseDouble(m.group(1));
                if (cm < 2.0) return Severity.LOW;
                if (cm < 4.0) return Severity.MEDIUM;
                return Severity.HIGH; // >= 4 cm
            }
        }
        if (cls != null) {
            if (cls <= 1) return Severity.NONE;
            if (cls == 2) return Severity.LOW;
            if (cls == 3) return Severity.MEDIUM;
            return Severity.HIGH; // >=4
        }
        return Severity.NONE;
    }

    private static Severity severityFromWind(String txt, Double val) {
        // Priorisiere Text wie "<= 144 km/h"; sonst val (m/s) → km/h
        if (txt != null) {
            java.util.regex.Matcher m = java.util.regex.Pattern.compile("(\\d+(?:\\.\\d+)?)\\s*km/h").matcher(txt.replace(",", "."));
            if (m.find()) {
                double kmh = Double.parseDouble(m.group(1));
                return windSeverityByKmh(kmh);
            }
            if (txt.toLowerCase().contains("keine")) return Severity.NONE;
        }
        if (val != null) {
            double kmh = val * 3.6; // m/s → km/h
            return windSeverityByKmh(kmh);
        }
        return Severity.NONE;
    }
    private static Severity windSeverityByKmh(double kmh) {
        // Heuristische Schwellen – gerne nach Bedarf anpassen
        if (kmh < 70) return Severity.NONE;
        if (kmh < 90) return Severity.LOW;
        if (kmh < 120) return Severity.MEDIUM;
        return Severity.HIGH; // >= 120 km/h
    }
}

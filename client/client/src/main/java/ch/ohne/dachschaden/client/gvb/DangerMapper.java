package ch.ohne.dachschaden.client.gvb;

import ch.ohne.dachschaden.client.Severity;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

class DangerMapper {

    List<Danger> mapFrom(GvbQueryResponse.Attributes a) {
        List<Danger> out = new ArrayList<>();
        if (a == null) return out;

        out.add(mapRunoff(a));
        out.add(mapRiverFlood(a));
        out.add(mapLakeFlood(a));
        out.add(mapHail(a));
        out.add(mapStorm(a));

        return out;
    }

    // ---------- Individual hazard mappers ----------

    private Danger mapRunoff(GvbQueryResponse.Attributes a) {
        Severity sev = severityFromRunoff(a.OBERFLAECHENABFLUSS_TEXT_DE(), a.OBERFLAECHENABFLUSS());
        String details = defaultIfBlank(a.OBERFLAECHENABFLUSS_TEXT_DE(), "Tiefe unbekannt");
        return new Danger("Oberflächenabfluss", sev, details);
    }

    private Danger mapRiverFlood(GvbQueryResponse.Attributes a) {
        Severity sev = severityFromGermanHazardText(a.FLIESSGEWAESSER_TEXT_DE(), a.HOCHWASSER_FLIESSGEWAESSER());
        String details = defaultIfBlank(a.FLIESSGEWAESSER_TEXT_DE(), "keine Angabe");
        return new Danger("Hochwasser (Fliessgewässer)", sev, details);
    }

    private Danger mapLakeFlood(GvbQueryResponse.Attributes a) {
        Severity sev = severityFromGermanHazardText(a.SEEN_TEXT_DE(), a.HOCHWASSER_SEEN());
        String details = defaultIfBlank(a.SEEN_TEXT_DE(), "keine Angabe");
        return new Danger("Hochwasser (Seen)", sev, details);
    }

    private Danger mapHail(GvbQueryResponse.Attributes a) {
        Severity sev = severityFromHail(a.HAGEL_TEXT(), a.HAGEL());
        String details = defaultIfBlank(a.HAGEL_TEXT(), "Durchmesser unbekannt");
        return new Danger("Hagel", sev, details);
    }

    private Danger mapStorm(GvbQueryResponse.Attributes a) {
        Severity sev = severityFromWind(a.STURM_TEXT(), a.STURM());
        String details = a.STURM_TEXT() != null
                ? a.STURM_TEXT()
                : (a.STURM() != null ? Math.round(a.STURM() * 3.6) + " km/h (aus m/s berechnet)" : "keine Angabe");
        return new Danger("Sturm", sev, details);
    }

    // ---------- Shared helpers (kept local & focused) ----------

    private static String defaultIfBlank(String value, String fallback) {
        return (value == null || value.isBlank()) ? fallback : value;
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

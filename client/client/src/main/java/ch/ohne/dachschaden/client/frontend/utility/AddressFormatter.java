package ch.ohne.dachschaden.client.frontend.utility;

import java.util.ArrayList;
import java.util.List;

final public class AddressFormatter {
    public AddressFormatter() {}

    public static String format(String displayName) {
        if (displayName == null || displayName.isBlank()) return "";
        String[] raw = displayName.split(",");
        List<String> parts = new ArrayList<>();
        for (String r : raw) {
            String t = r.trim();
            if (!t.isEmpty()) parts.add(t);
        }
        if (parts.isEmpty()) return displayName;

        String street = null, number = null;

        if (parts.size() >= 2) {
            String p0 = parts.get(0);
            String p1 = parts.get(1);
            if (p0.matches("\\d+[a-zA-Z]?$")) {
                number = p0; street = p1;
            } else if (p1.matches("\\d+[a-zA-Z]?$")) {
                street = p0; number = p1;
            }
        }

        String postal = null;
        int postalIndex = -1;
        for (int i = 0; i < parts.size(); i++) {
            String t = parts.get(i);
            if (t.matches("\\d{4,5}$")) { postal = t; postalIndex = i; break; }
        }

        String city = null;
        if (postalIndex > 0) {
            for (int i = postalIndex - 1; i >= 0; i--) {
                String cand = parts.get(i);
                String low = cand.toLowerCase();
                if (low.contains("administrative") || low.contains("region") || low.contains("district")
                        || low.contains("arrondissement") || low.contains("county") || low.contains("borough")
                        || low.contains("stadtteil") || low.contains("bezirk") || low.contains("kanton")
                        || low.matches("schweiz|switzerland|suisse|svizzera")) {
                    continue;
                }
                city = cand;
                break;
            }
        }

        if (street != null && number != null && city != null) return street + " " + number + " " + city;
        if (street != null && number != null) return street + " " + number;
        if (postal != null && city != null) return city + " " + postal;
        return displayName;
    }
}

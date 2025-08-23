package ch.ohne.dachschaden.client;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DangerService {

    /**
     * Returns a list of potential dangers for a given address.
     * For now, this returns fake data. Prepared code for a future API call is included below as comments.
     */
    public List<String> getDangers(String address) {
        // Example of how this could look with a real API call in the future:
        //
        // String endpoint = "https://api.example.com/dangers?address=" + URLEncoder.encode(address, StandardCharsets.UTF_8);
        // RestTemplate restTemplate = new RestTemplate();
        // ResponseEntity<List<String>> response = restTemplate.exchange(
        //         endpoint,
        //         HttpMethod.GET,
        //         null,
        //         new ParameterizedTypeReference<List<String>>() {}
        // );
        // return response.getBody();
        //
        // For now, return some mocked values:
        return List.of(
                "Sturmgefahr",
                "Hagel",
                "Hochwasser",
                "Schneelast"
        );
    }

    public String getDangerExplanation(String danger) {
        return "Begründung wieso das Scheisse ist";
    }
    /**
     * Returns a list of recommendations for a given address and selected danger (Gefahr).
     * For now, this returns fake data. Prepared code for a future API call is included below as comments.
     */
    public List<String> getRecommendations(String address, String gefahr) {
        // Example of how this could look with a real API call in the future:
        //
        // String baseUrl = "https://api.example.com/recommendations";
        // String endpoint = baseUrl + "?address=" + URLEncoder.encode(address, StandardCharsets.UTF_8)
        //         + "&gefahr=" + URLEncoder.encode(gefahr, StandardCharsets.UTF_8);
        // RestTemplate restTemplate = new RestTemplate();
        // ResponseEntity<List<String>> response = restTemplate.exchange(
        //         endpoint,
        //         HttpMethod.GET,
        //         null,
        //         new ParameterizedTypeReference<List<String>>() {}
        // );
        // return response.getBody();
        //
        // For now, return some mocked values depending on the given danger:
        return switch (gefahr == null ? "" : gefahr.toLowerCase()) {
            case "sturmgefahr", "sturm", "wind" -> List.of(
                    "Sichern Sie lose Gegenstände rund ums Haus",
                    "Überprüfen Sie die Dachziegel auf festen Sitz",
                    "Bäume und Äste in Hausnähe zurückschneiden"
            );
            case "hagel" -> List.of(
                    "Autos in Garage oder unter Schutz stellen",
                    "Rollläden und Fensterläden schließen",
                    "Empfindliche Außenanlagen abdecken"
            );
            case "hochwasser", "überschwemmung" -> List.of(
                    "Wertgegenstände in obere Stockwerke bringen",
                    "Rückstauklappen prüfen und schließen",
                    "Sandsäcke bereitstellen"
            );
            case "schneelast", "schnee" -> List.of(
                    "Dachlast beobachten und bei Bedarf Schnee räumen",
                    "Dachrinnen und Fallrohre frei halten",
                    "Schneefangsysteme prüfen"
            );
            default -> List.of(
                    "Allgemeine Vorsorge treffen",
                    "Wetterwarnungen verfolgen",
                    "Versicherungspolicen überprüfen"
            );
        };
    }
}

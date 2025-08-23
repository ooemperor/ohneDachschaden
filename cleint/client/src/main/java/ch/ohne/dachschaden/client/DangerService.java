package ch.ohne.dachschaden.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class DangerService {

    private final EgidService egidService;
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    public DangerService(
            EgidService egidService
    ) {
        this.egidService = egidService;
    }
    /**
     * Returns a list of potential dangers for a given address.
     * For now, this returns fake data. Prepared code for a future API call is included below as comments.
     */
    public List<String> getDangers(String address) {
        try {
            String egid = egidService.findEgidByAddress(address);
            System.out.println("egid: " + egid);
            // Gleichheitszeichen korrekt kodieren
            String url = "https://webgis.gvb.ch/server/rest/services/natur/GEBAEUDE_NATURGEFAHREN_BE_DE_FR/MapServer/1/query" +
                    "?where=GWR_EGID=" + egid +
                    "&outFields=OBJECTID,GWR_EGID,BEGID,OBERFLAECHENABFLUSS,HOCHWASSER_SEEN,HOCHWASSER_FLIESSGEWAESSER,HAGEL,STURM,HAUSNUMMER,STRNAME,PLZ,ORTSCHAFT,ADRESSE,ADRESSE_POPUP,OBERFLAECHENABFLUSS_TEXT_DE,OBERFLAECHENABFLUSS_TEXT_FR,FLIESSGEWAESSER_TEXT_DE,FLIESSGEWAESSER_TEXT_FR,SEEN_TEXT_DE,SEEN_TEXT_FR,HAGEL_TEXT,STURM_TEXT,SHAPE" +
                    "&returnGeometry=true" +
                    "&f=pjson";
            System.out.println("url: " + url);
            String response = restTemplate.getForObject(url, String.class);
            JsonNode root = objectMapper.readTree(response);
            System.out.println(root);
        } catch (Exception e) {
            System.out.println(e);
            return new ArrayList<>();
        }
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

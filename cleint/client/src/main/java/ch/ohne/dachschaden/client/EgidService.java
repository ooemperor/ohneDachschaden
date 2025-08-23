package ch.ohne.dachschaden.client;

import ch.ohne.dachschaden.client.adminBuilding.AdminBuilding;
import ch.ohne.dachschaden.client.adminBuilding.AdminBuildingRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class EgidService {
    private static final String SEARCH_URL =
            "https://api3.geo.admin.ch/rest/services/api/SearchServer" +
                    "?type=locations&searchText={address}&origins=address&limit=1&sr=2056";

    private static final String FEATURE_URL =
            "https://api3.geo.admin.ch/rest/services/api/MapServer/ch.bfs.gebaeude_wohnungs_register/{featureId}";

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final AdminBuildingRepository adminBuildingRepository;

    public EgidService(AdminBuildingRepository adminBuildingRepository) {
        this.adminBuildingRepository = adminBuildingRepository;
    }

    public String findEgidByAddress(String address) throws Exception {
        try {
            // Step 1: SearchServer
            String response = restTemplate.getForObject(SEARCH_URL, String.class, address);
            JsonNode root = objectMapper.readTree(response);

            if (root.path("results").isEmpty()) {
                throw new Exception("Kein Treffer gefunden.");
            }

            String featureId = root.path("results").get(0).path("attrs").path("featureId").asText();
            if (featureId == null || featureId.isEmpty()) {
                throw new Exception("FeatureId nicht gefunden.");
            }

            // Step 2: Feature Info
            String featureResponse = restTemplate.getForObject(FEATURE_URL, String.class, featureId);
            JsonNode featureRoot = objectMapper.readTree(featureResponse);

            String egid = featureRoot.path("feature").path("attributes").path("egid").asText();
            return egid.isEmpty() ? "EGID nicht gefunden." : egid;

        } catch (Exception e) {
            throw new Exception("Fehler: " + e.getMessage());
        }
    }

    public AdminBuilding findBuildingByEgid(String egid) {
        return adminBuildingRepository.findAdminBuildingByEGID(egid);
    }
}

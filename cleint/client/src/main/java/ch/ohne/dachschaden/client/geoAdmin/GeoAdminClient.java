package ch.ohne.dachschaden.client.geoAdmin;

import ch.ohne.dachschaden.client.config.GeoAdminProperties;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Component
public class GeoAdminClient {

    private final RestClient client;
    private final GeoAdminProperties props;

    public GeoAdminClient(RestClient geoAdminRestClient, GeoAdminProperties props) {
        this.client = geoAdminRestClient;
        this.props = props;
    }

    // GET /SearchServer?type=locations&searchText={address}&origins=address&limit=1&sr=2056
    public SearchResponse searchAddress(String address) {
        return client.get()
                .uri(uriBuilder -> uriBuilder
                        .path(props.search().path())
                        .queryParam("type", "locations")
                        .queryParam("searchText", address)
                        .queryParam("origins", "address")
                        .queryParam("limit", "1")
                        .queryParam("sr", "2056")
                        .build())
                .retrieve()
                .onStatus(HttpStatusCode::isError, (req, res) -> {
                    throw new RuntimeException("GeoAdmin SearchServer error: " + res.getStatusCode());
                })
                .body(SearchResponse.class);
    }

    // GET /MapServer/.../{featureId}
    public FeatureResponse fetchFeature(String featureId) {
        return client.get()
                .uri(uriBuilder -> uriBuilder
                        .path(props.feature().path() + "/{featureId}")
                        .build(Map.of("featureId", featureId)))
                .retrieve()
                .onStatus(HttpStatusCode::isError, (req, res) -> {
                    throw new RuntimeException("GeoAdmin MapServer error: " + res.getStatusCode());
                })
                .body(FeatureResponse.class);
    }
}

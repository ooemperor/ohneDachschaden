package ch.ohne.dachschaden.client.gvb;

import ch.ohne.dachschaden.client.config.GvbProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class GvbClient {

    private final RestClient client;
    private final GvbProperties props;
    private final ObjectMapper objectMapper;

    public GvbClient(RestClient gvbRestClient, GvbProperties props, ObjectMapper objectMapper) {
        this.client = gvbRestClient;
        this.props = props;
        this.objectMapper = objectMapper;
    }

    public GvbQueryResponse queryByEgid(String egid) {
        // Fetch raw string since ArcGIS mislabels JSON as text/plain
        String body = client.get()
                .uri(uri -> uri
                        .path(props.servicePath() + "/" + props.layerId() + "/query")
                        .queryParam("where", "GWR_EGID=" + egid)
                        .queryParam("outFields", props.outFields())
                        .queryParam("returnGeometry", props.returnGeometry())
                        .queryParam("f", props.format()) // "pjson" is fine
                        .build())
                .retrieve()
                .onStatus(HttpStatusCode::isError, (req, res) -> {
                    throw new RuntimeException("GVB query error: " + res.getStatusCode());
                })
                .body(String.class);

        try {
            return objectMapper.readValue(body, GvbQueryResponse.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to parse GVB response into GvbQueryResponse", e);
        }
    }
}

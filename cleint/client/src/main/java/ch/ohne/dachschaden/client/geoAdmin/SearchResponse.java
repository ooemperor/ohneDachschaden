package ch.ohne.dachschaden.client.geoAdmin;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record SearchResponse(List<Result> results) {
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Result(Attrs attrs) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Attrs(String featureId) {}
}

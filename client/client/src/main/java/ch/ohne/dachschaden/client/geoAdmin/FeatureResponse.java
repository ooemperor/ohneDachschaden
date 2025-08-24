package ch.ohne.dachschaden.client.geoAdmin;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record FeatureResponse(Feature feature) {
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Feature(Attributes attributes) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Attributes(String egid) {}
}

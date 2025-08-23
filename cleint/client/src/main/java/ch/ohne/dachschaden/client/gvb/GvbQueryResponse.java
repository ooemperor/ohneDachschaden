package ch.ohne.dachschaden.client.gvb;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record GvbQueryResponse(List<Feature> features) {
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Feature(Attributes attributes) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Attributes(
            Integer OBERFLAECHENABFLUSS,
            String  OBERFLAECHENABFLUSS_TEXT_DE,
            Integer HOCHWASSER_FLIESSGEWAESSER,
            String  FLIESSGEWAESSER_TEXT_DE,
            Integer HOCHWASSER_SEEN,
            String  SEEN_TEXT_DE,
            Integer HAGEL,
            String  HAGEL_TEXT,
            Double  STURM,                  // m/s
            String  STURM_TEXT
    ) {}
}

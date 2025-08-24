package ch.ohne.dachschaden.client.geoAdmin;

import ch.ohne.dachschaden.client.exceptions.AddressNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GeoAdminMapper {

    public String extractFeatureId(SearchResponse search) {
        var results = (search != null) ? search.results() : null;
        if (isNullOrEmpty(results)) {
            throw new AddressNotFoundException("Kein Treffer/FeatureId gefunden.");
        }

        var first = results.getFirst();
        var attrs = (first != null) ? first.attrs() : null;
        var id = (attrs != null) ? attrs.featureId() : null;

        if (id == null || id.isBlank()) {
            throw new AddressNotFoundException("FeatureId nicht gefunden.");
        }
        return id;
    }

    public String extractEgid(FeatureResponse feature) {
        var f = (feature != null) ? feature.feature() : null;
        var attributes = (f != null) ? f.attributes() : null;
        var egid = (attributes != null) ? attributes.egid() : null;

        if (egid == null || egid.isBlank()) {
            throw new AddressNotFoundException("EGID nicht gefunden.");
        }
        return egid;
    }

    private static <T> boolean isNullOrEmpty(List<T> list) {
        return list == null || list.isEmpty();
    }
}

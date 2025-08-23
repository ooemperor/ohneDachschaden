package ch.ohne.dachschaden.client.geoAdmin;

import ch.ohne.dachschaden.client.adminBuilding.AdminBuilding;
import ch.ohne.dachschaden.client.adminBuilding.AdminBuildingRepository;
import ch.ohne.dachschaden.client.exceptions.AddressNotFoundException;
import ch.ohne.dachschaden.client.exceptions.ExternalGeoServiceException;
import jakarta.validation.constraints.NotBlank;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.Optional;

@Service
@Validated
public class EgidService {

    private final GeoAdminClient geoAdminClient;
    private final AdminBuildingRepository adminBuildingRepository;

    public EgidService(GeoAdminClient geoAdminClient,
                       AdminBuildingRepository adminBuildingRepository) {
        this.geoAdminClient = geoAdminClient;
        this.adminBuildingRepository = adminBuildingRepository;
    }

    /**
     * Finds the EGID for a given Swiss address via geo.admin.ch.
     *
     * @throws AddressNotFoundException if no results or missing featureId/egid
     * @throws ExternalGeoServiceException on transport/remote errors
     */
    public String findEgidByAddress(@NotBlank String address) {
        try {
            SearchResponse search = geoAdminClient.searchAddress(address);

            var featureId = Optional.ofNullable(search)
                    .map(SearchResponse::results)
                    .filter(list -> !list.isEmpty())
                    .map(list -> list.get(0))
                    .map(SearchResponse.Result::attrs)
                    .map(SearchResponse.Attrs::featureId)
                    .filter(id -> id != null && !id.isBlank())
                    .orElseThrow(() -> new AddressNotFoundException("Kein Treffer/FeatureId gefunden."));

            FeatureResponse feature = geoAdminClient.fetchFeature(featureId);

            String egid = Optional.ofNullable(feature)
                    .map(FeatureResponse::feature)
                    .map(FeatureResponse.Feature::attributes)
                    .map(FeatureResponse.Attributes::egid)
                    .filter(e -> e != null && !e.isBlank())
                    .orElseThrow(() -> new AddressNotFoundException("EGID nicht gefunden."));

            return egid;

        } catch (AddressNotFoundException e) {
            throw e;
        } catch (RuntimeException e) {
            // Wrap low-level client issues
            throw new ExternalGeoServiceException("Fehler beim externen Geo-Dienst", e);
        }
    }

    public AdminBuilding findBuildingByEgid(@NotBlank String egid) {
        return adminBuildingRepository.findAdminBuildingByEGID(egid);
    }
}

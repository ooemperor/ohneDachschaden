package ch.ohne.dachschaden.client.geoAdmin;

import ch.ohne.dachschaden.client.adminBuilding.AdminBuilding;
import ch.ohne.dachschaden.client.adminBuilding.AdminBuildingRepository;
import ch.ohne.dachschaden.client.exceptions.AddressNotFoundException;
import ch.ohne.dachschaden.client.exceptions.ExternalGeoServiceException;
import jakarta.validation.constraints.NotBlank;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
public class EgidService {

    private final GeoAdminClient geoAdminClient;
    private final GeoAdminMapper geoAdminMapper;
    private final AdminBuildingRepository adminBuildingRepository;

    public EgidService(GeoAdminClient geoAdminClient,
                       AdminBuildingRepository adminBuildingRepository,
                       GeoAdminMapper geoAdminMapper) {
        this.geoAdminClient = geoAdminClient;
        this.adminBuildingRepository = adminBuildingRepository;
        this.geoAdminMapper = geoAdminMapper;
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
            String featureId = geoAdminMapper.extractFeatureId(search);
            FeatureResponse feature = geoAdminClient.fetchFeature(featureId);
            return geoAdminMapper.extractEgid(feature);

        } catch (AddressNotFoundException e) {
            throw e; // domain error: bubble up as-is
        } catch (RuntimeException e) {
            // transport/serialization/etc.
            throw new ExternalGeoServiceException("Fehler beim externen Geo-Dienst", e);
        }
    }

    public AdminBuilding findBuildingByEgid(@NotBlank String egid) {
        return adminBuildingRepository.findAdminBuildingByEGID(egid);
    }
}

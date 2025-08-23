package ch.ohne.dachschaden.client.gvb;

import ch.ohne.dachschaden.client.AiService;
import ch.ohne.dachschaden.client.Danger;
import ch.ohne.dachschaden.client.Severity;
import ch.ohne.dachschaden.client.adminBuilding.AdminBuilding;
import ch.ohne.dachschaden.client.exceptions.DangerDataNotFoundException;
import ch.ohne.dachschaden.client.exceptions.ExternalGvbServiceException;
import ch.ohne.dachschaden.client.geoAdmin.EgidService;
import jakarta.validation.constraints.NotBlank;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.Comparator;
import java.util.List;

@Service
@Validated
@Slf4j
public class DangerService {

    private final EgidService egidService;
    private final AiService aiService;
    private final GvbClient gvbClient;
    private final DangerMapper dangerMapper; // NEW

    public DangerService(EgidService egidService, AiService aiService, GvbClient gvbClient) {
        this.egidService = egidService;
        this.aiService = aiService;
        this.gvbClient = gvbClient;
        this.dangerMapper = new DangerMapper(); // or inject if you prefer
    }

    /** Returns a list of potential dangers for a given EGID, sorted by severity (desc). */
    public List<Danger> getDangers(@NotBlank String egid) {
        try {
            log.debug("Query GVB Naturgefahren for EGID={}", egid);
            GvbQueryResponse resp = gvbClient.queryByEgid(egid);

            if (resp == null || resp.features() == null || resp.features().isEmpty()) {
                throw new DangerDataNotFoundException("Keine Naturgefahren-Daten gefunden.");
            }

            var attrs = resp.features().getFirst().attributes();
            var dangers = dangerMapper.mapFrom(attrs);

            // Keep only relevant ones, then sort
            dangers.removeIf(d -> d.getSeverity() == Severity.NONE);
            dangers.sort(Comparator.comparing(Danger::getSeverity).reversed());
            return dangers;

        } catch (DangerDataNotFoundException e) {
            log.info("No danger data for EGID {}: {}", egid, e.getMessage());
            return List.of();
        } catch (RuntimeException e) {
            log.error("Error calling GVB for EGID {}: {}", egid, e.getMessage(), e);
            throw new ExternalGvbServiceException("Fehler beim externen GVB-Dienst", e);
        }
    }

    public String getDangerExplanation(@NotBlank String danger) {
        return aiService.getDangerExplanation(danger);
    }

    public List<String> getRecommendations(@NotBlank String egid, @NotBlank String danger, @NotBlank String address) {
        AdminBuilding building = egidService.findBuildingByEgid(egid);
        return List.of(aiService.getDangerSolutions(building, danger, address).split("\\+"));
    }
}

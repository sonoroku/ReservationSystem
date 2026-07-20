package reservationsystem.integration;

import org.junit.jupiter.api.Test;
import reservationsystem.controller.SpaceController;
import reservationsystem.controller.SpaceFilterResult;
import reservationsystem.model.Space;
import reservationsystem.persistence.SpaceJsonRepository;
import reservationsystem.service.SpaceFeature;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class US4FeatureFilteringIntegrationTest {

    @Test
    void filtersStarterSpacesByFeatureAliases() {
        SpaceController controller = new SpaceController(new SpaceJsonRepository());

        assertEquals(
                List.of("Nevins Hall Computer Lab", "Student Union Conference Room 1"),
                names(controller.filterByFeatures(List.of(SpaceFeature.COMPUTER)))
        );

        SpaceFilterResult projectorAndComputer = controller.filterByFeatures(List.of(
                SpaceFeature.PROJECTOR,
                SpaceFeature.COMPUTER
        ));

        assertEquals(List.of("Nevins Hall Computer Lab"), names(projectorAndComputer));
    }

    private List<String> names(SpaceFilterResult result) {
        return result.spaces().stream().map(Space::getName).toList();
    }
}

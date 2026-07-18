package reservationsystem.integration;

import org.junit.jupiter.api.Test;
import reservationsystem.controller.SpaceController;
import reservationsystem.controller.SpaceFilterResult;
import reservationsystem.model.Space;
import reservationsystem.persistence.SpaceJsonRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class US3MinimumCapacityFilteringIntegrationTest {

    @Test
    void persistedSpacesAreFilteredInclusivelyThroughController() {
        SpaceController controller = new SpaceController(new SpaceJsonRepository());

        SpaceFilterResult result = controller.filterByMinimumCapacity("40");
        List<String> names = result.spaces().stream().map(Space::getName).toList();

        assertEquals(SpaceFilterResult.Status.SUCCESS, result.status());
        assertEquals(List.of(
                "Student Union Multipurpose Room",
                "University Center North Meeting Room"
        ), names);
        assertTrue(result.spaces().stream().anyMatch(space -> space.getCapacity() == 40));
        assertFalse(result.spaces().stream().anyMatch(space -> space.getCapacity() < 40));
    }

    @Test
    void maximumValidMinimumReturnsControlledEmptyResultWithoutJavaFx() {
        SpaceController controller = new SpaceController(new SpaceJsonRepository());

        SpaceFilterResult result = controller.filterByMinimumCapacity("500");

        assertEquals(SpaceFilterResult.Status.EMPTY, result.status());
        assertTrue(result.spaces().isEmpty());
        assertEquals("No spaces match the filter.", result.message());
    }
}

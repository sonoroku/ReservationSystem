package reservationsystem.controller;

import org.junit.jupiter.api.Test;
import reservationsystem.model.Space;
import reservationsystem.persistence.SpaceJsonRepository;
import reservationsystem.service.SpaceFeature;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SpaceFeatureFilteringTest {

    @Test
    void filtersByEachFeature() {
        SpaceController controller = controllerWith(
                space("Conference Room", "Interactive Whiteboard", "PC"),
                space("Computer Lab", "Computers", "Projector"),
                space("Meeting Room", "Whiteboard", "Projector")
        );

        assertEquals(
                List.of("Computer Lab", "Meeting Room"),
                names(controller.filterByFeatures(List.of(SpaceFeature.PROJECTOR)))
        );
        assertEquals(
                List.of("Conference Room", "Meeting Room"),
                names(controller.filterByFeatures(List.of(SpaceFeature.WHITEBOARD)))
        );
        assertEquals(
                List.of("Computer Lab", "Conference Room"),
                names(controller.filterByFeatures(List.of(SpaceFeature.COMPUTER)))
        );
    }

    @Test
    void requiresAllSelectedFeatures() {
        SpaceController controller = controllerWith(
                space("Zoom Room", "Projector", "PC"),
                space("Auditorium", "Projector", "Computers"),
                space("Projector Only", "Projector")
        );

        SpaceFilterResult result = controller.filterByFeatures(List.of(
                SpaceFeature.PROJECTOR,
                SpaceFeature.COMPUTER
        ));

        assertEquals(SpaceFilterResult.Status.SUCCESS, result.status());
        assertEquals(List.of("Auditorium", "Zoom Room"), names(result));
    }

    @Test
    void returnsEmptyWhenNothingMatches() {
        SpaceController controller = controllerWith(
                space("Projector Room", "Projector"),
                space("Computer Room", "PC")
        );

        SpaceFilterResult result = controller.filterByFeatures(List.of(
                SpaceFeature.PROJECTOR,
                SpaceFeature.WHITEBOARD,
                SpaceFeature.COMPUTER
        ));

        assertEquals(SpaceFilterResult.Status.EMPTY, result.status());
        assertTrue(result.spaces().isEmpty());
        assertEquals("No spaces match the filter.", result.message());
    }

    @Test
    void requiresAFeatureSelection() {
        SpaceController controller = controllerWith();

        SpaceFilterResult result = controller.filterByFeatures(List.of());

        assertEquals(SpaceFilterResult.Status.INVALID_INPUT, result.status());
        assertFalse(result.isValid());
        assertEquals("Select at least one feature.", result.message());
    }

    private SpaceController controllerWith(Space... spaces) {
        return new SpaceController(new FakeSpaceJsonRepository(List.of(spaces)));
    }

    private Space space(String name, String... features) {
        return new Space(1, name, "Building", 10, List.of(features));
    }

    private List<String> names(SpaceFilterResult result) {
        return result.spaces().stream().map(Space::getName).toList();
    }

    private static class FakeSpaceJsonRepository extends SpaceJsonRepository {
        private final List<Space> spaces;

        FakeSpaceJsonRepository(List<Space> spaces) {
            this.spaces = spaces;
        }

        @Override
        public List<Space> loadSpaces() {
            return spaces;
        }
    }
}

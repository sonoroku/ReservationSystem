package reservationsystem.integration;

import org.junit.jupiter.api.Test;
import reservationsystem.controller.SpaceController;
import reservationsystem.model.Space;
import reservationsystem.persistence.SpaceJsonRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class US2SpaceDetailsIntegrationTest {

    @Test
    void repositoryControllerFlowReturnsCompleteDetailsForPersistedSpace() {
        SpaceController controller = new SpaceController(new SpaceJsonRepository());
        Space persistedSpace = findSpaceByName(
                controller.getAllSpaces(),
                "Student Union Conference Room 1"
        );

        Space details = controller.getSelectedSpaceDetails(persistedSpace).orElseThrow();

        assertEquals(1, details.getId());
        assertEquals("Student Union Conference Room 1", details.getName());
        assertEquals("Student Union", details.getBuilding());
        assertEquals(10, details.getCapacity());
        assertEquals(
                List.of("Interactive Whiteboard", "PC", "Conference Table", "Wi-Fi"),
                details.getFeatures()
        );
    }

    @Test
    void repositoryControllerFlowExposesCompletePersistedFeatureList() {
        SpaceController controller = new SpaceController(new SpaceJsonRepository());
        Space persistedSpace = findSpaceByName(
                controller.getAllSpaces(),
                "Nevins Hall Computer Lab"
        );

        Space details = controller.getSelectedSpaceDetails(persistedSpace).orElseThrow();

        assertEquals("Nevins Hall", details.getBuilding());
        assertEquals(30, details.getCapacity());
        assertEquals(List.of("Computers", "Projector", "Wi-Fi"), details.getFeatures());
    }

    private Space findSpaceByName(List<Space> spaces, String name) {
        return spaces.stream()
                .filter(space -> space.getName().equals(name))
                .findFirst()
                .orElseThrow(() -> new AssertionError("Expected persisted space: " + name));
    }
}

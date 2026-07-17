package reservationsystem.controller;

import org.junit.jupiter.api.Test;
import reservationsystem.model.Space;
import reservationsystem.persistence.SpaceJsonRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SpaceControllerTest {
	
	@Test
    void getAllSpacesReturnsSpacesWhenDataExists() {
        SpaceController controller = new SpaceController(new FakeSpaceJsonRepository(List.of(
                new Space(1, "Library Study Room", "Library", 6, List.of("Whiteboard"))
        )));

        List<Space> spaces = controller.getAllSpaces();

        assertEquals(1, spaces.size());
        assertEquals("Library Study Room", spaces.get(0).getName());
        assertTrue(controller.hasSpaces());
    }

    @Test
    void getAllSpacesReturnsSpacesSortedAlphabeticallyByName() {
        SpaceController controller = new SpaceController(new FakeSpaceJsonRepository(List.of(
                new Space(1, "Zoom Room", "Library", 4, List.of()),
                new Space(2, "Auditorium", "Student Center", 100, List.of()),
                new Space(3, "Computer Lab", "Science Hall", 30, List.of())
        )));

        List<Space> spaces = controller.getAllSpaces();

        assertEquals("Auditorium", spaces.get(0).getName());
        assertEquals("Computer Lab", spaces.get(1).getName());
        assertEquals("Zoom Room", spaces.get(2).getName());
    }

    @Test
    void hasSpacesReturnsFalseWhenNoSpacesExist() {
        SpaceController controller = new SpaceController(new FakeSpaceJsonRepository(List.of()));

        List<Space> spaces = controller.getAllSpaces();

        assertTrue(spaces.isEmpty());
        assertFalse(controller.hasSpaces());
    }

    @Test
    void getSelectedSpaceDetailsReturnsCompleteSelectedSpaceData() {
        SpaceController controller = new SpaceController(new FakeSpaceJsonRepository(List.of()));
        Space selectedSpace = new Space(
                1,
                "Collaboration Room",
                "Library",
                8,
                List.of("Projector", "Whiteboard", "Computers")
        );

        Space result = controller.getSelectedSpaceDetails(selectedSpace).orElseThrow();

        assertSame(selectedSpace, result);
        assertEquals("Collaboration Room", result.getName());
        assertEquals("Library", result.getBuilding());
        assertEquals(8, result.getCapacity());
        assertEquals(List.of("Projector", "Whiteboard", "Computers"), result.getFeatures());
    }

    @Test
    void getSelectedSpaceDetailsReturnsSpaceWithNoFeatures() {
        SpaceController controller = new SpaceController(new FakeSpaceJsonRepository(List.of()));
        Space selectedSpace = new Space(
                2,
                "Quiet Study Room",
                "Student Center",
                4,
                List.of()
        );

        Space result = controller.getSelectedSpaceDetails(selectedSpace).orElseThrow();

        assertEquals("Student Center", result.getBuilding());
        assertEquals(4, result.getCapacity());
        assertTrue(result.getFeatures().isEmpty());
    }

    @Test
    void getSelectedSpaceDetailsReturnsEmptyWhenNoSpaceIsSelected() {
        SpaceController controller = new SpaceController(new FakeSpaceJsonRepository(List.of()));

        assertTrue(controller.getSelectedSpaceDetails(null).isEmpty());
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

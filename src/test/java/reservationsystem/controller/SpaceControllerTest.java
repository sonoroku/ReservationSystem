package reservationsystem.controller;

import org.junit.jupiter.api.Test;
import reservationsystem.controller.SpaceFilterResult.Status;
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
    void minimumCapacityFilterIncludesExactMatchesAndPreservesAlphabeticalOrder() {
        SpaceController controller = new SpaceController(new FakeSpaceJsonRepository(List.of(
                new Space(1, "Zoom Room", "Library", 40, List.of()),
                new Space(2, "Auditorium", "Student Center", 100, List.of()),
                new Space(3, "Computer Lab", "Science Hall", 39, List.of())
        )));

        SpaceFilterResult result = controller.filterByMinimumCapacity("40");

        assertEquals(Status.SUCCESS, result.status());
        assertTrue(result.isValid());
        assertEquals(List.of("Auditorium", "Zoom Room"),
                result.spaces().stream().map(Space::getName).toList());
        assertEquals(40, result.spaces().get(1).getCapacity());
    }

    @Test
    void minimumCapacityFilterReturnsControlledEmptyResult() {
        SpaceController controller = new SpaceController(new FakeSpaceJsonRepository(List.of(
                new Space(1, "Small Room", "Library", 10, List.of())
        )));

        SpaceFilterResult result = controller.filterByMinimumCapacity("500");

        assertEquals(Status.EMPTY, result.status());
        assertTrue(result.isValid());
        assertTrue(result.spaces().isEmpty());
        assertEquals("No spaces match the filter.", result.message());
    }

    @Test
    void minimumCapacityFilterRejectsInvalidInputsWithClearErrors() {
        SpaceController controller = new SpaceController(new FakeSpaceJsonRepository(List.of()));

        for (String input : new String[]{"people", "-1", "0", "501"}) {
            SpaceFilterResult result = controller.filterByMinimumCapacity(input);

            assertEquals(Status.INVALID_INPUT, result.status(), "input: " + input);
            assertFalse(result.isValid(), "input: " + input);
            assertTrue(result.spaces().isEmpty(), "input: " + input);
            assertFalse(result.message().isBlank(), "input: " + input);
        }
    }

    @Test
    void minimumCapacityFilterRejectsMissingInput() {
        SpaceController controller = new SpaceController(new FakeSpaceJsonRepository(List.of()));

        assertEquals("Minimum capacity is required.", controller.filterByMinimumCapacity(null).message());
        assertEquals("Minimum capacity is required.", controller.filterByMinimumCapacity("  ").message());
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

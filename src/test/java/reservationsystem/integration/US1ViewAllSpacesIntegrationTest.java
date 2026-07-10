package reservationsystem.integration;

import org.junit.jupiter.api.Test;
import reservationsystem.controller.SpaceController;
import reservationsystem.model.Space;
import reservationsystem.persistence.SpaceJsonRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class US1ViewAllSpacesIntegrationTest {

    @Test
    void controllerCanRetrieveSpacesThroughPersistenceLayer() {
        // US-1 Acceptance Test 1:
        // Given the application has predefined campus spaces,
        // when the user opens the space list,
        // then all available spaces are displayed.

        SpaceJsonRepository repository = new SpaceJsonRepository();
        SpaceController controller = new SpaceController(repository);

        List<Space> spaces = controller.getAllSpaces();

        assertNotNull(spaces);
        assertFalse(spaces.isEmpty());
    }

    @Test
    void returnedSpacesAreSortedAlphabeticallyByName() {
        // US-1 Acceptance Test 2:
        // Given multiple spaces exist,
        // when the space list is displayed,
        // then spaces are sorted alphabetically by name.

        SpaceJsonRepository repository = new SpaceJsonRepository();
        SpaceController controller = new SpaceController(repository);

        List<Space> spaces = controller.getAllSpaces();

        for (int i = 0; i < spaces.size() - 1; i++) {
            String currentName = spaces.get(i).getName();
            String nextName = spaces.get(i + 1).getName();

            assertTrue(
                    currentName.compareToIgnoreCase(nextName) <= 0,
                    "Spaces should be sorted alphabetically by name"
            );
        }
    }

    @Test
    void hasSpacesReturnsTrueWhenSpacesExist() {
        // US-1 Acceptance Test 1:
        // Given predefined campus spaces exist,
        // when the controller checks for spaces,
        // then it confirms spaces are available.

        SpaceJsonRepository repository = new SpaceJsonRepository();
        SpaceController controller = new SpaceController(repository);

        assertTrue(controller.hasSpaces());
    }
}

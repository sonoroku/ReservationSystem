package reservationsystem.persistence;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import reservationsystem.model.User;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UserJsonRepositoryTest {

    @TempDir
    Path temporaryDirectory;

    @Test
    void saveAndLoadUsersRoundTripsThroughIsolatedRuntimeFile() {
        Path runtimeFile = temporaryDirectory.resolve("users.json");
        UserJsonRepository repository = new UserJsonRepository(runtimeFile);

        repository.saveUsers(List.of(
                new User("student", "student123", false),
                new User("admin", "admin123", true)
        ));

        List<User> loadedUsers = repository.loadUsers();

        assertEquals(2, loadedUsers.size());
        assertEquals("student", loadedUsers.get(0).getUsername());
        assertEquals("student123", loadedUsers.get(0).getPassword());
        assertFalse(loadedUsers.get(0).isAdmin());
        assertEquals("admin", loadedUsers.get(1).getUsername());
        assertTrue(loadedUsers.get(1).isAdmin());
    }

    @Test
    void invalidSavePreservesExistingRuntimeFile() throws Exception {
        Path runtimeFile = temporaryDirectory.resolve("users.json");
        UserJsonRepository repository = new UserJsonRepository(runtimeFile);
        repository.saveUsers(List.of(
                new User("student", "student123", false)
        ));
        String originalJson = Files.readString(runtimeFile);

        List<User> invalidUsers = new ArrayList<>();
        invalidUsers.add(new User("admin", "admin123", true));
        invalidUsers.add(null);

        assertThrows(
                IllegalArgumentException.class,
                () -> repository.saveUsers(invalidUsers));
        assertEquals(originalJson, Files.readString(runtimeFile));
    }

    @Test
    void malformedRuntimeJsonReturnsControlledRepositoryFailure()
            throws Exception {
        Path runtimeFile = temporaryDirectory.resolve("users.json");
        Files.writeString(runtimeFile, "{not valid json");
        UserJsonRepository repository = new UserJsonRepository(runtimeFile);

        assertThrows(IllegalStateException.class, repository::loadUsers);
    }
}

package reservationsystem.persistence;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import reservationsystem.model.User;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class US20UserPersistenceIntegrationTest {

    private static final String STARTER_USERS_RESOURCE =
            "/data/users.json";

    @TempDir
    Path temporaryDirectory;

    @Test
    void missingRuntimeFileInitializesFromStarterUsersWithoutChangingResource()
            throws IOException {
        Path runtimeFile = temporaryDirectory.resolve("users.json");
        byte[] starterContentsBefore = readStarterUserContents();

        UserJsonRepository repository =
                new UserJsonRepository(runtimeFile);
        List<User> initializedUsers = repository.loadUsers();

        assertTrue(Files.exists(runtimeFile));
        assertEquals(2, initializedUsers.size());
        assertUser(initializedUsers.get(0), "student", "student123", false);
        assertUser(initializedUsers.get(1), "admin", "admin123", true);

        UserJsonRepository reloadedRepository =
                new UserJsonRepository(runtimeFile);
        List<User> reloadedUsers = reloadedRepository.loadUsers();

        assertUsersEqual(initializedUsers, reloadedUsers);
        assertArrayEquals(
                starterContentsBefore,
                readStarterUserContents()
        );
    }

    @Test
    void newlyRegisteredUserSurvivesRepositoryRecreation() {
        Path runtimeFile = temporaryDirectory.resolve("registered-users.json");
        UserJsonRepository repository =
                new UserJsonRepository(runtimeFile);

        List<User> usersWithRegistration =
                new ArrayList<>(repository.loadUsers());
        usersWithRegistration.add(
                new User("newstudent", "newPassword123", false)
        );
        repository.saveUsers(usersWithRegistration);

        UserJsonRepository restartedRepository =
                new UserJsonRepository(runtimeFile);
        List<User> reloadedUsers = restartedRepository.loadUsers();

        assertEquals(3, reloadedUsers.size());
        assertUser(
                reloadedUsers.get(0),
                "student",
                "student123",
                false
        );
        assertUser(
                reloadedUsers.get(1),
                "admin",
                "admin123",
                true
        );
        assertUser(
                reloadedUsers.get(2),
                "newstudent",
                "newPassword123",
                false
        );
    }

    @Test
    void intentionallyEmptyRuntimeListRemainsEmptyAfterRestart() {
        Path runtimeFile = temporaryDirectory.resolve("empty-users.json");
        UserJsonRepository repository =
                new UserJsonRepository(runtimeFile);

        repository.saveUsers(List.of());

        assertTrue(Files.exists(runtimeFile));

        UserJsonRepository restartedRepository =
                new UserJsonRepository(runtimeFile);
        List<User> reloadedUsers = restartedRepository.loadUsers();

        assertTrue(reloadedUsers.isEmpty());
    }

    private byte[] readStarterUserContents() throws IOException {
        try (InputStream inputStream =
                     UserJsonRepository.class.getResourceAsStream(
                             STARTER_USERS_RESOURCE
                     )) {
            if (inputStream == null) {
                throw new IOException(
                        "Starter users resource was not found"
                );
            }

            return inputStream.readAllBytes();
        }
    }

    private void assertUsersEqual(
            List<User> expectedUsers,
            List<User> actualUsers
    ) {
        assertEquals(expectedUsers.size(), actualUsers.size());

        for (int index = 0; index < expectedUsers.size(); index++) {
            User expectedUser = expectedUsers.get(index);
            User actualUser = actualUsers.get(index);

            assertUser(
                    actualUser,
                    expectedUser.getUsername(),
                    expectedUser.getPassword(),
                    expectedUser.isAdmin()
            );
        }
    }

    private void assertUser(
            User user,
            String expectedUsername,
            String expectedPassword,
            boolean expectedAdmin
    ) {
        assertEquals(expectedUsername, user.getUsername());
        assertEquals(expectedPassword, user.getPassword());

        if (expectedAdmin) {
            assertTrue(user.isAdmin());
        } else {
            assertFalse(user.isAdmin());
        }
    }
}

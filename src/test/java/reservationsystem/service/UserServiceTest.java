package reservationsystem.service;

import org.junit.jupiter.api.Test;
import reservationsystem.model.User;
import reservationsystem.persistence.UserJsonRepository;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserServiceTest {
	
	@Test
    void registerUserCreatesAndSavesValidAccount() {
        FakeUserJsonRepository repository =
                new FakeUserJsonRepository(List.of());

        UserService service = new UserService(repository);

        RegistrationResult result =
                service.registerUser("newstudent", "secret1");

        assertTrue(result.isSuccessful());
        assertEquals(
                "Account registered successfully",
                result.getMessage()
        );
        assertTrue(repository.wasSaved());
        assertEquals(1, repository.getSavedUsers().size());

        User savedUser = repository.getSavedUsers().get(0);

        assertEquals("newstudent", savedUser.getUsername());
        assertEquals("secret1", savedUser.getPassword());
        assertFalse(savedUser.isAdmin());
    }

    @Test
    void registerUserRejectsMissingUsername() {
        FakeUserJsonRepository repository =
                new FakeUserJsonRepository(List.of());

        UserService service = new UserService(repository);

        RegistrationResult result =
                service.registerUser("   ", "secret1");

        assertFalse(result.isSuccessful());
        assertEquals("Username is required", result.getMessage());
        assertFalse(repository.wasSaved());
    }

    @Test
    void registerUserRejectsMissingPassword() {
        FakeUserJsonRepository repository =
                new FakeUserJsonRepository(List.of());

        UserService service = new UserService(repository);

        RegistrationResult result =
                service.registerUser("newstudent", "   ");

        assertFalse(result.isSuccessful());
        assertEquals("Password is required", result.getMessage());
        assertFalse(repository.wasSaved());
    }

    @Test
    void registerUserRejectsPasswordShorterThanSixCharacters() {
        FakeUserJsonRepository repository =
                new FakeUserJsonRepository(List.of());

        UserService service = new UserService(repository);

        RegistrationResult result =
                service.registerUser("newstudent", "12345");

        assertFalse(result.isSuccessful());
        assertEquals(
                "Password must be at least 6 characters",
                result.getMessage()
        );
        assertFalse(repository.wasSaved());
    }

    @Test
    void registerUserRejectsDuplicateUsername() {
        FakeUserJsonRepository repository =
                new FakeUserJsonRepository(List.of(
                        new User("student", "student123", false)
                ));

        UserService service = new UserService(repository);

        RegistrationResult result =
                service.registerUser("student", "newpassword");

        assertFalse(result.isSuccessful());
        assertEquals(
                "Username is already registered",
                result.getMessage()
        );
        assertFalse(repository.wasSaved());
    }

    @Test
    void registerUserRejectsDuplicateUsernameIgnoringCase() {
        FakeUserJsonRepository repository =
                new FakeUserJsonRepository(List.of(
                        new User("student", "student123", false)
                ));

        UserService service = new UserService(repository);

        RegistrationResult result =
                service.registerUser("STUDENT", "newpassword");

        assertFalse(result.isSuccessful());
        assertEquals(
                "Username is already registered",
                result.getMessage()
        );
        assertFalse(repository.wasSaved());
    }

    private static class FakeUserJsonRepository
            extends UserJsonRepository {

        private final List<User> existingUsers;
        private List<User> savedUsers;

        FakeUserJsonRepository(List<User> existingUsers) {
            this.existingUsers = new ArrayList<>(existingUsers);
        }

        @Override
        public List<User> loadUsers() {
            return new ArrayList<>(existingUsers);
        }

        @Override
        public void saveUsers(List<User> users) {
            savedUsers = new ArrayList<>(users);
        }

        boolean wasSaved() {
            return savedUsers != null;
        }

        List<User> getSavedUsers() {
            return savedUsers;
        }
    }

}

package reservationsystem.service;

import reservationsystem.model.User;
import reservationsystem.persistence.UserJsonRepository;

import java.util.ArrayList;
import java.util.List;

public class UserService {
	
	private static final int MINIMUM_PASSWORD_LENGTH = 6;

    private final UserJsonRepository userJsonRepository;

    public UserService() {
        this(new UserJsonRepository());
    }

    public UserService(UserJsonRepository userJsonRepository) {
        if (userJsonRepository == null) {
            throw new IllegalArgumentException(
                    "User repository cannot be null"
            );
        }

        this.userJsonRepository = userJsonRepository;
    }

    public RegistrationResult registerUser(
            String username,
            String password
    ) {
        if (username == null || username.isBlank()) {
            return RegistrationResult.failure(
                    "Username is required"
            );
        }

        if (password == null || password.isBlank()) {
            return RegistrationResult.failure(
                    "Password is required"
            );
        }

        if (password.length() < MINIMUM_PASSWORD_LENGTH) {
            return RegistrationResult.failure(
                    "Password must be at least 6 characters"
            );
        }

        String normalizedUsername = username.trim();
        List<User> existingUsers = userJsonRepository.loadUsers();

        boolean usernameExists = existingUsers.stream()
                .anyMatch(user ->
                        user.getUsername().equalsIgnoreCase(
                                normalizedUsername
                        )
                );

        if (usernameExists) {
            return RegistrationResult.failure(
                    "Username is already registered"
            );
        }

        List<User> updatedUsers = new ArrayList<>(existingUsers);
        updatedUsers.add(
                new User(normalizedUsername, password, false)
        );

        userJsonRepository.saveUsers(updatedUsers);

        return RegistrationResult.success();
    }

}

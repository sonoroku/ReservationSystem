package reservationsystem.controller;

import reservationsystem.model.User;
import reservationsystem.persistence.UserJsonRepository;
import reservationsystem.service.AuthenticationResult;
import reservationsystem.service.AuthenticationService;
import reservationsystem.service.CurrentUserProvider;

import java.util.List;

public class AuthenticationController implements CurrentUserProvider {
	
	   private final AuthenticationService authenticationService;
	    private final UserJsonRepository userJsonRepository;

	    public AuthenticationController() {
	        this(
	                new AuthenticationService(),
	                new UserJsonRepository()
	        );
	    }

	    public AuthenticationController(
	            AuthenticationService authenticationService,
	            UserJsonRepository userJsonRepository
	    ) {
	        if (authenticationService == null) {
	            throw new IllegalArgumentException(
	                    "Authentication service cannot be null"
	            );
	        }

	        if (userJsonRepository == null) {
	            throw new IllegalArgumentException(
	                    "User repository cannot be null"
	            );
	        }

	        this.authenticationService = authenticationService;
	        this.userJsonRepository = userJsonRepository;
	    }

	    public AuthenticationResult login(
	            String username,
	            String password
	    ) {
	        try {
	            List<User> users = userJsonRepository.loadUsers();

	            return authenticationService.login(
	                    username,
	                    password,
	                    users
	            );
	        } catch (IllegalStateException exception) {
	            authenticationService.logout();

	            return AuthenticationResult.failure(
	                    "Unable to load user accounts. Please try again."
	            );
	        }
	    }

	    public void logout() {
	        authenticationService.logout();
	    }

	    public boolean isLoggedIn() {
	        return authenticationService.isLoggedIn();
	    }

	    public User getCurrentUser() {
	        return authenticationService.getCurrentUser();
	    }

	    @Override
	    public String getCurrentUserId() {
	        User currentUser = getCurrentUser();

	        if (currentUser == null) {
	            throw new IllegalStateException("No user is logged in");
	        }

	        return currentUser.getUsername();
	    }

}

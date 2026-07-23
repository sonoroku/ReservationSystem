package reservationsystem.service;

import reservationsystem.model.User;

public class AuthorizationService {

    private final AuthenticatedUserProvider authenticatedUserProvider;

    public AuthorizationService(
            AuthenticatedUserProvider authenticatedUserProvider
    ) {
        if (authenticatedUserProvider == null) {
            throw new IllegalArgumentException(
                    "Authenticated user provider cannot be null"
            );
        }

        this.authenticatedUserProvider = authenticatedUserProvider;
    }

    public boolean isCurrentUserAdmin() {
        User currentUser = authenticatedUserProvider.getCurrentUser();
        return currentUser != null && currentUser.isAdmin();
    }

    public AuthorizationResult checkAdminAccess() {
        User currentUser = authenticatedUserProvider.getCurrentUser();

        if (currentUser == null) {
            return AuthorizationResult.denied(
                    "You must be logged in to access this feature"
            );
        }

        if (!currentUser.isAdmin()) {
            return AuthorizationResult.denied(
                    "Administrator access is required"
            );
        }

        return AuthorizationResult.authorized();
    }
}

package reservationsystem.service;

import reservationsystem.model.User;

public interface AuthenticatedUserProvider {

    User getCurrentUser();
}

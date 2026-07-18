package reservationsystem.service;

public class DefaultUserProvider implements CurrentUserProvider {

    public static final String DEFAULT_USER_ID = "student";

    @Override
    public String getCurrentUserId() {
        return DEFAULT_USER_ID;
    }
}

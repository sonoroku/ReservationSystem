package reservationsystem.persistence;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import reservationsystem.model.User;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

public class UserJsonRepository {

    private static final String USERS_RESOURCE_PATH = "/data/users.json";

    private static final Path DEFAULT_USERS_RUNTIME_FILE_PATH =
            Path.of("app-data", "users.json");

    private final Gson gson;
    private final Path usersRuntimeFilePath;

    public UserJsonRepository() {
        this(DEFAULT_USERS_RUNTIME_FILE_PATH);
    }

    UserJsonRepository(Path usersRuntimeFilePath) {
        if (usersRuntimeFilePath == null) {
            throw new IllegalArgumentException(
                    "Users runtime file path cannot be null.");
        }

        this.usersRuntimeFilePath = usersRuntimeFilePath;
        gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();
    }

    public List<User> loadUsers() {
        if (Files.exists(usersRuntimeFilePath)) {
            return loadUsersFromRuntimeFile();
        }

        List<User> starterUsers = loadUsersFromResource();
        saveUsers(starterUsers);

        return starterUsers;
    }

    public void saveUsers(List<User> users) {
        if (users == null) {
            throw new IllegalArgumentException("Users list cannot be null.");
        }

        JsonArray usersJson = convertUsersToJson(users);
        Path temporaryFile = null;

        try {
            Path destination = usersRuntimeFilePath.toAbsolutePath();
            Path parentDirectory = destination.getParent();

            if (parentDirectory != null) {
                Files.createDirectories(parentDirectory);
            }

            temporaryFile = Files.createTempFile(
                    parentDirectory,
                    "users-",
                    ".tmp");

            try (Writer writer = Files.newBufferedWriter(
                    temporaryFile,
                    StandardCharsets.UTF_8)) {
                gson.toJson(usersJson, writer);
            }

            replaceRuntimeFile(temporaryFile, destination);
            temporaryFile = null;
        } catch (IOException exception) {
            throw new IllegalStateException(
                    "Unable to save users to runtime JSON.",
                    exception);
        } finally {
            if (temporaryFile != null) {
                try {
                    Files.deleteIfExists(temporaryFile);
                } catch (IOException ignored) {
                    // Preserve the original write failure.
                }
            }
        }
    }

    private void replaceRuntimeFile(
            Path temporaryFile,
            Path destination) throws IOException {
        try {
            Files.move(
                    temporaryFile,
                    destination,
                    StandardCopyOption.ATOMIC_MOVE,
                    StandardCopyOption.REPLACE_EXISTING);
        } catch (AtomicMoveNotSupportedException exception) {
            Files.move(
                    temporaryFile,
                    destination,
                    StandardCopyOption.REPLACE_EXISTING);
        }
    }

    private List<User> loadUsersFromRuntimeFile() {
        try (Reader reader = Files.newBufferedReader(
                usersRuntimeFilePath,
                StandardCharsets.UTF_8)) {

            return readUsers(reader);
        } catch (IOException | RuntimeException exception) {
            throw new IllegalStateException(
                    "Unable to load users from runtime JSON.",
                    exception);
        }
    }

    private List<User> loadUsersFromResource() {
        try (Reader reader = new java.io.InputStreamReader(
                getRequiredResourceStream(),
                StandardCharsets.UTF_8)) {

            return readUsers(reader);
        } catch (IOException | RuntimeException exception) {
            throw new IllegalStateException(
                    "Unable to load starter users JSON.",
                    exception);
        }
    }

    private java.io.InputStream getRequiredResourceStream() {
        java.io.InputStream inputStream =
                UserJsonRepository.class.getResourceAsStream(
                        USERS_RESOURCE_PATH);

        if (inputStream == null) {
            throw new IllegalStateException(
                    "Starter users JSON was not found at "
                            + USERS_RESOURCE_PATH);
        }

        return inputStream;
    }

    private List<User> readUsers(Reader reader) {
        JsonElement rootElement = JsonParser.parseReader(reader);

        if (!rootElement.isJsonArray()) {
            throw new IllegalStateException(
                    "Users JSON must contain an array.");
        }

        JsonArray usersJson = rootElement.getAsJsonArray();
        List<User> users = new ArrayList<>();

        for (JsonElement element : usersJson) {
            if (!element.isJsonObject()) {
                throw new IllegalStateException(
                        "Each user entry must be a JSON object.");
            }

            users.add(convertJsonToUser(element.getAsJsonObject()));
        }

        return users;
    }

    private User convertJsonToUser(JsonObject userJson) {
        String username =
                getRequiredString(userJson, "username");

        String password =
                getRequiredString(userJson, "password");

        boolean isAdmin =
                getRequiredBoolean(userJson, "isAdmin");

        return new User(username, password, isAdmin);
    }

    private JsonArray convertUsersToJson(List<User> users) {
        JsonArray usersJson = new JsonArray();

        for (User user : users) {
            if (user == null) {
                throw new IllegalArgumentException(
                        "Users list cannot contain null values.");
            }

            if (user.getUsername() == null
                    || user.getUsername().isBlank()) {
                throw new IllegalArgumentException(
                        "Usernames cannot be null or blank.");
            }

            if (user.getPassword() == null
                    || user.getPassword().isBlank()) {
                throw new IllegalArgumentException(
                        "Passwords cannot be null or blank.");
            }

            JsonObject userJson = new JsonObject();

            userJson.addProperty(
                    "username",
                    user.getUsername());

            userJson.addProperty(
                    "password",
                    user.getPassword());

            userJson.addProperty(
                    "isAdmin",
                    user.isAdmin());

            usersJson.add(userJson);
        }

        return usersJson;
    }

    private String getRequiredString(
            JsonObject jsonObject,
            String propertyName) {

        if (!jsonObject.has(propertyName)
                || jsonObject.get(propertyName).isJsonNull()) {

            throw new IllegalStateException(
                    "Missing required user property: "
                            + propertyName);
        }

        return jsonObject
                .get(propertyName)
                .getAsString();
    }
    
    private boolean getRequiredBoolean(
            JsonObject jsonObject,
            String propertyName) {

        if (!jsonObject.has(propertyName)
                || jsonObject.get(propertyName).isJsonNull()) {

            throw new IllegalStateException(
                    "Missing required user property: "
                            + propertyName);
        }

        return jsonObject
                .get(propertyName)
                .getAsBoolean();
    }
}

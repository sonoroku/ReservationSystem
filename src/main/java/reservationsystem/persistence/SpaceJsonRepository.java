package reservationsystem.persistence;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import reservationsystem.model.Space;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class SpaceJsonRepository {

    private static final String SPACES_FILE_PATH = "/data/spaces.json";

    public List<Space> loadSpaces() {
        InputStream inputStream = getClass().getResourceAsStream(SPACES_FILE_PATH);

        if (inputStream == null) {
            throw new IllegalStateException("Could not find " + SPACES_FILE_PATH);
        }

        try (Reader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8)) {
            JsonArray spacesJson = JsonParser.parseReader(reader).getAsJsonArray();
            return parseSpaces(spacesJson);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to load spaces from " + SPACES_FILE_PATH, e);
        }
    }

    private List<Space> parseSpaces(JsonArray spacesJson) {
        List<Space> spaces = new ArrayList<>();

        for (JsonElement element : spacesJson) {
            Space space = parseSpace(element.getAsJsonObject());
            spaces.add(space);
        }

        return spaces;
    }

    private Space parseSpace(JsonObject spaceJson) {
        int id = spaceJson.get("id").getAsInt();
        String name = spaceJson.get("name").getAsString();
        String building = spaceJson.get("building").getAsString();
        int capacity = spaceJson.get("capacity").getAsInt();

        List<String> features = new ArrayList<>();
        JsonArray featuresJson = spaceJson.getAsJsonArray("features");

        for (JsonElement featureElement : featuresJson) {
            features.add(featureElement.getAsString());
        }

        return new Space(id, name, building, capacity, features);
    }
}
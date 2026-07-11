package reservationsystem.persistence;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import reservationsystem.model.Reservation;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Writer;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class ReservationJsonRepository {

    private static final String RESERVATIONS_RESOURCE_PATH = "/data/reservations.json";
    private static final Path RESERVATIONS_FILE_PATH =
            Path.of("src", "main", "resources", "data", "reservations.json");

    private final Gson gson;

    public ReservationJsonRepository() {
        this.gson = new GsonBuilder().setPrettyPrinting().create();
    }

    public List<Reservation> loadReservations() {
        InputStream inputStream = getClass().getResourceAsStream(RESERVATIONS_RESOURCE_PATH);

        if (inputStream == null) {
            throw new IllegalStateException("Could not find " + RESERVATIONS_RESOURCE_PATH);
        }

        try (Reader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8)) {
            JsonArray reservationsJson = JsonParser.parseReader(reader).getAsJsonArray();
            return parseReservations(reservationsJson);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to load reservations from " + RESERVATIONS_RESOURCE_PATH, e);
        }
    }

    public void saveReservations(List<Reservation> reservations) {
        if (reservations == null) {
            throw new IllegalArgumentException("Reservations list cannot be null");
        }

        JsonArray reservationsJson = new JsonArray();

        for (Reservation reservation : reservations) {
            reservationsJson.add(convertReservationToJson(reservation));
        }

        try {
            Files.createDirectories(RESERVATIONS_FILE_PATH.getParent());

            try (Writer writer = Files.newBufferedWriter(RESERVATIONS_FILE_PATH, StandardCharsets.UTF_8)) {
                gson.toJson(reservationsJson, writer);
            }
        } catch (IOException e) {
            throw new IllegalStateException("Failed to save reservations to " + RESERVATIONS_FILE_PATH, e);
        }
    }

    private List<Reservation> parseReservations(JsonArray reservationsJson) {
        List<Reservation> reservations = new ArrayList<>();

        for (JsonElement element : reservationsJson) {
            Reservation reservation = parseReservation(element.getAsJsonObject());
            reservations.add(reservation);
        }

        return reservations;
    }

    private Reservation parseReservation(JsonObject reservationJson) {
        int id = reservationJson.get("id").getAsInt();
        int spaceId = reservationJson.get("spaceId").getAsInt();
        String userId = reservationJson.get("userId").getAsString();

        LocalDate date = LocalDate.parse(reservationJson.get("date").getAsString());
        LocalTime startTime = LocalTime.parse(reservationJson.get("startTime").getAsString());
        LocalTime endTime = LocalTime.parse(reservationJson.get("endTime").getAsString());

        return new Reservation(id, spaceId, userId, date, startTime, endTime);
    }

    private JsonObject convertReservationToJson(Reservation reservation) {
        JsonObject reservationJson = new JsonObject();

        reservationJson.addProperty("id", reservation.getId());
        reservationJson.addProperty("spaceId", reservation.getSpaceId());
        reservationJson.addProperty("userId", reservation.getUserId());
        reservationJson.addProperty("date", reservation.getDate().toString());
        reservationJson.addProperty("startTime", reservation.getStartTime().toString());
        reservationJson.addProperty("endTime", reservation.getEndTime().toString());

        return reservationJson;
    }
}
package reservationsystem.persistence;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import reservationsystem.model.Reservation;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class ReservationJsonRepository {

    private static final String RESERVATIONS_FILE_PATH = "/data/reservations.json";

    public List<Reservation> loadReservations() {
        InputStream inputStream = getClass().getResourceAsStream(RESERVATIONS_FILE_PATH);

        if (inputStream == null) {
            throw new IllegalStateException("Could not find " + RESERVATIONS_FILE_PATH);
        }

        try (Reader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8)) {
            JsonArray reservationsJson = JsonParser.parseReader(reader).getAsJsonArray();
            return parseReservations(reservationsJson);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to load reservations from " + RESERVATIONS_FILE_PATH, e);
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
}
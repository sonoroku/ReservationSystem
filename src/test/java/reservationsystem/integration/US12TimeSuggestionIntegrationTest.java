package reservationsystem.integration;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import reservationsystem.controller.TimeSuggestionController;
import reservationsystem.model.Reservation;
import reservationsystem.model.TimeSlot;
import reservationsystem.persistence.ReservationJsonRepository;
import reservationsystem.service.TimeSuggestionResult;
import reservationsystem.service.TimeSuggestionService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class US12TimeSuggestionIntegrationTest {
	
	private static final LocalDate TEST_DATE =
            LocalDate.of(2026, 7, 20);

    @TempDir
    Path tempDirectory;

    @Test
    void controllerUsesPersistedReservationsWithoutChangingRuntimeData()
            throws IOException {

        Path runtimeFile =
                tempDirectory.resolve("reservations.json");

        ReservationJsonRepository repository =
                new ReservationJsonRepository(runtimeFile);

        repository.saveReservations(List.of(
                new Reservation(
                        1,
                        1,
                        "student",
                        TEST_DATE,
                        LocalTime.of(10, 0),
                        LocalTime.of(11, 0)
                ),
                new Reservation(
                        2,
                        2,
                        "student",
                        TEST_DATE,
                        LocalTime.of(13, 0),
                        LocalTime.of(14, 0)
                )
        ));

        String runtimeDataBeforeSuggestion =
                Files.readString(runtimeFile);

        TimeSuggestionController controller =
                new TimeSuggestionController(
                        new TimeSuggestionService(),
                        repository
                );

        TimeSuggestionResult result =
                controller.suggestAvailableTimes(
                        1,
                        TEST_DATE,
                        30
                );

        assertTrue(result.isSuccessful());

        List<LocalTime> suggestedStartTimes =
                result.getSuggestions()
                        .stream()
                        .map(TimeSlot::getStartTime)
                        .toList();

        assertTrue(
                suggestedStartTimes.contains(LocalTime.of(9, 0))
        );
        assertFalse(
                suggestedStartTimes.contains(LocalTime.of(9, 30))
        );
        assertFalse(
                suggestedStartTimes.contains(LocalTime.of(10, 0))
        );
        assertFalse(
                suggestedStartTimes.contains(LocalTime.of(10, 30))
        );
        assertFalse(
                suggestedStartTimes.contains(LocalTime.of(11, 0))
        );
        assertTrue(
                suggestedStartTimes.contains(LocalTime.of(11, 30))
        );

        String runtimeDataAfterSuggestion =
                Files.readString(runtimeFile);

        assertEquals(
                runtimeDataBeforeSuggestion,
                runtimeDataAfterSuggestion
        );
    }

    @Test
    void controllerLoadsReservationsOnceAndNeverSaves() {
        TrackingReservationRepository repository =
                new TrackingReservationRepository(
                        tempDirectory.resolve("tracking.json"),
                        List.of(
                                new Reservation(
                                        1,
                                        1,
                                        "student",
                                        TEST_DATE,
                                        LocalTime.of(10, 0),
                                        LocalTime.of(11, 0)
                                )
                        )
                );

        TimeSuggestionController controller =
                new TimeSuggestionController(
                        new TimeSuggestionService(),
                        repository
                );

        TimeSuggestionResult result =
                controller.suggestAvailableTimes(
                        1,
                        TEST_DATE,
                        30
                );

        assertTrue(result.isSuccessful());
        assertEquals(1, repository.getLoadCount());
        assertEquals(0, repository.getSaveCount());
    }

    private static class TrackingReservationRepository
            extends ReservationJsonRepository {

        private final List<Reservation> reservations;
        private int loadCount;
        private int saveCount;

        TrackingReservationRepository(
                Path runtimeFile,
                List<Reservation> reservations
        ) {
            super(runtimeFile);
            this.reservations = new ArrayList<>(reservations);
        }

        @Override
        public List<Reservation> loadReservations() {
            loadCount++;
            return new ArrayList<>(reservations);
        }

        @Override
        public void saveReservations(
                List<Reservation> reservations
        ) {
            saveCount++;
        }

        int getLoadCount() {
            return loadCount;
        }

        int getSaveCount() {
            return saveCount;
        }
    }

}

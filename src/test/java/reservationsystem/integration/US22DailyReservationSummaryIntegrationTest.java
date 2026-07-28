package reservationsystem.integration;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import reservationsystem.controller.ReservationController;
import reservationsystem.model.DailyReservationSummary;
import reservationsystem.model.Reservation;
import reservationsystem.persistence.ReservationJsonRepository;
import reservationsystem.service.DailyReservationSummaryResult;
import reservationsystem.service.ReservationService;

import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class US22DailyReservationSummaryIntegrationTest {

    @TempDir
    Path temporaryDirectory;

    @Test
    void controllerSummarizesOnlyCurrentUserReservationsInStableOrder() {
        ReservationJsonRepository repository = repository(
                "daily-summary.json"
        );
        repository.saveReservations(List.of(
                reservation(7, "student", 12, 11),
                reservation(3, "admin", 10, 8),
                reservation(5, "student", 10, 13),
                reservation(2, "student", 10, 9)
        ));

        ReservationController controller = new ReservationController(
                repository,
                new ReservationService(),
                () -> "student"
        );

        DailyReservationSummaryResult result =
                controller.getDailyReservationSummary();

        assertEquals(
                DailyReservationSummaryResult.Status.SUCCESS,
                result.getStatus()
        );
        assertEquals(
                List.of(
                        LocalDate.of(2026, 8, 10),
                        LocalDate.of(2026, 8, 12)
                ),
                result.getSummaries().stream()
                        .map(DailyReservationSummary::getDate)
                        .toList()
        );
        assertEquals(
                List.of(2, 5),
                result.getSummaries().get(0).getReservations().stream()
                        .map(Reservation::getId)
                        .toList()
        );
        assertEquals(2, result.getSummaries().get(0).getReservationCount());
        assertTrue(result.getSummaries().stream()
                .flatMap(summary -> summary.getReservations().stream())
                .allMatch(reservation ->
                        "student".equals(reservation.getUserId())));
    }

    @Test
    void controllerReturnsEmptySummaryForCurrentUserWithoutReservations() {
        ReservationJsonRepository repository = repository(
                "empty-daily-summary.json"
        );
        repository.saveReservations(List.of(
                reservation(1, "admin", 10, 9)
        ));

        ReservationController controller = new ReservationController(
                repository,
                new ReservationService(),
                () -> "student"
        );

        DailyReservationSummaryResult result =
                controller.getDailyReservationSummary();

        assertTrue(result.isEmpty());
        assertEquals("No reservations found.", result.getMessage());
    }

    private ReservationJsonRepository repository(String fileName) {
        return new ReservationJsonRepository(
                temporaryDirectory.resolve(fileName)
        );
    }

    private Reservation reservation(
            int id,
            String userId,
            int day,
            int startHour
    ) {
        return new Reservation(
                id,
                1,
                userId,
                LocalDate.of(2026, 8, day),
                LocalTime.of(startHour, 0),
                LocalTime.of(startHour + 1, 0)
        );
    }
}

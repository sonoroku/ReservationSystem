package reservationsystem.service;

import org.junit.jupiter.api.Test;
import reservationsystem.model.DailyReservationSummary;
import reservationsystem.model.Reservation;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DailyReservationSummaryServiceTest {

    private final DailyReservationSummaryService service =
            new DailyReservationSummaryService();

    @Test
    void groupsReservationsByChronologicalDateAndCalculatesCounts() {
        DailyReservationSummaryResult result = service.summarize(List.of(
                reservation(3, 12, 11),
                reservation(1, 10, 13),
                reservation(2, 10, 9)
        ));

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
        assertEquals(2, result.getSummaries().get(0).getReservationCount());
        assertEquals(1, result.getSummaries().get(1).getReservationCount());
    }

    @Test
    void sameDayReservationsAreChronologicalWithStableTieBreaking() {
        DailyReservationSummaryResult result = service.summarize(List.of(
                reservation(8, 10, 11),
                reservation(6, 10, 9),
                reservation(5, 10, 9)
        ));

        assertEquals(
                List.of(5, 6, 8),
                result.getSummaries().get(0).getReservations().stream()
                        .map(Reservation::getId)
                        .toList()
        );
    }

    @Test
    void emptyReservationsReturnControlledEmptyResult() {
        DailyReservationSummaryResult result =
                service.summarize(List.of());

        assertTrue(result.isEmpty());
        assertEquals(
                DailyReservationSummaryResult.Status.EMPTY,
                result.getStatus()
        );
        assertTrue(result.getSummaries().isEmpty());
        assertEquals("No reservations found.", result.getMessage());
    }

    @Test
    void nullReservationsAreRejectedClearly() {
        assertThrows(
                IllegalArgumentException.class,
                () -> service.summarize(null)
        );
    }

    @Test
    void returnedCollectionsCannotBeModified() {
        DailyReservationSummaryResult result = service.summarize(List.of(
                reservation(1, 10, 9)
        ));

        assertThrows(
                UnsupportedOperationException.class,
                () -> result.getSummaries().clear()
        );
        assertThrows(
                UnsupportedOperationException.class,
                () -> result.getSummaries()
                        .get(0)
                        .getReservations()
                        .clear()
        );
    }

    private Reservation reservation(int id, int day, int startHour) {
        return new Reservation(
                id,
                1,
                "student",
                LocalDate.of(2026, 8, day),
                LocalTime.of(startHour, 0),
                LocalTime.of(startHour + 1, 0)
        );
    }
}

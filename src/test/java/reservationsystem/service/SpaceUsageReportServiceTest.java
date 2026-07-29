package reservationsystem.service;

import org.junit.jupiter.api.Test;
import reservationsystem.model.Reservation;
import reservationsystem.model.Space;
import reservationsystem.model.SpaceUsageReportRow;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SpaceUsageReportServiceTest {

    private final SpaceUsageReportService service =
            new SpaceUsageReportService();

    @Test
    void countsReservationsForEverySpaceInStableNameOrder() {
        SpaceUsageReportResult result = service.createReport(
                List.of(
                        space(3, "Zoom Room"),
                        space(1, "Auditorium"),
                        space(2, "Computer Lab")
                ),
                List.of(
                        reservation(1, 2),
                        reservation(2, 1),
                        reservation(3, 2)
                )
        );

        assertEquals(
                SpaceUsageReportResult.Status.SUCCESS,
                result.getStatus()
        );
        assertEquals(
                List.of("Auditorium", "Computer Lab", "Zoom Room"),
                result.getRows().stream()
                        .map(row -> row.getSpace().getName())
                        .toList()
        );
        assertEquals(
                List.of(1, 2, 0),
                result.getRows().stream()
                        .map(SpaceUsageReportRow::getReservationCount)
                        .toList()
        );
    }

    @Test
    void unknownReservationSpaceIdsAreIgnored() {
        SpaceUsageReportResult result = service.createReport(
                List.of(space(1, "Known Room")),
                List.of(
                        reservation(1, 1),
                        reservation(2, 99),
                        reservation(3, 99)
                )
        );

        assertEquals(1, result.getRows().size());
        assertEquals(1, result.getRows().get(0).getReservationCount());
    }

    @Test
    void emptyReservationsGiveEveryKnownSpaceAZeroCount() {
        SpaceUsageReportResult result = service.createReport(
                List.of(
                        space(2, "Second Room"),
                        space(1, "First Room")
                ),
                List.of()
        );

        assertTrue(result.isSuccessful());
        assertEquals(
                List.of(0, 0),
                result.getRows().stream()
                        .map(SpaceUsageReportRow::getReservationCount)
                        .toList()
        );
    }

    @Test
    void noKnownSpacesReturnsControlledEmptyResult() {
        SpaceUsageReportResult result = service.createReport(
                List.of(),
                List.of(reservation(1, 99))
        );

        assertEquals(
                SpaceUsageReportResult.Status.EMPTY,
                result.getStatus()
        );
        assertTrue(result.getRows().isEmpty());
        assertEquals("No spaces are available.", result.getMessage());
    }

    @Test
    void nullInputsAreRejectedClearly() {
        assertThrows(
                IllegalArgumentException.class,
                () -> service.createReport(null, List.of())
        );
        assertThrows(
                IllegalArgumentException.class,
                () -> service.createReport(List.of(), null)
        );
    }

    private Space space(int id, String name) {
        return new Space(id, name, "Building", 10, List.of());
    }

    private Reservation reservation(int id, int spaceId) {
        return new Reservation(
                id,
                spaceId,
                "student",
                LocalDate.of(2026, 8, 15),
                LocalTime.of(9, 0),
                LocalTime.of(10, 0)
        );
    }
}

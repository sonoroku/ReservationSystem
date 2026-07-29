package reservationsystem.service;

import org.junit.jupiter.api.Test;
import reservationsystem.model.Reservation;
import reservationsystem.model.Space;
import reservationsystem.model.User;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ReservationReportServiceTest {

    @Test
    void adminReceivesAllReservationsWithSpaceAndOwnerInformation() {
        ReservationReportService service = new ReservationReportService();

        User admin = new User("admin001", "password123", true);

        Reservation reservation = new Reservation(
                1,
                1,
                "user001",
                LocalDate.of(2026, 7, 8),
                LocalTime.of(9, 0),
                LocalTime.of(10, 0)
        );

        Space space = new Space(
                1,
                "Nevins Hall Computer Lab",
                "Nevins Hall",
                30,
                List.of("Computer")
        );

        ReservationReportResult result = service.generateAllReservationsReport(
                admin,
                List.of(reservation),
                List.of(space)
        );

        assertTrue(result.isSuccessful());
        assertEquals("Reservations found", result.getMessage());
        assertEquals(1, result.getEntries().size());

        ReservationReportEntry entry = result.getEntries().get(0);

        assertEquals(1, entry.getReservationId());
        assertEquals(1, entry.getSpaceId());
        assertEquals("Nevins Hall Computer Lab", entry.getSpaceName());
        assertEquals("Nevins Hall", entry.getBuilding());
        assertEquals("user001", entry.getUserId());
        assertEquals(LocalDate.of(2026, 7, 8), entry.getDate());
        assertEquals(LocalTime.of(9, 0), entry.getStartTime());
        assertEquals(LocalTime.of(10, 0), entry.getEndTime());
    }

    @Test
    void reportEntriesAreSortedChronologicallyWithDeterministicTieBreaking() {
        ReservationReportService service = new ReservationReportService();

        User admin = new User("admin001", "password123", true);

        Reservation laterReservation = new Reservation(
                2,
                2,
                "user002",
                LocalDate.of(2026, 7, 9),
                LocalTime.of(11, 0),
                LocalTime.of(12, 0)
        );

        Reservation earlierReservation = new Reservation(
                1,
                1,
                "user001",
                LocalDate.of(2026, 7, 8),
                LocalTime.of(9, 0),
                LocalTime.of(10, 0)
        );

        Reservation sameTimeReservation = new Reservation(
                3,
                1,
                "user003",
                LocalDate.of(2026, 7, 8),
                LocalTime.of(9, 0),
                LocalTime.of(10, 0)
        );

        Space spaceOne = new Space(
                1,
                "Nevins Hall Computer Lab",
                "Nevins Hall",
                30,
                List.of("Computer")
        );

        Space spaceTwo = new Space(
                2,
                "Odum Library Study Room",
                "Odum Library",
                8,
                List.of("Whiteboard")
        );

        ReservationReportResult result = service.generateAllReservationsReport(
                admin,
                List.of(laterReservation, sameTimeReservation, earlierReservation),
                List.of(spaceOne, spaceTwo)
        );

        assertTrue(result.isSuccessful());
        assertEquals(3, result.getEntries().size());
        assertEquals(1, result.getEntries().get(0).getReservationId());
        assertEquals(3, result.getEntries().get(1).getReservationId());
        assertEquals(2, result.getEntries().get(2).getReservationId());
    }

    @Test
    void regularUserIsRejected() {
        ReservationReportService service = new ReservationReportService();

        User regularUser = new User("user001", "password123", false);

        ReservationReportResult result = service.generateAllReservationsReport(
                regularUser,
                List.of(),
                List.of()
        );

        assertFalse(result.isSuccessful());
        assertEquals("Only administrators can view all reservations", result.getMessage());
        assertTrue(result.getEntries().isEmpty());
    }

    @Test
    void nullUserIsRejected() {
        ReservationReportService service = new ReservationReportService();

        ReservationReportResult result = service.generateAllReservationsReport(
                null,
                List.of(),
                List.of()
        );

        assertFalse(result.isSuccessful());
        assertEquals("An authenticated administrator is required", result.getMessage());
        assertTrue(result.getEntries().isEmpty());
    }

    @Test
    void emptyReservationListReturnsControlledEmptyResult() {
        ReservationReportService service = new ReservationReportService();

        User admin = new User("admin001", "password123", true);

        ReservationReportResult result = service.generateAllReservationsReport(
                admin,
                List.of(),
                List.of()
        );

        assertTrue(result.isSuccessful());
        assertEquals("No reservations found", result.getMessage());
        assertTrue(result.getEntries().isEmpty());
    }

    @Test
    void missingSpaceDataUsesUnknownSpaceLabels() {
        ReservationReportService service = new ReservationReportService();

        User admin = new User("admin001", "password123", true);

        Reservation reservation = new Reservation(
                1,
                999,
                "user001",
                LocalDate.of(2026, 7, 8),
                LocalTime.of(9, 0),
                LocalTime.of(10, 0)
        );

        ReservationReportResult result = service.generateAllReservationsReport(
                admin,
                List.of(reservation),
                List.of()
        );

        assertTrue(result.isSuccessful());
        assertEquals(1, result.getEntries().size());
        assertEquals("Unknown Space", result.getEntries().get(0).getSpaceName());
        assertEquals("Unknown Building", result.getEntries().get(0).getBuilding());
    }

    @Test
    void nullReservationsThrowsException() {
        ReservationReportService service = new ReservationReportService();

        User admin = new User("admin001", "password123", true);

        assertThrows(
                IllegalArgumentException.class,
                () -> service.generateAllReservationsReport(admin, null, List.of())
        );
    }

    @Test
    void nullSpacesThrowsException() {
        ReservationReportService service = new ReservationReportService();

        User admin = new User("admin001", "password123", true);

        assertThrows(
                IllegalArgumentException.class,
                () -> service.generateAllReservationsReport(admin, List.of(), null)
        );
    }
}

package reservationsystem.service;

import org.junit.jupiter.api.Test;
import reservationsystem.model.Reservation;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MyReservationsServiceTest {

    @Test
    void returnsOnlyReservationsForSelectedUser() {
        MyReservationsService service = new MyReservationsService();

        Reservation reservationOne = new Reservation(
                1,
                1,
                "student",
                LocalDate.of(2026, 7, 8),
                LocalTime.of(9, 0),
                LocalTime.of(10, 0)
        );

        Reservation reservationTwo = new Reservation(
                2,
                2,
                "admin",
                LocalDate.of(2026, 7, 8),
                LocalTime.of(13, 0),
                LocalTime.of(14, 30)
        );

        List<Reservation> result = service.getReservationsForUser(
                "student",
                List.of(reservationOne, reservationTwo)
        );

        assertEquals(1, result.size());
        assertEquals("student", result.get(0).getUserId());
    }

    @Test
    void returnsEmptyListWhenUserHasNoReservations() {
        MyReservationsService service = new MyReservationsService();

        Reservation reservation = new Reservation(
                1,
                1,
                "student",
                LocalDate.of(2026, 7, 8),
                LocalTime.of(9, 0),
                LocalTime.of(10, 0)
        );

        List<Reservation> result = service.getReservationsForUser(
                "admin",
                List.of(reservation)
        );

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void reservationsAreSortedByDateAndStartTime() {
        MyReservationsService service = new MyReservationsService();

        Reservation laterReservation = new Reservation(
                1,
                1,
                "student",
                LocalDate.of(2026, 7, 9),
                LocalTime.of(11, 0),
                LocalTime.of(12, 0)
        );

        Reservation earlierReservation = new Reservation(
                2,
                2,
                "student",
                LocalDate.of(2026, 7, 8),
                LocalTime.of(9, 0),
                LocalTime.of(10, 0)
        );

        List<Reservation> result = service.getReservationsForUser(
                "student",
                List.of(laterReservation, earlierReservation)
        );

        assertEquals(LocalDate.of(2026, 7, 8), result.get(0).getDate());
        assertEquals(LocalTime.of(9, 0), result.get(0).getStartTime());
        assertEquals(LocalDate.of(2026, 7, 9), result.get(1).getDate());
        assertEquals(LocalTime.of(11, 0), result.get(1).getStartTime());
    }

    @Test
    void reservationsWithMatchingDateAndStartTimeAreSortedById() {
        MyReservationsService service = new MyReservationsService();

        Reservation higherId = new Reservation(
                8,
                1,
                "student",
                LocalDate.of(2026, 7, 8),
                LocalTime.of(9, 0),
                LocalTime.of(10, 0)
        );

        Reservation lowerId = new Reservation(
                3,
                2,
                "student",
                LocalDate.of(2026, 7, 8),
                LocalTime.of(9, 0),
                LocalTime.of(10, 0)
        );

        List<Reservation> result = service.getReservationsForUser(
                "student",
                List.of(higherId, lowerId)
        );

        assertEquals(List.of(3, 8), result.stream().map(Reservation::getId).toList());
    }

    @Test
    void blankUserIdThrowsException() {
        MyReservationsService service = new MyReservationsService();

        assertThrows(
                IllegalArgumentException.class,
                () -> service.getReservationsForUser("", List.of())
        );
    }

    @Test
    void nullReservationListThrowsException() {
        MyReservationsService service = new MyReservationsService();

        assertThrows(
                IllegalArgumentException.class,
                () -> service.getReservationsForUser("student", null)
        );
    }
}

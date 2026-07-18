package reservationsystem.integration;

import org.junit.jupiter.api.Test;
import reservationsystem.controller.MyReservationsController;
import reservationsystem.model.Reservation;
import reservationsystem.persistence.ReservationJsonRepository;
import reservationsystem.service.MyReservationsService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class US9MyReservationsIntegrationTest {

    @Test
    void controllerLoadsReservationsForSelectedUserFromJson() {
        // US-9 Acceptance Test:
        // Given reservation data exists in JSON,
        // when the user opens My Reservations,
        // then only that user's reservations are displayed.

        ReservationJsonRepository repository = new ReservationJsonRepository();
        MyReservationsService service = new MyReservationsService();
        MyReservationsController controller = new MyReservationsController(repository, service);

        List<Reservation> reservations = controller.getReservationsForUser("user001");

        assertNotNull(reservations);
        assertFalse(reservations.isEmpty());

        for (Reservation reservation : reservations) {
            assertEquals("user001", reservation.getUserId());
        }
    }

    @Test
    void controllerReturnsEmptyListWhenUserHasNoReservations() {
        // US-9 Acceptance Test:
        // Given a user has no reservations,
        // when the user opens My Reservations,
        // then an empty reservation list is returned.

        ReservationJsonRepository repository = new ReservationJsonRepository();
        MyReservationsService service = new MyReservationsService();
        MyReservationsController controller = new MyReservationsController(repository, service);

        List<Reservation> reservations = controller.getReservationsForUser("user999");

        assertNotNull(reservations);
        assertTrue(reservations.isEmpty());
    }
}

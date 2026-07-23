package reservationsystem.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import reservationsystem.model.Reservation;
import reservationsystem.persistence.ReservationJsonRepository;
import reservationsystem.service.MyReservationsService;
import reservationsystem.service.ReservationCancellationResult;
import reservationsystem.service.ReservationService;

import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ReservationCancellationControllerTest {

    private static final String CURRENT_USER_ID = "student";
	
	@TempDir
    Path tempDirectory;

    @Test
    void cancelReservationRemovesReservationOwnedByCurrentUser() {
        ReservationJsonRepository repository = createRepository("owned-reservation.json");
        repository.saveReservations(List.of(
                reservation(1, "student"),
                reservation(2, "admin")
        ));

        ReservationController controller = createController(repository);

        ReservationCancellationResult result = controller.cancelReservation(1);

        assertTrue(result.isSuccessful());
        assertEquals("Reservation cancelled successfully", result.getMessage());

        List<Reservation> remainingReservations = repository.loadReservations();

        assertEquals(1, remainingReservations.size());
        assertEquals(2, remainingReservations.get(0).getId());
    }

    @Test
    void cancelReservationReturnsNotFoundWhenReservationDoesNotExist() {
        ReservationJsonRepository repository = createRepository("missing-reservation.json");
        repository.saveReservations(List.of(
                reservation(1, "student")
        ));

        ReservationController controller = createController(repository);

        ReservationCancellationResult result = controller.cancelReservation(99);

        assertFalse(result.isSuccessful());
        assertEquals("Reservation was not found", result.getMessage());

        List<Reservation> remainingReservations = repository.loadReservations();

        assertEquals(1, remainingReservations.size());
        assertEquals(1, remainingReservations.get(0).getId());
    }

    @Test
    void cancelReservationReturnsNotOwnedWhenReservationBelongsToAnotherUser() {
        ReservationJsonRepository repository = createRepository("not-owned-reservation.json");
        repository.saveReservations(List.of(
                reservation(1, "admin"),
                reservation(2, "student")
        ));

        ReservationController controller = createController(repository);

        ReservationCancellationResult result = controller.cancelReservation(1);

        assertFalse(result.isSuccessful());
        assertEquals("Reservation does not belong to current user", result.getMessage());

        List<Reservation> remainingReservations = repository.loadReservations();

        assertEquals(List.of(1, 2), remainingReservations.stream().map(Reservation::getId).toList());
    }

    private ReservationController createController(ReservationJsonRepository repository) {
        return new ReservationController(
                repository,
                new ReservationService(),
                () -> CURRENT_USER_ID,
                new MyReservationsService(),
                new SpaceController()
        );
    }

    private ReservationJsonRepository createRepository(String fileName) {
        return new ReservationJsonRepository(tempDirectory.resolve(fileName));
    }

    private Reservation reservation(int id, String userId) {
        return new Reservation(
                id,
                1,
                userId,
                LocalDate.of(2026, 7, 8),
                LocalTime.of(9, 0),
                LocalTime.of(10, 0)
        );
    }

}

package reservationsystem.integration;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import reservationsystem.controller.ReservationController;
import reservationsystem.controller.SpaceController;
import reservationsystem.model.Reservation;
import reservationsystem.persistence.ReservationJsonRepository;
import reservationsystem.service.CurrentUserProvider;
import reservationsystem.service.MyReservationsService;
import reservationsystem.service.ReservationCancellationResult;
import reservationsystem.service.ReservationModificationResult;
import reservationsystem.service.ReservationService;

import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class US16ReservationOwnershipIntegrationTest {

    private static final LocalDate RESERVATION_DATE = LocalDate.of(2026, 7, 8);

    @TempDir
    Path tempDirectory;

    @Test
    void currentSessionUserDeterminesWhoCanModifyAReservation() {
        ReservationJsonRepository repository = createRepository("modify-ownership.json");
        repository.saveReservations(List.of(
                reservation(1, "student", 9, 10),
                reservation(2, "admin", 13, 14)
        ));
        MutableCurrentUserProvider currentUser = new MutableCurrentUserProvider("student");
        ReservationController controller = createController(repository, currentUser);

        ReservationModificationResult ownerResult = controller.modifyReservation(
                1,
                1,
                RESERVATION_DATE,
                LocalTime.of(10, 0),
                LocalTime.of(11, 0)
        );

        assertTrue(ownerResult.isSuccessful());
        assertEquals(LocalTime.of(10, 0), findReservation(repository, 1).getStartTime());

        currentUser.setCurrentUserId("admin");
        ReservationModificationResult nonOwnerResult = controller.modifyReservation(
                1,
                1,
                RESERVATION_DATE,
                LocalTime.of(11, 0),
                LocalTime.of(12, 0)
        );

        assertFalse(nonOwnerResult.isSuccessful());
        assertEquals("Reservation does not belong to current user", nonOwnerResult.getMessage());
        assertEquals(LocalTime.of(10, 0), findReservation(repository, 1).getStartTime());
        assertEquals(LocalTime.of(11, 0), findReservation(repository, 1).getEndTime());
    }

    @Test
    void currentSessionUserDeterminesWhoCanCancelAReservation() {
        ReservationJsonRepository repository = createRepository("cancel-ownership.json");
        repository.saveReservations(List.of(
                reservation(1, "student", 9, 10),
                reservation(2, "admin", 13, 14),
                reservation(3, "admin", 15, 16)
        ));
        MutableCurrentUserProvider currentUser = new MutableCurrentUserProvider("admin");
        ReservationController controller = createController(repository, currentUser);

        ReservationCancellationResult ownerResult = controller.cancelReservation(2);

        assertTrue(ownerResult.isSuccessful());
        assertEquals(List.of(1, 3), reservationIds(repository));

        currentUser.setCurrentUserId("student");
        ReservationCancellationResult nonOwnerResult = controller.cancelReservation(3);

        assertFalse(nonOwnerResult.isSuccessful());
        assertEquals("Reservation does not belong to current user", nonOwnerResult.getMessage());
        assertEquals(List.of(1, 3), reservationIds(repository));
    }

    private ReservationController createController(
            ReservationJsonRepository repository,
            CurrentUserProvider currentUserProvider
    ) {
        return new ReservationController(
                repository,
                new ReservationService(),
                currentUserProvider,
                new MyReservationsService(),
                new SpaceController()
        );
    }

    private ReservationJsonRepository createRepository(String fileName) {
        return new ReservationJsonRepository(tempDirectory.resolve(fileName));
    }

    private Reservation reservation(int id, String userId, int startHour, int endHour) {
        return new Reservation(
                id,
                1,
                userId,
                RESERVATION_DATE,
                LocalTime.of(startHour, 0),
                LocalTime.of(endHour, 0)
        );
    }

    private Reservation findReservation(ReservationJsonRepository repository, int reservationId) {
        return repository.loadReservations().stream()
                .filter(reservation -> reservation.getId() == reservationId)
                .findFirst()
                .orElseThrow();
    }

    private List<Integer> reservationIds(ReservationJsonRepository repository) {
        return repository.loadReservations().stream()
                .map(Reservation::getId)
                .toList();
    }

    private static class MutableCurrentUserProvider implements CurrentUserProvider {

        private String currentUserId;

        private MutableCurrentUserProvider(String currentUserId) {
            this.currentUserId = currentUserId;
        }

        @Override
        public String getCurrentUserId() {
            return currentUserId;
        }

        private void setCurrentUserId(String currentUserId) {
            this.currentUserId = currentUserId;
        }
    }
}

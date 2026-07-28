package reservationsystem.controller;

import org.junit.jupiter.api.Test;
import reservationsystem.model.Reservation;
import reservationsystem.model.User;
import reservationsystem.persistence.ReservationJsonRepository;
import reservationsystem.persistence.UserJsonRepository;
import reservationsystem.service.AdminReservationCancellationResult;
import reservationsystem.service.AuthenticatedUserProvider;
import reservationsystem.service.AuthorizationService;
import reservationsystem.service.ReservationService;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AdminReservationCancellationControllerTest {

    @Test
    void administratorCanCancelOwnReservation() {
        FakeReservationJsonRepository repository = repositoryWith(
                reservation(1, "admin"),
                reservation(2, "student")
        );

        AdminReservationCancellationResult result = controller(
                new User("admin", "admin123", true),
                repository
        ).cancelReservation(1);

        assertTrue(result.isSuccessful());
        assertEquals(
                AdminReservationCancellationResult.Status.SUCCESS,
                result.getStatus()
        );
        assertEquals(List.of(2), savedReservationIds(repository));
        assertEquals(1, repository.getSaveCount());
    }

    @Test
    void administratorCanCancelAnotherUsersReservation() {
        FakeReservationJsonRepository repository = repositoryWith(
                reservation(1, "admin"),
                reservation(2, "student")
        );

        AdminReservationCancellationResult result = controller(
                new User("admin", "admin123", true),
                repository
        ).cancelReservation(2);

        assertTrue(result.isSuccessful());
        assertEquals(List.of(1), savedReservationIds(repository));
        assertEquals(1, repository.getSaveCount());
    }

    @Test
    void regularUserCannotUseAdminCancellation() {
        FakeReservationJsonRepository repository = repositoryWith(
                reservation(1, "student"),
                reservation(2, "admin")
        );

        AdminReservationCancellationResult result = controller(
                new User("student", "student123", false),
                repository
        ).cancelReservation(1);

        assertFalse(result.isSuccessful());
        assertEquals(
                AdminReservationCancellationResult.Status.UNAUTHORIZED,
                result.getStatus()
        );
        assertEquals("Administrator access is required", result.getMessage());
        assertEquals(List.of(1, 2), originalReservationIds(repository));
        assertEquals(0, repository.getSaveCount());
    }

    @Test
    void missingReservationDoesNotChangeStorage() {
        FakeReservationJsonRepository repository = repositoryWith(
                reservation(1, "admin"),
                reservation(2, "student")
        );

        AdminReservationCancellationResult result = controller(
                new User("admin", "admin123", true),
                repository
        ).cancelReservation(99);

        assertFalse(result.isSuccessful());
        assertEquals(
                AdminReservationCancellationResult.Status.NOT_FOUND,
                result.getStatus()
        );
        assertEquals("Reservation was not found", result.getMessage());
        assertEquals(List.of(1, 2), originalReservationIds(repository));
        assertEquals(0, repository.getSaveCount());
    }

    private AdminReservationController controller(
            User currentUser,
            ReservationJsonRepository reservationRepository
    ) {
        AuthenticatedUserProvider currentUserProvider = () -> currentUser;

        return new AdminReservationController(
                reservationRepository,
                new FakeUserJsonRepository(),
                new ReservationService(),
                new AuthorizationService(currentUserProvider)
        );
    }

    private FakeReservationJsonRepository repositoryWith(
            Reservation... reservations
    ) {
        return new FakeReservationJsonRepository(List.of(reservations));
    }

    private List<Integer> savedReservationIds(
            FakeReservationJsonRepository repository
    ) {
        return repository.getSavedReservations().stream()
                .map(Reservation::getId)
                .toList();
    }

    private List<Integer> originalReservationIds(
            FakeReservationJsonRepository repository
    ) {
        return repository.getOriginalReservations().stream()
                .map(Reservation::getId)
                .toList();
    }

    private Reservation reservation(int id, String userId) {
        return new Reservation(
                id,
                1,
                userId,
                LocalDate.of(2026, 8, 4),
                LocalTime.of(9, 0),
                LocalTime.of(10, 0)
        );
    }

    private static class FakeUserJsonRepository
            extends UserJsonRepository {

        @Override
        public List<User> loadUsers() {
            return List.of();
        }
    }

    private static class FakeReservationJsonRepository
            extends ReservationJsonRepository {

        private final List<Reservation> originalReservations;
        private List<Reservation> savedReservations = new ArrayList<>();
        private int saveCount;

        FakeReservationJsonRepository(List<Reservation> reservations) {
            originalReservations = new ArrayList<>(reservations);
        }

        @Override
        public List<Reservation> loadReservations() {
            return new ArrayList<>(originalReservations);
        }

        @Override
        public void saveReservations(List<Reservation> reservations) {
            saveCount++;
            savedReservations = new ArrayList<>(reservations);
        }

        List<Reservation> getOriginalReservations() {
            return originalReservations;
        }

        List<Reservation> getSavedReservations() {
            return savedReservations;
        }

        int getSaveCount() {
            return saveCount;
        }
    }
}

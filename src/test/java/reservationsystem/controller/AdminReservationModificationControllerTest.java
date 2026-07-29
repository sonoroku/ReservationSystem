package reservationsystem.controller;

import org.junit.jupiter.api.Test;
import reservationsystem.model.Reservation;
import reservationsystem.model.User;
import reservationsystem.persistence.ReservationJsonRepository;
import reservationsystem.persistence.UserJsonRepository;
import reservationsystem.service.AdminReservationModificationResult;
import reservationsystem.service.AuthenticatedUserProvider;
import reservationsystem.service.AuthorizationService;
import reservationsystem.service.ReservationService;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AdminReservationModificationControllerTest {
	
	private static final LocalDate TEST_DATE =
            LocalDate.of(2026, 8, 10);

    @Test
    void administratorCanModifyOwnReservation() {
        FakeReservationJsonRepository repository = repositoryWith(
                reservation(1, "admin", 9, 10),
                reservation(2, "student", 13, 14)
        );

        AdminReservationModificationResult result = controller(
                new User("admin", "admin123", true),
                repository
        ).modifyReservation(
                1,
                2,
                TEST_DATE.plusDays(1),
                LocalTime.of(10, 0),
                LocalTime.of(11, 0)
        );

        assertTrue(result.isSuccessful());
        assertEquals(
                AdminReservationModificationResult.Status.SUCCESS,
                result.getStatus()
        );
        assertEquals(1, repository.getSaveCount());

        Reservation updatedReservation =
                result.getUpdatedReservation();

        assertNotNull(updatedReservation);
        assertEquals(1, updatedReservation.getId());
        assertEquals("admin", updatedReservation.getUserId());
        assertEquals(2, updatedReservation.getSpaceId());
        assertEquals(
                TEST_DATE.plusDays(1),
                updatedReservation.getDate()
        );
        assertEquals(
                LocalTime.of(10, 0),
                updatedReservation.getStartTime()
        );
        assertEquals(
                LocalTime.of(11, 0),
                updatedReservation.getEndTime()
        );
    }

    @Test
    void administratorCanModifyAnotherUsersReservation() {
        FakeReservationJsonRepository repository = repositoryWith(
                reservation(1, "admin", 9, 10),
                reservation(2, "student", 13, 14)
        );

        AdminReservationModificationResult result = controller(
                new User("admin", "admin123", true),
                repository
        ).modifyReservation(
                2,
                2,
                TEST_DATE.plusDays(1),
                LocalTime.of(15, 0),
                LocalTime.of(16, 0)
        );

        assertTrue(result.isSuccessful());
        assertEquals(1, repository.getSaveCount());

        Reservation updatedReservation =
                result.getUpdatedReservation();

        assertEquals(2, updatedReservation.getId());
        assertEquals(
                "student",
                updatedReservation.getUserId()
        );

        List<Reservation> savedReservations =
                repository.getSavedReservations();

        assertEquals(2, savedReservations.size());
        assertEquals(1, savedReservations.get(0).getId());
        assertEquals(
                LocalTime.of(9, 0),
                savedReservations.get(0).getStartTime()
        );
    }

    @Test
    void unchangedReservationDoesNotConflictWithItself() {
        FakeReservationJsonRepository repository = repositoryWith(
                reservation(1, "student", 9, 10)
        );

        AdminReservationModificationResult result = controller(
                new User("admin", "admin123", true),
                repository
        ).modifyReservation(
                1,
                1,
                TEST_DATE,
                LocalTime.of(9, 0),
                LocalTime.of(10, 0)
        );

        assertTrue(result.isSuccessful());
        assertEquals(1, repository.getSaveCount());
    }

    @Test
    void regularUserCannotUseAdminModification() {
        FakeReservationJsonRepository repository = repositoryWith(
                reservation(1, "student", 9, 10)
        );

        AdminReservationModificationResult result = controller(
                new User("student", "student123", false),
                repository
        ).modifyReservation(
                1,
                1,
                TEST_DATE,
                LocalTime.of(10, 0),
                LocalTime.of(11, 0)
        );

        assertFalse(result.isSuccessful());
        assertEquals(
                AdminReservationModificationResult.Status.UNAUTHORIZED,
                result.getStatus()
        );
        assertEquals(
                "Administrator access is required",
                result.getMessage()
        );
        assertEquals(0, repository.getSaveCount());
        assertOriginalReservationUnchanged(repository);
    }

    @Test
    void missingReservationIsRejectedWithoutSaving() {
        FakeReservationJsonRepository repository = repositoryWith(
                reservation(1, "student", 9, 10)
        );

        AdminReservationModificationResult result = controller(
                new User("admin", "admin123", true),
                repository
        ).modifyReservation(
                99,
                1,
                TEST_DATE,
                LocalTime.of(10, 0),
                LocalTime.of(11, 0)
        );

        assertEquals(
                AdminReservationModificationResult.Status.NOT_FOUND,
                result.getStatus()
        );
        assertEquals(0, repository.getSaveCount());
        assertOriginalReservationUnchanged(repository);
    }

    @Test
    void conflictingModificationIsRejectedAtomically() {
        FakeReservationJsonRepository repository = repositoryWith(
                reservation(1, "student", 9, 10),
                reservation(2, "admin", 11, 12)
        );

        AdminReservationModificationResult result = controller(
                new User("admin", "admin123", true),
                repository
        ).modifyReservation(
                1,
                1,
                TEST_DATE,
                LocalTime.of(10, 30),
                LocalTime.of(11, 30)
        );

        assertEquals(
                AdminReservationModificationResult.Status.VALIDATION_FAILED,
                result.getStatus()
        );
        assertEquals(
                "Reservation conflicts with an existing reservation",
                result.getMessage()
        );
        assertEquals(0, repository.getSaveCount());
        assertOriginalReservationUnchanged(repository);
    }

    @Test
    void reversedTimesAreRejectedWithoutSaving() {
        FakeReservationJsonRepository repository = repositoryWith(
                reservation(1, "student", 9, 10)
        );

        AdminReservationModificationResult result = controller(
                new User("admin", "admin123", true),
                repository
        ).modifyReservation(
                1,
                1,
                TEST_DATE,
                LocalTime.of(12, 0),
                LocalTime.of(11, 0)
        );

        assertEquals(
                AdminReservationModificationResult.Status.VALIDATION_FAILED,
                result.getStatus()
        );
        assertEquals(
                "End time must be after start time",
                result.getMessage()
        );
        assertEquals(0, repository.getSaveCount());
    }

    @Test
    void durationLongerThanTwoHoursIsRejectedWithoutSaving() {
        FakeReservationJsonRepository repository = repositoryWith(
                reservation(1, "student", 9, 10)
        );

        AdminReservationModificationResult result = controller(
                new User("admin", "admin123", true),
                repository
        ).modifyReservation(
                1,
                1,
                TEST_DATE,
                LocalTime.of(8, 0),
                LocalTime.of(10, 1)
        );

        assertEquals(
                AdminReservationModificationResult.Status.VALIDATION_FAILED,
                result.getStatus()
        );
        assertEquals(0, repository.getSaveCount());
    }

    @Test
    void modificationInsideBufferIsRejectedWithoutSaving() {
        FakeReservationJsonRepository repository = repositoryWith(
                reservation(1, "student", 9, 10),
                reservation(2, "admin", 11, 12)
        );

        AdminReservationModificationResult result = controller(
                new User("admin", "admin123", true),
                repository
        ).modifyReservation(
                1,
                1,
                TEST_DATE,
                LocalTime.of(9, 30),
                LocalTime.of(10, 55)
        );

        assertEquals(
                AdminReservationModificationResult.Status.VALIDATION_FAILED,
                result.getStatus()
        );
        assertEquals(
                "Reservation must be at least 10 minutes away from another reservation",
                result.getMessage()
        );
        assertEquals(0, repository.getSaveCount());
    }

    @Test
    void missingDateIsRejectedWithoutSaving() {
        FakeReservationJsonRepository repository = repositoryWith(
                reservation(1, "student", 9, 10)
        );

        AdminReservationModificationResult result = controller(
                new User("admin", "admin123", true),
                repository
        ).modifyReservation(
                1,
                1,
                null,
                LocalTime.of(10, 0),
                LocalTime.of(11, 0)
        );

        assertEquals(
                AdminReservationModificationResult.Status.VALIDATION_FAILED,
                result.getStatus()
        );
        assertEquals(
                "Reservation date is required",
                result.getMessage()
        );
        assertEquals(0, repository.getSaveCount());
    }

    private AdminReservationController controller(
            User currentUser,
            ReservationJsonRepository reservationRepository
    ) {
        AuthenticatedUserProvider currentUserProvider =
                () -> currentUser;

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
        return new FakeReservationJsonRepository(
                List.of(reservations)
        );
    }

    private Reservation reservation(
            int id,
            String userId,
            int startHour,
            int endHour
    ) {
        return new Reservation(
                id,
                1,
                userId,
                TEST_DATE,
                LocalTime.of(startHour, 0),
                LocalTime.of(endHour, 0)
        );
    }

    private void assertOriginalReservationUnchanged(
            FakeReservationJsonRepository repository
    ) {
        Reservation original =
                repository.getOriginalReservations().get(0);

        assertEquals(1, original.getId());
        assertEquals("student", original.getUserId());
        assertEquals(LocalTime.of(9, 0), original.getStartTime());
        assertEquals(LocalTime.of(10, 0), original.getEndTime());
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
        private List<Reservation> savedReservations;
        private int saveCount;

        FakeReservationJsonRepository(
                List<Reservation> reservations
        ) {
            originalReservations =
                    new ArrayList<>(reservations);
            savedReservations = new ArrayList<>();
        }

        @Override
        public List<Reservation> loadReservations() {
            return new ArrayList<>(originalReservations);
        }

        @Override
        public void saveReservations(
                List<Reservation> reservations
        ) {
            saveCount++;
            savedReservations =
                    new ArrayList<>(reservations);
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

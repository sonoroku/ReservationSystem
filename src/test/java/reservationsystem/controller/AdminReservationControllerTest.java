package reservationsystem.controller;

import org.junit.jupiter.api.Test;
import reservationsystem.model.Reservation;
import reservationsystem.model.User;
import reservationsystem.persistence.ReservationJsonRepository;
import reservationsystem.persistence.UserJsonRepository;
import reservationsystem.service.AdminReservationCreationResult;
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
import static org.junit.jupiter.api.Assertions.assertThrows;

public class AdminReservationControllerTest {

	private static final LocalDate TEST_DATE =
            LocalDate.of(2026, 8, 1);

    @Test
    void administratorCreatesReservationForExistingUser() {
        FakeReservationJsonRepository reservationRepository =
                new FakeReservationJsonRepository(List.of(
                        reservation(
                                1,
                                "admin",
                                LocalTime.of(10, 0),
                                LocalTime.of(11, 0)
                        )
                ));

        AdminReservationController controller = controller(
                new User("admin", "admin123", true),
                reservationRepository
        );

        AdminReservationCreationResult result =
                controller.createReservationForUser(
                        "StUdEnT",
                        1,
                        TEST_DATE,
                        LocalTime.of(12, 0),
                        LocalTime.of(13, 0)
                );

        assertTrue(result.isSuccessful());
        assertEquals(
                AdminReservationCreationResult.Status.SUCCESS,
                result.getStatus()
        );
        assertNotNull(result.getCreatedReservation());
        assertEquals(
                "student",
                result.getCreatedReservation().getUserId()
        );
        assertEquals(2, result.getCreatedReservation().getId());
        assertEquals(1, reservationRepository.getSaveCount());
        assertEquals(2, reservationRepository.getSavedReservations().size());
    }

    @Test
    void regularUserCannotCreateReservationForAnotherUser() {
        FakeReservationJsonRepository reservationRepository =
                new FakeReservationJsonRepository(List.of());

        AdminReservationController controller = controller(
                new User("student", "student123", false),
                reservationRepository
        );

        AdminReservationCreationResult result =
                controller.createReservationForUser(
                        "admin",
                        1,
                        TEST_DATE,
                        LocalTime.of(12, 0),
                        LocalTime.of(13, 0)
                );

        assertFalse(result.isSuccessful());
        assertEquals(
                AdminReservationCreationResult.Status.UNAUTHORIZED,
                result.getStatus()
        );
        assertEquals(
                "Administrator access is required",
                result.getMessage()
        );
        assertEquals(0, reservationRepository.getSaveCount());
    }

    @Test
    void missingTargetUserIsRejectedWithoutSaving() {
        FakeReservationJsonRepository reservationRepository =
                new FakeReservationJsonRepository(List.of());

        AdminReservationController controller = controller(
                new User("admin", "admin123", true),
                reservationRepository
        );

        AdminReservationCreationResult result =
                controller.createReservationForUser(
                        "missing-user",
                        1,
                        TEST_DATE,
                        LocalTime.of(12, 0),
                        LocalTime.of(13, 0)
                );

        assertEquals(
                AdminReservationCreationResult.Status.INVALID_USER,
                result.getStatus()
        );
        assertEquals(0, reservationRepository.getSaveCount());
    }

    @Test
    void conflictingReservationIsRejectedWithoutSaving() {
        FakeReservationJsonRepository reservationRepository =
                repositoryWithExistingReservation();

        AdminReservationController controller = controller(
                new User("admin", "admin123", true),
                reservationRepository
        );

        AdminReservationCreationResult result =
                controller.createReservationForUser(
                        "student",
                        1,
                        TEST_DATE,
                        LocalTime.of(10, 30),
                        LocalTime.of(11, 30)
                );

        assertEquals(
                AdminReservationCreationResult.Status.VALIDATION_FAILED,
                result.getStatus()
        );
        assertEquals(
                "Reservation conflicts with an existing reservation",
                result.getMessage()
        );
        assertEquals(0, reservationRepository.getSaveCount());
        assertEquals(1, reservationRepository.getOriginalReservations().size());
    }

    @Test
    void reversedTimesAreRejectedWithoutSaving() {
        FakeReservationJsonRepository reservationRepository =
                new FakeReservationJsonRepository(List.of());

        AdminReservationController controller = controller(
                new User("admin", "admin123", true),
                reservationRepository
        );

        AdminReservationCreationResult result =
                controller.createReservationForUser(
                        "student",
                        1,
                        TEST_DATE,
                        LocalTime.of(13, 0),
                        LocalTime.of(12, 0)
                );

        assertEquals(
                AdminReservationCreationResult.Status.VALIDATION_FAILED,
                result.getStatus()
        );
        assertEquals(
                "End time must be after start time",
                result.getMessage()
        );
        assertEquals(0, reservationRepository.getSaveCount());
    }

    @Test
    void missingDateIsRejectedWithoutSaving() {
        FakeReservationJsonRepository reservationRepository =
                new FakeReservationJsonRepository(List.of());

        AdminReservationController controller = controller(
                new User("admin", "admin123", true),
                reservationRepository
        );

        AdminReservationCreationResult result =
                controller.createReservationForUser(
                        "student",
                        1,
                        null,
                        LocalTime.of(12, 0),
                        LocalTime.of(13, 0)
                );

        assertEquals(
                AdminReservationCreationResult.Status.VALIDATION_FAILED,
                result.getStatus()
        );
        assertEquals("Reservation date is required", result.getMessage());
        assertEquals(0, reservationRepository.getSaveCount());
    }

    @Test
    void reservationLongerThanTwoHoursIsRejectedWithoutSaving() {
        FakeReservationJsonRepository reservationRepository =
                new FakeReservationJsonRepository(List.of());

        AdminReservationController controller = controller(
                new User("admin", "admin123", true),
                reservationRepository
        );

        AdminReservationCreationResult result =
                controller.createReservationForUser(
                        "student",
                        1,
                        TEST_DATE,
                        LocalTime.of(9, 0),
                        LocalTime.of(11, 1)
                );

        assertEquals(
                AdminReservationCreationResult.Status.VALIDATION_FAILED,
                result.getStatus()
        );
        assertEquals(0, reservationRepository.getSaveCount());
    }

    @Test
    void reservationInsideTenMinuteBufferIsRejectedWithoutSaving() {
        FakeReservationJsonRepository reservationRepository =
                repositoryWithExistingReservation();

        AdminReservationController controller = controller(
                new User("admin", "admin123", true),
                reservationRepository
        );

        AdminReservationCreationResult result =
                controller.createReservationForUser(
                        "student",
                        1,
                        TEST_DATE,
                        LocalTime.of(11, 5),
                        LocalTime.of(12, 0)
                );

        assertEquals(
                AdminReservationCreationResult.Status.VALIDATION_FAILED,
                result.getStatus()
        );
        assertEquals(
                "Reservation must be at least 10 minutes away from another reservation",
                result.getMessage()
        );
        assertEquals(0, reservationRepository.getSaveCount());
    }

    @Test
    void administratorReceivesSortedUserIdsForSelection() {
        AdminReservationController controller = controller(
                new User("admin", "admin123", true),
                new FakeReservationJsonRepository(List.of())
        );

        List<String> userIds =
                controller.getAvailableUserIds();

        assertEquals(
                List.of("admin", "student"),
                userIds
        );
    }

    @Test
    void regularUserCannotLoadAdminUserSelector() {
        AdminReservationController controller = controller(
                new User("student", "student123", false),
                new FakeReservationJsonRepository(List.of())
        );

        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                controller::getAvailableUserIds
        );

        assertEquals(
                "Administrator access is required",
                exception.getMessage()
        );
    }

    private AdminReservationController controller(
            User currentUser,
            FakeReservationJsonRepository reservationRepository
    ) {
        FakeUserJsonRepository userRepository =
                new FakeUserJsonRepository(List.of(
                        new User("student", "student123", false),
                        new User("admin", "admin123", true)
                ));

        AuthenticatedUserProvider currentUserProvider =
                () -> currentUser;

        return new AdminReservationController(
                reservationRepository,
                userRepository,
                new ReservationService(),
                new AuthorizationService(currentUserProvider)
        );
    }

    private FakeReservationJsonRepository
            repositoryWithExistingReservation() {
        return new FakeReservationJsonRepository(List.of(
                reservation(
                        1,
                        "admin",
                        LocalTime.of(10, 0),
                        LocalTime.of(11, 0)
                )
        ));
    }

    private Reservation reservation(
            int id,
            String userId,
            LocalTime startTime,
            LocalTime endTime
    ) {
        return new Reservation(
                id,
                1,
                userId,
                TEST_DATE,
                startTime,
                endTime
        );
    }

    private static class FakeUserJsonRepository
            extends UserJsonRepository {

        private final List<User> users;

        FakeUserJsonRepository(List<User> users) {
            this.users = users;
        }

        @Override
        public List<User> loadUsers() {
            return users;
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
            originalReservations = new ArrayList<>(reservations);
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

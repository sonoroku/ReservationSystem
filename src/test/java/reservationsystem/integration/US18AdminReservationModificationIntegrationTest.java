package reservationsystem.integration;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import reservationsystem.controller.AdminReservationController;
import reservationsystem.controller.AvailabilityController;
import reservationsystem.model.Reservation;
import reservationsystem.model.TimeSlot;
import reservationsystem.model.User;
import reservationsystem.persistence.ReservationJsonRepository;
import reservationsystem.persistence.UserJsonRepository;
import reservationsystem.service.AdminReservationModificationResult;
import reservationsystem.service.AuthenticatedUserProvider;
import reservationsystem.service.AuthorizationService;
import reservationsystem.service.AvailabilityService;
import reservationsystem.service.ReservationService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class US18AdminReservationModificationIntegrationTest {
	
	private static final LocalDate TEST_DATE =
            LocalDate.of(2026, 8, 12);

    @TempDir
    Path temporaryDirectory;

    @Test
    void administratorModificationPersistsAndMovesAvailability() {
        Path runtimeFile =
                temporaryDirectory.resolve("reservations.json");

        ReservationJsonRepository repository =
                new ReservationJsonRepository(runtimeFile);

        repository.saveReservations(List.of(
                reservation(1, 1, "student", 9, 10),
                reservation(2, 2, "admin", 15, 16)
        ));

        AdminReservationModificationResult result = controller(
                new User("admin", "admin123", true),
                repository
        ).modifyReservation(
                1,
                1,
                TEST_DATE,
                LocalTime.of(13, 0),
                LocalTime.of(14, 0)
        );

        assertTrue(result.isSuccessful());

        ReservationJsonRepository reloadedRepository =
                new ReservationJsonRepository(runtimeFile);

        List<Reservation> reloadedReservations =
                reloadedRepository.loadReservations();

        assertEquals(2, reloadedReservations.size());

        Reservation modifiedReservation =
                reloadedReservations.stream()
                        .filter(reservation ->
                                reservation.getId() == 1)
                        .findFirst()
                        .orElseThrow();

        assertEquals(1, modifiedReservation.getId());
        assertEquals(
                "student",
                modifiedReservation.getUserId()
        );
        assertEquals(1, modifiedReservation.getSpaceId());
        assertEquals(TEST_DATE, modifiedReservation.getDate());
        assertEquals(
                LocalTime.of(13, 0),
                modifiedReservation.getStartTime()
        );
        assertEquals(
                LocalTime.of(14, 0),
                modifiedReservation.getEndTime()
        );

        Reservation unrelatedReservation =
                reloadedReservations.stream()
                        .filter(reservation ->
                                reservation.getId() == 2)
                        .findFirst()
                        .orElseThrow();

        assertEquals("admin", unrelatedReservation.getUserId());
        assertEquals(2, unrelatedReservation.getSpaceId());
        assertEquals(
                LocalTime.of(15, 0),
                unrelatedReservation.getStartTime()
        );
        assertEquals(
                LocalTime.of(16, 0),
                unrelatedReservation.getEndTime()
        );

        AvailabilityController availabilityController =
                new AvailabilityController(
                        new AvailabilityService(),
                        reloadedRepository
                );

        List<TimeSlot> timeSlots =
                availabilityController.getAvailabilityForDay(
                        1,
                        TEST_DATE
                );

        assertTrue(findSlot(timeSlots, 9).isAvailable());
        assertTrue(findSlot(timeSlots, 13).isReserved());
    }

    @Test
    void conflictingModificationLeavesPersistedDataUnchanged()
            throws IOException {

        Path runtimeFile =
                temporaryDirectory.resolve(
                        "conflict-reservations.json"
                );

        ReservationJsonRepository repository =
                new ReservationJsonRepository(runtimeFile);

        repository.saveReservations(List.of(
                reservation(1, 1, "student", 9, 10),
                reservation(2, 1, "admin", 11, 12)
        ));

        String contentsBeforeAttempt =
                Files.readString(runtimeFile);

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

        assertFalse(result.isSuccessful());
        assertEquals(
                AdminReservationModificationResult.Status.VALIDATION_FAILED,
                result.getStatus()
        );
        assertEquals(
                contentsBeforeAttempt,
                Files.readString(runtimeFile)
        );

        List<Reservation> reloadedReservations =
                new ReservationJsonRepository(runtimeFile)
                        .loadReservations();

        assertEquals(2, reloadedReservations.size());
        assertEquals(
                LocalTime.of(9, 0),
                reloadedReservations.get(0).getStartTime()
        );
        assertEquals(
                LocalTime.of(10, 0),
                reloadedReservations.get(0).getEndTime()
        );
    }

    @Test
    void regularUserDenialLeavesPersistedDataUnchanged()
            throws IOException {

        Path runtimeFile =
                temporaryDirectory.resolve(
                        "unauthorized-reservations.json"
                );

        ReservationJsonRepository repository =
                new ReservationJsonRepository(runtimeFile);

        repository.saveReservations(List.of(
                reservation(1, 1, "student", 9, 10)
        ));

        String contentsBeforeAttempt =
                Files.readString(runtimeFile);

        AdminReservationModificationResult result = controller(
                new User("student", "student123", false),
                repository
        ).modifyReservation(
                1,
                1,
                TEST_DATE,
                LocalTime.of(13, 0),
                LocalTime.of(14, 0)
        );

        assertFalse(result.isSuccessful());
        assertEquals(
                AdminReservationModificationResult.Status.UNAUTHORIZED,
                result.getStatus()
        );
        assertEquals(
                contentsBeforeAttempt,
                Files.readString(runtimeFile)
        );
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

    private Reservation reservation(
            int id,
            int spaceId,
            String userId,
            int startHour,
            int endHour
    ) {
        return new Reservation(
                id,
                spaceId,
                userId,
                TEST_DATE,
                LocalTime.of(startHour, 0),
                LocalTime.of(endHour, 0)
        );
    }

    private TimeSlot findSlot(
            List<TimeSlot> timeSlots,
            int startHour
    ) {
        return timeSlots.stream()
                .filter(timeSlot ->
                        timeSlot.getStartTime().equals(
                                LocalTime.of(startHour, 0)
                        ))
                .findFirst()
                .orElseThrow();
    }

    private static class FakeUserJsonRepository
            extends UserJsonRepository {

        @Override
        public List<User> loadUsers() {
            return List.of();
        }
    }

}

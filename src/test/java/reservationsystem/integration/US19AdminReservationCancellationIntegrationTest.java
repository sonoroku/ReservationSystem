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
import reservationsystem.service.AdminReservationCancellationResult;
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

class US19AdminReservationCancellationIntegrationTest {

    private static final LocalDate TEST_DATE =
            LocalDate.of(2026, 8, 5);

    @TempDir
    Path temporaryDirectory;

    @Test
    void administratorCancellationPersistsAndReleasesAvailability() {
        Path runtimeFile = temporaryDirectory.resolve("reservations.json");
        ReservationJsonRepository repository =
                new ReservationJsonRepository(runtimeFile);

        repository.saveReservations(List.of(
                reservation(1, 1, "student", 9, 10),
                reservation(2, 1, "admin", 13, 14)
        ));

        AdminReservationCancellationResult result = controller(
                new User("admin", "admin123", true),
                repository
        ).cancelReservation(1);

        assertTrue(result.isSuccessful());

        ReservationJsonRepository reloadedRepository =
                new ReservationJsonRepository(runtimeFile);
        List<Reservation> reloadedReservations =
                reloadedRepository.loadReservations();

        assertEquals(
                List.of(2),
                reloadedReservations.stream()
                        .map(Reservation::getId)
                        .toList()
        );

        AvailabilityController availabilityController =
                new AvailabilityController(
                        new AvailabilityService(),
                        reloadedRepository
                );
        List<TimeSlot> timeSlots =
                availabilityController.getAvailabilityForDay(1, TEST_DATE);

        assertTrue(findSlot(timeSlots, 9).isAvailable());
        assertTrue(findSlot(timeSlots, 13).isReserved());
    }

    @Test
    void regularUserDenialLeavesPersistedDataUnchanged()
            throws IOException {
        Path runtimeFile = temporaryDirectory.resolve(
                "denied-reservations.json"
        );
        ReservationJsonRepository repository =
                new ReservationJsonRepository(runtimeFile);

        repository.saveReservations(List.of(
                reservation(1, 1, "student", 9, 10),
                reservation(2, 1, "admin", 13, 14)
        ));
        String contentsBeforeAttempt = Files.readString(runtimeFile);

        AdminReservationCancellationResult result = controller(
                new User("student", "student123", false),
                repository
        ).cancelReservation(1);

        assertFalse(result.isSuccessful());
        assertEquals(
                AdminReservationCancellationResult.Status.UNAUTHORIZED,
                result.getStatus()
        );
        assertEquals(contentsBeforeAttempt, Files.readString(runtimeFile));
        assertEquals(
                List.of(1, 2),
                new ReservationJsonRepository(runtimeFile)
                        .loadReservations()
                        .stream()
                        .map(Reservation::getId)
                        .toList()
        );
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

    private TimeSlot findSlot(List<TimeSlot> timeSlots, int startHour) {
        return timeSlots.stream()
                .filter(timeSlot -> timeSlot.getStartTime().equals(
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

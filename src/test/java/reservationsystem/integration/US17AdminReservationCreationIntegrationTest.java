package reservationsystem.integration;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import reservationsystem.controller.AdminReservationController;
import reservationsystem.model.Reservation;
import reservationsystem.model.User;
import reservationsystem.persistence.ReservationJsonRepository;
import reservationsystem.persistence.UserJsonRepository;
import reservationsystem.service.AdminReservationCreationResult;
import reservationsystem.service.AuthenticatedUserProvider;
import reservationsystem.service.AuthorizationService;
import reservationsystem.service.ReservationService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class US17AdminReservationCreationIntegrationTest {
	
	@TempDir
    Path temporaryDirectory;

    @Test
    void administratorCreatedReservationPersistsForSelectedUser() {
        Path runtimeFile =
                temporaryDirectory.resolve("reservations.json");

        ReservationJsonRepository repository =
                new ReservationJsonRepository(runtimeFile);

        repository.saveReservations(List.of(
                new Reservation(
                        10,
                        1,
                        "admin",
                        LocalDate.of(2026, 8, 2),
                        LocalTime.of(8, 0),
                        LocalTime.of(9, 0)
                )
        ));

        AdminReservationController controller =
                createController(
                        new User("admin", "admin123", true),
                        repository
                );

        AdminReservationCreationResult result =
                controller.createReservationForUser(
                        "student",
                        1,
                        LocalDate.of(2026, 8, 2),
                        LocalTime.of(10, 0),
                        LocalTime.of(11, 0)
                );

        assertTrue(result.isSuccessful());

        ReservationJsonRepository reloadedRepository =
                new ReservationJsonRepository(runtimeFile);

        List<Reservation> reloadedReservations =
                reloadedRepository.loadReservations();

        assertEquals(2, reloadedReservations.size());

        Reservation createdReservation =
                reloadedReservations.stream()
                        .filter(reservation ->
                                reservation.getId() == 11)
                        .findFirst()
                        .orElseThrow();

        assertEquals("student", createdReservation.getUserId());
        assertEquals(1, createdReservation.getSpaceId());
        assertEquals(
                LocalDate.of(2026, 8, 2),
                createdReservation.getDate()
        );
        assertEquals(
                LocalTime.of(10, 0),
                createdReservation.getStartTime()
        );
        assertEquals(
                LocalTime.of(11, 0),
                createdReservation.getEndTime()
        );
    }

    @Test
    void rejectedConflictLeavesPersistedDataUnchanged()
            throws IOException {

        Path runtimeFile =
                temporaryDirectory.resolve("reservations.json");

        ReservationJsonRepository repository =
                new ReservationJsonRepository(runtimeFile);

        repository.saveReservations(List.of(
                new Reservation(
                        1,
                        1,
                        "admin",
                        LocalDate.of(2026, 8, 3),
                        LocalTime.of(10, 0),
                        LocalTime.of(11, 0)
                )
        ));

        String contentsBeforeAttempt =
                Files.readString(runtimeFile);

        AdminReservationController controller =
                createController(
                        new User("admin", "admin123", true),
                        repository
                );

        AdminReservationCreationResult result =
                controller.createReservationForUser(
                        "student",
                        1,
                        LocalDate.of(2026, 8, 3),
                        LocalTime.of(10, 30),
                        LocalTime.of(11, 30)
                );

        String contentsAfterAttempt =
                Files.readString(runtimeFile);

        assertEquals(
                AdminReservationCreationResult.Status.VALIDATION_FAILED,
                result.getStatus()
        );
        assertEquals(contentsBeforeAttempt, contentsAfterAttempt);
        assertEquals(
                1,
                new ReservationJsonRepository(runtimeFile)
                        .loadReservations()
                        .size()
        );
    }

    private AdminReservationController createController(
            User currentUser,
            ReservationJsonRepository reservationRepository
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

}

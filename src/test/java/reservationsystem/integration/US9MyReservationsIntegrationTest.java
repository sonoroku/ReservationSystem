package reservationsystem.integration;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import reservationsystem.controller.ReservationController;
import reservationsystem.controller.SpaceController;
import reservationsystem.model.Reservation;
import reservationsystem.model.Space;
import reservationsystem.persistence.ReservationJsonRepository;
import reservationsystem.persistence.SpaceJsonRepository;
import reservationsystem.service.DefaultUserProvider;
import reservationsystem.service.MyReservationsService;
import reservationsystem.service.ReservationService;

import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class US9MyReservationsIntegrationTest {

    @TempDir
    Path tempDirectory;

    @Test
    void controllerLoadsDefaultUserReservationsInDeterministicOrderWithSpaceDetails() {
        ReservationJsonRepository repository = createRepository("mixed-users.json");
        repository.saveReservations(List.of(
                reservation(5, 1, "student", 8, 9),
                reservation(2, 2, "admin", 8, 8),
                reservation(4, 3, "student", 9, 11),
                reservation(3, 1, "student", 8, 9)
        ));

        ReservationController controller = createController(repository);

        List<Reservation> reservations = controller.getMyReservations();

        assertEquals(List.of(3, 5, 4), reservations.stream().map(Reservation::getId).toList());
        assertTrue(reservations.stream().allMatch(
                reservation -> DefaultUserProvider.DEFAULT_USER_ID.equals(reservation.getUserId())
        ));

        Space space = controller.getSpaceForReservation(reservations.get(0)).orElseThrow();
        assertEquals(1, space.getId());
        assertEquals("Student Union Conference Room 1", space.getName());
    }

    @Test
    void controllerReturnsEmptyListWhenDefaultUserHasNoReservations() {
        ReservationJsonRepository repository = createRepository("no-student-reservations.json");
        repository.saveReservations(List.of(reservation(1, 1, "admin", 8, 9)));

        List<Reservation> reservations = createController(repository).getMyReservations();

        assertTrue(reservations.isEmpty());
    }

    private ReservationController createController(ReservationJsonRepository repository) {
        return new ReservationController(
                repository,
                new ReservationService(),
                new DefaultUserProvider(),
                new MyReservationsService(),
                new SpaceController(new SpaceJsonRepository())
        );
    }

    private ReservationJsonRepository createRepository(String fileName) {
        return new ReservationJsonRepository(tempDirectory.resolve(fileName));
    }

    private Reservation reservation(int id, int spaceId, String userId, int day, int startHour) {
        return new Reservation(
                id,
                spaceId,
                userId,
                LocalDate.of(2026, 7, day),
                LocalTime.of(startHour, 0),
                LocalTime.of(startHour + 1, 0)
        );
    }
}

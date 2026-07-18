package reservationsystem.integration;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import reservationsystem.controller.AvailabilityController;
import reservationsystem.controller.ReservationController;
import reservationsystem.controller.SpaceController;
import reservationsystem.model.Reservation;
import reservationsystem.model.TimeSlot;
import reservationsystem.persistence.ReservationJsonRepository;
import reservationsystem.service.AvailabilityService;
import reservationsystem.service.DefaultUserProvider;
import reservationsystem.service.MyReservationsService;
import reservationsystem.service.ReservationCancellationResult;
import reservationsystem.service.ReservationService;

import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class US10CancelReservationIntegrationTest {
	
	@TempDir
    Path tempDirectory;

    @Test
    void cancellingOwnedReservationRemovesItFromPersistenceAndReleasesAvailability() {
        ReservationJsonRepository repository = createRepository("cancel-owned-reservation.json");
        repository.saveReservations(List.of(
                reservation(1, 1, DefaultUserProvider.DEFAULT_USER_ID, 9, 10),
                reservation(2, 1, "admin", 13, 14)
        ));

        ReservationController reservationController = createReservationController(repository);

        ReservationCancellationResult result = reservationController.cancelReservation(1);

        assertTrue(result.isSuccessful());

        List<Reservation> remainingReservations = repository.loadReservations();

        assertEquals(1, remainingReservations.size());
        assertEquals(2, remainingReservations.get(0).getId());

        AvailabilityController availabilityController = new AvailabilityController(
                new AvailabilityService(),
                repository
        );

        List<TimeSlot> timeSlots = availabilityController.getAvailabilityForDay(
                1,
                LocalDate.of(2026, 7, 8)
        );

        TimeSlot releasedSlot = findSlotStartingAt(timeSlots, LocalTime.of(9, 0));
        TimeSlot unrelatedReservedSlot = findSlotStartingAt(timeSlots, LocalTime.of(13, 0));

        assertTrue(releasedSlot.isAvailable());
        assertTrue(unrelatedReservedSlot.isReserved());
    }

    @Test
    void cancellingMissingReservationDoesNotChangePersistence() {
        ReservationJsonRepository repository = createRepository("cancel-missing-reservation.json");
        repository.saveReservations(List.of(
                reservation(1, 1, DefaultUserProvider.DEFAULT_USER_ID, 9, 10),
                reservation(2, 2, "admin", 13, 14)
        ));

        ReservationController reservationController = createReservationController(repository);

        ReservationCancellationResult result = reservationController.cancelReservation(99);

        assertFalse(result.isSuccessful());
        assertEquals("Reservation was not found", result.getMessage());

        List<Reservation> reservations = repository.loadReservations();

        assertEquals(List.of(1, 2), reservations.stream().map(Reservation::getId).toList());
    }

    @Test
    void cancellingReservationOwnedByAnotherUserDoesNotChangePersistence() {
        ReservationJsonRepository repository = createRepository("cancel-not-owned-reservation.json");
        repository.saveReservations(List.of(
                reservation(1, 1, "admin", 9, 10),
                reservation(2, 2, DefaultUserProvider.DEFAULT_USER_ID, 13, 14)
        ));

        ReservationController reservationController = createReservationController(repository);

        ReservationCancellationResult result = reservationController.cancelReservation(1);

        assertFalse(result.isSuccessful());
        assertEquals("Reservation does not belong to current user", result.getMessage());

        List<Reservation> reservations = repository.loadReservations();

        assertEquals(List.of(1, 2), reservations.stream().map(Reservation::getId).toList());
    }

    private ReservationController createReservationController(ReservationJsonRepository repository) {
        return new ReservationController(
                repository,
                new ReservationService(),
                new DefaultUserProvider(),
                new MyReservationsService(),
                new SpaceController()
        );
    }

    private ReservationJsonRepository createRepository(String fileName) {
        return new ReservationJsonRepository(tempDirectory.resolve(fileName));
    }

    private Reservation reservation(int id, int spaceId, String userId, int startHour, int endHour) {
        return new Reservation(
                id,
                spaceId,
                userId,
                LocalDate.of(2026, 7, 8),
                LocalTime.of(startHour, 0),
                LocalTime.of(endHour, 0)
        );
    }

    private TimeSlot findSlotStartingAt(List<TimeSlot> timeSlots, LocalTime startTime) {
        return timeSlots.stream()
                .filter(timeSlot -> timeSlot.getStartTime().equals(startTime))
                .findFirst()
                .orElseThrow(() -> new AssertionError("Expected time slot starting at " + startTime));
    }

}

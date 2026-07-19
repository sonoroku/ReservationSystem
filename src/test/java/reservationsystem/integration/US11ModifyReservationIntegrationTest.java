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
import reservationsystem.service.ReservationModificationResult;
import reservationsystem.service.ReservationService;

import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class US11ModifyReservationIntegrationTest {

    private static final LocalDate RESERVATION_DATE = LocalDate.of(2026, 7, 8);

    @TempDir
    Path tempDirectory;

    @Test
    void movingReservationUpdatesPersistenceAndAvailability() {
        ReservationJsonRepository repository = new ReservationJsonRepository(
                tempDirectory.resolve("modified-availability.json")
        );
        repository.saveReservations(List.of(new Reservation(
                1,
                1,
                DefaultUserProvider.DEFAULT_USER_ID,
                RESERVATION_DATE,
                LocalTime.of(9, 0),
                LocalTime.of(10, 0)
        )));

        ReservationController reservationController = new ReservationController(
                repository,
                new ReservationService(),
                new DefaultUserProvider(),
                new MyReservationsService(),
                new SpaceController()
        );
        AvailabilityController availabilityController = new AvailabilityController(
                new AvailabilityService(),
                repository
        );

        ReservationModificationResult result = reservationController.modifyReservation(
                1,
                1,
                RESERVATION_DATE,
                LocalTime.of(13, 0),
                LocalTime.of(14, 0)
        );

        assertTrue(result.isSuccessful());

        List<TimeSlot> timeSlots = availabilityController.getAvailabilityForDay(1, RESERVATION_DATE);
        assertTrue(findSlot(timeSlots, LocalTime.of(9, 0)).isAvailable());
        assertFalse(findSlot(timeSlots, LocalTime.of(13, 0)).isAvailable());
    }

    private TimeSlot findSlot(List<TimeSlot> timeSlots, LocalTime startTime) {
        return timeSlots.stream()
                .filter(timeSlot -> timeSlot.getStartTime().equals(startTime))
                .findFirst()
                .orElseThrow(() -> new AssertionError("Expected time slot starting at " + startTime));
    }
}

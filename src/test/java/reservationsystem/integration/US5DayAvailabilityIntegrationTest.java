package reservationsystem.integration;

import org.junit.jupiter.api.Test;
import reservationsystem.controller.AvailabilityController;
import reservationsystem.model.TimeSlot;
import reservationsystem.persistence.ReservationJsonRepository;
import reservationsystem.service.AvailabilityService;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class US5DayAvailabilityIntegrationTest {

    @Test
    void reservationDataLoadsAndGeneratesAvailabilityForSelectedSpaceAndDate() {
        // US-5 Acceptance Test:
        // Given the user selects a space and date,
        // when availability is requested using reservation data from JSON,
        // then the schedule shows reserved and available 30-minute slots.

        AvailabilityService availabilityService = new AvailabilityService();
        ReservationJsonRepository reservationJsonRepository = new ReservationJsonRepository();

        AvailabilityController controller = new AvailabilityController(
                availabilityService,
                reservationJsonRepository
        );

        List<TimeSlot> slots = controller.getAvailabilityForDay(
                1,
                LocalDate.of(2026, 7, 8)
        );

        assertNotNull(slots);
        assertEquals(24, slots.size());

        long reservedCount = slots.stream()
                .filter(TimeSlot::isReserved)
                .count();

        long availableCount = slots.stream()
                .filter(TimeSlot::isAvailable)
                .count();

        assertEquals(2, reservedCount);
        assertEquals(22, availableCount);
    }

    @Test
    void availabilityForSpaceWithoutReservationsShowsAllSlotsAvailable() {
        // US-5 Acceptance Test:
        // Given a selected space and date with no matching reservations,
        // when availability is requested,
        // then all 30-minute slots remain available.

        AvailabilityService availabilityService = new AvailabilityService();
        ReservationJsonRepository reservationJsonRepository = new ReservationJsonRepository();

        AvailabilityController controller = new AvailabilityController(
                availabilityService,
                reservationJsonRepository
        );

        List<TimeSlot> slots = controller.getAvailabilityForDay(
                5,
                LocalDate.of(2026, 7, 8)
        );

        assertNotNull(slots);
        assertEquals(24, slots.size());

        long reservedCount = slots.stream()
                .filter(TimeSlot::isReserved)
                .count();

        long availableCount = slots.stream()
                .filter(TimeSlot::isAvailable)
                .count();

        assertEquals(0, reservedCount);
        assertEquals(24, availableCount);
    }

    @Test
    void longerReservationMarksThreeSlotsReserved() {
        // US-5 Acceptance Test:
        // Given a selected space and date with an existing longer reservation,
        // when availability is requested,
        // then every overlapping 30-minute slot is marked reserved.

        AvailabilityService availabilityService = new AvailabilityService();
        ReservationJsonRepository reservationJsonRepository = new ReservationJsonRepository();

        AvailabilityController controller = new AvailabilityController(
                availabilityService,
                reservationJsonRepository
        );

        List<TimeSlot> slots = controller.getAvailabilityForDay(
                2,
                LocalDate.of(2026, 7, 8)
        );

        assertNotNull(slots);
        assertEquals(24, slots.size());

        long reservedCount = slots.stream()
                .filter(TimeSlot::isReserved)
                .count();

        long availableCount = slots.stream()
                .filter(TimeSlot::isAvailable)
                .count();

        assertEquals(3, reservedCount);
        assertEquals(21, availableCount);
    }
}

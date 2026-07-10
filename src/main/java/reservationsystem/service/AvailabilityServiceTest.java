package reservationsystem.service;

import org.junit.jupiter.api.Test;
import reservationsystem.model.Reservation;
import reservationsystem.model.TimeSlot;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AvailabilityServiceTest {

    @Test
    void createsCorrectNumberOfThirtyMinuteSlots() {
        AvailabilityService service = new AvailabilityService();

        List<TimeSlot> slots = service.getAvailabilityForDay(
                1,
                LocalDate.of(2026, 7, 8),
                List.of()
        );

        assertEquals(24, slots.size());
    }

    @Test
    void slotsStartAtEightAmAndEndAtEightPm() {
        AvailabilityService service = new AvailabilityService();

        List<TimeSlot> slots = service.getAvailabilityForDay(
                1,
                LocalDate.of(2026, 7, 8),
                List.of()
        );

        assertEquals(LocalTime.of(8, 0), slots.get(0).getStartTime());
        assertEquals(LocalTime.of(20, 0), slots.get(slots.size() - 1).getEndTime());
    }

    @Test
    void reservationForSelectedSpaceAndDateMarksMatchingSlotsReserved() {
        AvailabilityService service = new AvailabilityService();

        Reservation reservation = new Reservation(
                1,
                1,
                "user001",
                LocalDate.of(2026, 7, 8),
                LocalTime.of(9, 0),
                LocalTime.of(10, 0)
        );

        List<TimeSlot> slots = service.getAvailabilityForDay(
                1,
                LocalDate.of(2026, 7, 8),
                List.of(reservation)
        );

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
    void partialBookingMarksOnlyOverlappingSlotsReserved() {
        AvailabilityService service = new AvailabilityService();

        Reservation reservation = new Reservation(
                1,
                1,
                "user001",
                LocalDate.of(2026, 7, 8),
                LocalTime.of(9, 15),
                LocalTime.of(10, 15)
        );

        List<TimeSlot> slots = service.getAvailabilityForDay(
                1,
                LocalDate.of(2026, 7, 8),
                List.of(reservation)
        );

        long reservedCount = slots.stream()
                .filter(TimeSlot::isReserved)
                .count();

        long availableCount = slots.stream()
                .filter(TimeSlot::isAvailable)
                .count();

        assertEquals(3, reservedCount);
        assertEquals(21, availableCount);
    }

    @Test
    void reservationForDifferentSpaceDoesNotMarkSlotsReserved() {
        AvailabilityService service = new AvailabilityService();

        Reservation reservation = new Reservation(
                1,
                2,
                "user001",
                LocalDate.of(2026, 7, 8),
                LocalTime.of(9, 0),
                LocalTime.of(10, 0)
        );

        List<TimeSlot> slots = service.getAvailabilityForDay(
                1,
                LocalDate.of(2026, 7, 8),
                List.of(reservation)
        );

        assertTrue(slots.stream().allMatch(TimeSlot::isAvailable));
    }

    @Test
    void reservationForDifferentDateDoesNotMarkSlotsReserved() {
        AvailabilityService service = new AvailabilityService();

        Reservation reservation = new Reservation(
                1,
                1,
                "user001",
                LocalDate.of(2026, 7, 9),
                LocalTime.of(9, 0),
                LocalTime.of(10, 0)
        );

        List<TimeSlot> slots = service.getAvailabilityForDay(
                1,
                LocalDate.of(2026, 7, 8),
                List.of(reservation)
        );

        assertTrue(slots.stream().allMatch(TimeSlot::isAvailable));
    }
}

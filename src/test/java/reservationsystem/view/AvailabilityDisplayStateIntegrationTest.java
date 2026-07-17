package reservationsystem.view;

import org.junit.jupiter.api.Test;
import reservationsystem.model.Reservation;
import reservationsystem.model.TimeSlot;
import reservationsystem.service.AvailabilityService;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class AvailabilityDisplayStateIntegrationTest {
	
	@Test
    void calculatedReservedSlotsMapToReservedDisplayState() {
        AvailabilityService availabilityService = new AvailabilityService();
        AvailabilityDisplayMapper displayMapper = new AvailabilityDisplayMapper();

        Reservation reservation = new Reservation(
                1,
                1,
                "user001",
                LocalDate.of(2026, 7, 8),
                LocalTime.of(9, 0),
                LocalTime.of(10, 0)
        );

        List<TimeSlot> timeSlots = availabilityService.getAvailabilityForDay(
                1,
                LocalDate.of(2026, 7, 8),
                List.of(reservation)
        );

        List<AvailabilityDisplayState> reservedDisplayStates = timeSlots.stream()
                .filter(TimeSlot::isReserved)
                .map(displayMapper::map)
                .toList();

        assertEquals(2, reservedDisplayStates.size());

        for (AvailabilityDisplayState displayState : reservedDisplayStates) {
            assertEquals("Reserved", displayState.getStatusText());
            assertEquals("availability-slot-reserved", displayState.getStyleClass());
        }
    }

    @Test
    void calculatedAvailableSlotsMapToAvailableDisplayState() {
        AvailabilityService availabilityService = new AvailabilityService();
        AvailabilityDisplayMapper displayMapper = new AvailabilityDisplayMapper();

        Reservation reservation = new Reservation(
                1,
                1,
                "user001",
                LocalDate.of(2026, 7, 8),
                LocalTime.of(9, 0),
                LocalTime.of(10, 0)
        );

        List<TimeSlot> timeSlots = availabilityService.getAvailabilityForDay(
                1,
                LocalDate.of(2026, 7, 8),
                List.of(reservation)
        );

        List<AvailabilityDisplayState> availableDisplayStates = timeSlots.stream()
                .filter(TimeSlot::isAvailable)
                .map(displayMapper::map)
                .toList();

        assertEquals(22, availableDisplayStates.size());

        for (AvailabilityDisplayState displayState : availableDisplayStates) {
            assertEquals("Available", displayState.getStatusText());
            assertEquals("availability-slot-available", displayState.getStyleClass());
        }
    }

    @Test
    void nonOverlappingSlotsRemainAvailableAndMapToAvailableDisplayState() {
        AvailabilityService availabilityService = new AvailabilityService();
        AvailabilityDisplayMapper displayMapper = new AvailabilityDisplayMapper();

        Reservation reservation = new Reservation(
                1,
                1,
                "user001",
                LocalDate.of(2026, 7, 8),
                LocalTime.of(9, 0),
                LocalTime.of(10, 0)
        );

        List<TimeSlot> timeSlots = availabilityService.getAvailabilityForDay(
                1,
                LocalDate.of(2026, 7, 8),
                List.of(reservation)
        );

        TimeSlot eightAmSlot = timeSlots.get(0);

        AvailabilityDisplayState displayState = displayMapper.map(eightAmSlot);

        assertEquals(LocalTime.of(8, 0), eightAmSlot.getStartTime());
        assertEquals("Available", displayState.getStatusText());
        assertEquals("availability-slot-available", displayState.getStyleClass());
    }

}

package reservationsystem.view;

import org.junit.jupiter.api.Test;
import reservationsystem.model.Reservation;
import reservationsystem.model.TimeSlot;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class AvailabilityDisplayMapperTest {
	
	@Test
    void mapsAvailableTimeSlotToAvailableDisplayState() {
        AvailabilityDisplayMapper mapper = new AvailabilityDisplayMapper();
        TimeSlot timeSlot = new TimeSlot(LocalTime.of(9, 0), LocalTime.of(9, 30));

        AvailabilityDisplayState displayState = mapper.map(timeSlot);

        assertEquals("Available", displayState.getStatusText());
        assertEquals("availability-slot-available", displayState.getStyleClass());
    }

    @Test
    void mapsReservedTimeSlotToReservedDisplayState() {
        AvailabilityDisplayMapper mapper = new AvailabilityDisplayMapper();
        TimeSlot timeSlot = new TimeSlot(LocalTime.of(9, 0), LocalTime.of(9, 30));

        Reservation reservation = new Reservation(
                1,
                1,
                "user001",
                LocalDate.of(2026, 7, 8),
                LocalTime.of(9, 0),
                LocalTime.of(9, 30)
        );

        timeSlot.markReserved(reservation);

        AvailabilityDisplayState displayState = mapper.map(timeSlot);

        assertEquals("Reserved", displayState.getStatusText());
        assertEquals("availability-slot-reserved", displayState.getStyleClass());
    }

    @Test
    void rejectsNullTimeSlot() {
        AvailabilityDisplayMapper mapper = new AvailabilityDisplayMapper();

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> mapper.map(null)
        );

        assertEquals("Time slot cannot be null", exception.getMessage());
    }

}

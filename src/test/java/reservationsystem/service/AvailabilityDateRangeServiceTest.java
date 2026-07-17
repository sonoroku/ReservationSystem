package reservationsystem.service;

import org.junit.jupiter.api.Test;
import reservationsystem.model.DatedAvailability;
import reservationsystem.model.Reservation;
import reservationsystem.model.TimeSlot;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class AvailabilityDateRangeServiceTest {
	
	@Test
    void sameDayRangeReturnsOneDatedSchedule() {
        AvailabilityService service = new AvailabilityService();

        List<DatedAvailability> result = service.getAvailabilityForDateRange(
                1,
                LocalDate.of(2026, 7, 8),
                LocalDate.of(2026, 7, 8),
                List.of()
        );

        assertEquals(1, result.size());
        assertEquals(LocalDate.of(2026, 7, 8), result.get(0).getDate());
        assertEquals(24, result.get(0).getTimeSlots().size());
    }

    @Test
    void sevenDayRangeReturnsSevenDatedSchedules() {
        AvailabilityService service = new AvailabilityService();

        List<DatedAvailability> result = service.getAvailabilityForDateRange(
                1,
                LocalDate.of(2026, 7, 8),
                LocalDate.of(2026, 7, 14),
                List.of()
        );

        assertEquals(7, result.size());
        assertEquals(LocalDate.of(2026, 7, 8), result.get(0).getDate());
        assertEquals(LocalDate.of(2026, 7, 14), result.get(6).getDate());
    }

    @Test
    void reversedDateRangeIsRejected() {
        AvailabilityService service = new AvailabilityService();

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> service.getAvailabilityForDateRange(
                        1,
                        LocalDate.of(2026, 7, 14),
                        LocalDate.of(2026, 7, 8),
                        List.of()
                )
        );

        assertEquals("Start date cannot be after end date", exception.getMessage());
    }

    @Test
    void rangeLongerThanSevenDaysIsRejected() {
        AvailabilityService service = new AvailabilityService();

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> service.getAvailabilityForDateRange(
                        1,
                        LocalDate.of(2026, 7, 8),
                        LocalDate.of(2026, 7, 15),
                        List.of()
                )
        );

        assertEquals("Date range cannot be longer than 7 days", exception.getMessage());
    }

    @Test
    void datedSchedulesAreReturnedChronologically() {
        AvailabilityService service = new AvailabilityService();

        List<DatedAvailability> result = service.getAvailabilityForDateRange(
                1,
                LocalDate.of(2026, 7, 8),
                LocalDate.of(2026, 7, 10),
                List.of()
        );

        assertEquals(LocalDate.of(2026, 7, 8), result.get(0).getDate());
        assertEquals(LocalDate.of(2026, 7, 9), result.get(1).getDate());
        assertEquals(LocalDate.of(2026, 7, 10), result.get(2).getDate());
    }

    @Test
    void reservationMarksOnlyMatchingDateSlotsReserved() {
        AvailabilityService service = new AvailabilityService();

        Reservation reservation = new Reservation(
                1,
                1,
                "user001",
                LocalDate.of(2026, 7, 9),
                LocalTime.of(9, 0),
                LocalTime.of(10, 0)
        );

        List<DatedAvailability> result = service.getAvailabilityForDateRange(
                1,
                LocalDate.of(2026, 7, 8),
                LocalDate.of(2026, 7, 10),
                List.of(reservation)
        );

        long reservedOnJuly8 = countReservedSlots(result.get(0).getTimeSlots());
        long reservedOnJuly9 = countReservedSlots(result.get(1).getTimeSlots());
        long reservedOnJuly10 = countReservedSlots(result.get(2).getTimeSlots());

        assertEquals(0, reservedOnJuly8);
        assertEquals(2, reservedOnJuly9);
        assertEquals(0, reservedOnJuly10);
    }

    private long countReservedSlots(List<TimeSlot> timeSlots) {
        return timeSlots.stream()
                .filter(TimeSlot::isReserved)
                .count();
    }
    
    @Test
    void getAvailabilityForDateRangeRejectsNonpositiveSpaceId() {
        AvailabilityService service = new AvailabilityService();

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> service.getAvailabilityForDateRange(
                        0,
                        LocalDate.of(2026, 7, 8),
                        LocalDate.of(2026, 7, 10),
                        List.of()
                )
        );

        assertEquals("Space ID must be positive", exception.getMessage());
    }

}

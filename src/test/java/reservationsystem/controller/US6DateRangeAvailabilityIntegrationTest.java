package reservationsystem.controller;

import org.junit.jupiter.api.Test;
import reservationsystem.model.DatedAvailability;
import reservationsystem.model.Reservation;
import reservationsystem.model.TimeSlot;
import reservationsystem.persistence.ReservationJsonRepository;
import reservationsystem.service.AvailabilityService;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class US6DateRangeAvailabilityIntegrationTest {
	
	@Test
    void controllerLoadsReservationsOnceAndReturnsDatedAvailabilityRange() {
        Reservation reservation = new Reservation(
                1,
                1,
                "user001",
                LocalDate.of(2026, 7, 9),
                LocalTime.of(9, 0),
                LocalTime.of(10, 0)
        );

        FakeReservationJsonRepository repository = new FakeReservationJsonRepository(List.of(reservation));

        AvailabilityController controller = new AvailabilityController(
                new AvailabilityService(),
                repository
        );

        List<DatedAvailability> result = controller.getAvailabilityForDateRange(
                1,
                LocalDate.of(2026, 7, 8),
                LocalDate.of(2026, 7, 10)
        );

        assertEquals(1, repository.getLoadCount());
        assertEquals(3, result.size());
        assertEquals(LocalDate.of(2026, 7, 8), result.get(0).getDate());
        assertEquals(LocalDate.of(2026, 7, 9), result.get(1).getDate());
        assertEquals(LocalDate.of(2026, 7, 10), result.get(2).getDate());

        assertEquals(0, countReservedSlots(result.get(0).getTimeSlots()));
        assertEquals(2, countReservedSlots(result.get(1).getTimeSlots()));
        assertEquals(0, countReservedSlots(result.get(2).getTimeSlots()));
    }

    @Test
    void controllerRejectsInvalidDateRangeWithoutLaunchingJavaFx() {
        AvailabilityController controller = new AvailabilityController(
                new AvailabilityService(),
                new FakeReservationJsonRepository(List.of())
        );

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> controller.getAvailabilityForDateRange(
                        1,
                        LocalDate.of(2026, 7, 10),
                        LocalDate.of(2026, 7, 8)
                )
        );

        assertEquals("Start date cannot be after end date", exception.getMessage());
    }

    private long countReservedSlots(List<TimeSlot> timeSlots) {
        return timeSlots.stream()
                .filter(TimeSlot::isReserved)
                .count();
    }

    private static class FakeReservationJsonRepository extends ReservationJsonRepository {
        private final List<Reservation> reservations;
        private int loadCount;

        FakeReservationJsonRepository(List<Reservation> reservations) {
            this.reservations = reservations;
            this.loadCount = 0;
        }

        @Override
        public List<Reservation> loadReservations() {
            loadCount++;
            return reservations;
        }

        int getLoadCount() {
            return loadCount;
        }
    }

}

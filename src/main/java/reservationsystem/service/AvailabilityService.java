package reservationsystem.service;

import reservationsystem.model.Reservation;
import reservationsystem.model.TimeSlot;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class AvailabilityService {

    private static final LocalTime DAY_START_TIME = LocalTime.of(8, 0);
    private static final LocalTime DAY_END_TIME = LocalTime.of(20, 0);
    private static final int SLOT_MINUTES = 30;

    public List<TimeSlot> getAvailabilityForDay(int spaceId, LocalDate date, List<Reservation> reservations) {
        if (date == null) {
            throw new IllegalArgumentException("Date cannot be null");
        }

        if (reservations == null) {
            throw new IllegalArgumentException("Reservations cannot be null");
        }

        List<TimeSlot> timeSlots = createDailyTimeSlots();
        markReservedSlots(timeSlots, spaceId, date, reservations);

        return timeSlots;
    }

    private List<TimeSlot> createDailyTimeSlots() {
        List<TimeSlot> timeSlots = new ArrayList<>();

        LocalTime currentStartTime = DAY_START_TIME;

        while (currentStartTime.isBefore(DAY_END_TIME)) {
            LocalTime currentEndTime = currentStartTime.plusMinutes(SLOT_MINUTES);
            timeSlots.add(new TimeSlot(currentStartTime, currentEndTime));
            currentStartTime = currentEndTime;
        }

        return timeSlots;
    }

    private void markReservedSlots(
            List<TimeSlot> timeSlots,
            int spaceId,
            LocalDate date,
            List<Reservation> reservations
    ) {
        for (Reservation reservation : reservations) {
            if (isReservationForSpaceAndDate(reservation, spaceId, date)) {
                markOverlappingSlots(timeSlots, reservation);
            }
        }
    }

    private boolean isReservationForSpaceAndDate(Reservation reservation, int spaceId, LocalDate date) {
        return reservation.getSpaceId() == spaceId
                && reservation.getDate().equals(date);
    }

    private void markOverlappingSlots(List<TimeSlot> timeSlots, Reservation reservation) {
        for (TimeSlot timeSlot : timeSlots) {
            if (timeSlot.overlaps(reservation.getStartTime(), reservation.getEndTime())) {
                timeSlot.markReserved(reservation);
            }
        }
    }
}

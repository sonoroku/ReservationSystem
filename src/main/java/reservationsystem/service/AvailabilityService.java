package reservationsystem.service;

import reservationsystem.model.Reservation;
import reservationsystem.model.TimeSlot;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import reservationsystem.model.DatedAvailability;
import java.time.temporal.ChronoUnit;

public class AvailabilityService {

    private static final LocalTime DAY_START_TIME = LocalTime.of(8, 0);
    private static final LocalTime DAY_END_TIME = LocalTime.of(20, 0);
    private static final int SLOT_MINUTES = 30;
    private static final int MAX_DATE_RANGE_DAYS = 7;

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
    
    public List<DatedAvailability> getAvailabilityForDateRange(
            int spaceId,
            LocalDate startDate,
            LocalDate endDate,
            List<Reservation> reservations
    ) {
        validateDateRange(startDate, endDate, reservations);

        List<DatedAvailability> datedAvailabilityList = new ArrayList<>();

        LocalDate currentDate = startDate;

        while (!currentDate.isAfter(endDate)) {
            List<TimeSlot> timeSlots = getAvailabilityForDay(spaceId, currentDate, reservations);
            datedAvailabilityList.add(new DatedAvailability(currentDate, timeSlots));
            currentDate = currentDate.plusDays(1);
        }

        return datedAvailabilityList;
    }

    private void validateDateRange(LocalDate startDate, LocalDate endDate, List<Reservation> reservations) {
        if (startDate == null) {
            throw new IllegalArgumentException("Start date cannot be null");
        }

        if (endDate == null) {
            throw new IllegalArgumentException("End date cannot be null");
        }

        if (reservations == null) {
            throw new IllegalArgumentException("Reservations cannot be null");
        }

        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Start date cannot be after end date");
        }

        long inclusiveDayCount = ChronoUnit.DAYS.between(startDate, endDate) + 1;

        if (inclusiveDayCount > MAX_DATE_RANGE_DAYS) {
            throw new IllegalArgumentException("Date range cannot be longer than 7 days");
        }
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

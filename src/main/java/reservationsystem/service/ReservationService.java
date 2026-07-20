package reservationsystem.service;

import reservationsystem.model.Reservation;

import java.time.Duration;
import java.time.LocalTime;
import java.util.List;

public class ReservationService {
	
	

    public ReservationValidationResult validateReservation(
            Reservation newReservation,
            List<Reservation> existingReservations) {

        if (newReservation == null) {
            return ReservationValidationResult.invalid("Reservation cannot be null");
        }

        if (existingReservations == null) {
            return ReservationValidationResult.invalid("Existing reservations cannot be null");
        }

        LocalTime startTime = newReservation.getStartTime();
        LocalTime endTime = newReservation.getEndTime();

        if (startTime == null || endTime == null) {
            return ReservationValidationResult.invalid("Start time and end time are required");
        }

        if (!startTime.isBefore(endTime)) {
            return ReservationValidationResult.invalid("End time must be after start time");
        }

        long durationMinutes = Duration.between(startTime, endTime).toMinutes();

        if (durationMinutes > SchedulePolicy.MAX_RESERVATION_MINUTES) {
            return ReservationValidationResult.invalid("Reservation cannot be longer than 2 hours");
        }

        for (Reservation existingReservation : existingReservations) {
            if (isSameSpaceAndDate(newReservation, existingReservation)) {
                if (overlaps(newReservation, existingReservation)) {
                    return ReservationValidationResult.invalid("Reservation conflicts with an existing reservation");
                }

                if (violatesBuffer(newReservation, existingReservation)) {
                    return ReservationValidationResult.invalid("Reservation must be at least 10 minutes away from another reservation");
                }
            }
        }

        return ReservationValidationResult.valid();
    }

    public ReservationValidationResult validateReservationUpdate(
            Reservation updatedReservation,
            List<Reservation> existingReservations
    ) {
        if (updatedReservation == null) {
            return validateReservation(null, existingReservations);
        }

        if (existingReservations == null) {
            return validateReservation(updatedReservation, null);
        }

        List<Reservation> otherReservations = existingReservations.stream()
                .filter(reservation -> reservation.getId() != updatedReservation.getId())
                .toList();

        return validateReservation(updatedReservation, otherReservations);
    }

    private boolean isSameSpaceAndDate(Reservation reservationOne, Reservation reservationTwo) {
        return reservationOne.getSpaceId() == reservationTwo.getSpaceId()
                && reservationOne.getDate().equals(reservationTwo.getDate());
    }

    private boolean overlaps(Reservation newReservation, Reservation existingReservation) {
        return newReservation.getStartTime().isBefore(existingReservation.getEndTime())
                && existingReservation.getStartTime().isBefore(newReservation.getEndTime());
    }

    private boolean violatesBuffer(Reservation newReservation, Reservation existingReservation) {
    	LocalTime bufferedStart = existingReservation.getStartTime()
    	        .minusMinutes(SchedulePolicy.BUFFER_MINUTES);

    	LocalTime bufferedEnd = existingReservation.getEndTime()
    	        .plusMinutes(SchedulePolicy.BUFFER_MINUTES);

        return newReservation.getStartTime().isBefore(bufferedEnd)
                && bufferedStart.isBefore(newReservation.getEndTime());
    }

}

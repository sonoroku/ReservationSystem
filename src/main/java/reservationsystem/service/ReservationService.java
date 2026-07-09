package reservationsystem.service;

import reservationsystem.model.Reservation;

import java.time.Duration;
import java.time.LocalTime;
import java.util.List;

public class ReservationService {
	
	private static final long MAX_RESERVATION_MINUTES = 120;
    private static final long BUFFER_MINUTES = 10;

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

        if (durationMinutes > MAX_RESERVATION_MINUTES) {
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

    private boolean isSameSpaceAndDate(Reservation reservationOne, Reservation reservationTwo) {
        return reservationOne.getSpaceId() == reservationTwo.getSpaceId()
                && reservationOne.getDate().equals(reservationTwo.getDate());
    }

    private boolean overlaps(Reservation newReservation, Reservation existingReservation) {
        return newReservation.getStartTime().isBefore(existingReservation.getEndTime())
                && existingReservation.getStartTime().isBefore(newReservation.getEndTime());
    }

    private boolean violatesBuffer(Reservation newReservation, Reservation existingReservation) {
        LocalTime bufferedStart = existingReservation.getStartTime().minusMinutes(BUFFER_MINUTES);
        LocalTime bufferedEnd = existingReservation.getEndTime().plusMinutes(BUFFER_MINUTES);

        return newReservation.getStartTime().isBefore(bufferedEnd)
                && bufferedStart.isBefore(newReservation.getEndTime());
    }

}

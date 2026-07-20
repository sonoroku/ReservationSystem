package reservationsystem.service;

import reservationsystem.model.Reservation;
import reservationsystem.model.TimeSlot;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class TimeSuggestionService {
	
	private static final int SUGGESTION_RESERVATION_ID = -1;

    private final ReservationService reservationService;

    public TimeSuggestionService() {
        this(new ReservationService());
    }

    public TimeSuggestionService(ReservationService reservationService) {
        if (reservationService == null) {
            throw new IllegalArgumentException(
                    "Reservation service cannot be null"
            );
        }

        this.reservationService = reservationService;
    }

    public TimeSuggestionResult suggestAvailableTimes(
            int spaceId,
            LocalDate date,
            int durationMinutes,
            List<Reservation> existingReservations
    ) {
        TimeSuggestionResult validationResult = validateInput(
                spaceId,
                date,
                durationMinutes,
                existingReservations
        );

        if (validationResult != null) {
            return validationResult;
        }

        List<TimeSlot> suggestions = new ArrayList<>();
        LocalTime candidateStart = SchedulePolicy.DAY_START_TIME;

        while (!candidateStart
                .plusMinutes(durationMinutes)
                .isAfter(SchedulePolicy.DAY_END_TIME)) {

            LocalTime candidateEnd =
                    candidateStart.plusMinutes(durationMinutes);

            Reservation candidateReservation = new Reservation(
                    SUGGESTION_RESERVATION_ID,
                    spaceId,
                    DefaultUserProvider.DEFAULT_USER_ID,
                    date,
                    candidateStart,
                    candidateEnd
            );

            ReservationValidationResult reservationValidation =
                    reservationService.validateReservation(
                            candidateReservation,
                            existingReservations
                    );

            if (reservationValidation.isValid()) {
                suggestions.add(
                        new TimeSlot(candidateStart, candidateEnd)
                );
            }

            candidateStart = candidateStart.plusMinutes(
                    SchedulePolicy.TIME_INCREMENT_MINUTES
            );
        }

        return TimeSuggestionResult.success(suggestions);
    }

    private TimeSuggestionResult validateInput(
            int spaceId,
            LocalDate date,
            int durationMinutes,
            List<Reservation> existingReservations
    ) {
        if (spaceId <= 0) {
            return TimeSuggestionResult.invalid(
                    "Space ID must be positive"
            );
        }

        if (date == null) {
            return TimeSuggestionResult.invalid(
                    "Date cannot be null"
            );
        }

        if (durationMinutes <= 0) {
            return TimeSuggestionResult.invalid(
                    "Duration must be greater than 0 minutes"
            );
        }

        if (durationMinutes
                > SchedulePolicy.MAX_RESERVATION_MINUTES) {
            return TimeSuggestionResult.invalid(
                    "Duration cannot be longer than 120 minutes"
            );
        }

        if (existingReservations == null) {
            return TimeSuggestionResult.invalid(
                    "Reservations cannot be null"
            );
        }

        return null;
    }

}

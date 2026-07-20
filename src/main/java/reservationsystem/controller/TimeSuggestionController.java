package reservationsystem.controller;

import reservationsystem.model.Reservation;
import reservationsystem.persistence.ReservationJsonRepository;
import reservationsystem.service.TimeSuggestionResult;
import reservationsystem.service.TimeSuggestionService;

import java.time.LocalDate;
import java.util.List;

public class TimeSuggestionController {
	
	private final TimeSuggestionService timeSuggestionService;
    private final ReservationJsonRepository reservationJsonRepository;

    public TimeSuggestionController() {
        this(
                new TimeSuggestionService(),
                new ReservationJsonRepository()
        );
    }

    public TimeSuggestionController(
            TimeSuggestionService timeSuggestionService,
            ReservationJsonRepository reservationJsonRepository
    ) {
        if (timeSuggestionService == null) {
            throw new IllegalArgumentException(
                    "Time suggestion service cannot be null"
            );
        }

        if (reservationJsonRepository == null) {
            throw new IllegalArgumentException(
                    "Reservation repository cannot be null"
            );
        }

        this.timeSuggestionService = timeSuggestionService;
        this.reservationJsonRepository = reservationJsonRepository;
    }

    public TimeSuggestionResult suggestAvailableTimes(
            int spaceId,
            LocalDate date,
            int durationMinutes
    ) {
        List<Reservation> existingReservations =
                reservationJsonRepository.loadReservations();

        return timeSuggestionService.suggestAvailableTimes(
                spaceId,
                date,
                durationMinutes,
                existingReservations
        );
    }

}

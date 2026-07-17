package reservationsystem.controller;

import reservationsystem.model.Reservation;
import reservationsystem.model.TimeSlot;
import reservationsystem.persistence.ReservationJsonRepository;
import reservationsystem.service.AvailabilityService;
import reservationsystem.model.DatedAvailability;

import java.time.LocalDate;
import java.util.List;

public class AvailabilityController {

    private final AvailabilityService availabilityService;
    private final ReservationJsonRepository reservationJsonRepository;

    public AvailabilityController() {
        this(new AvailabilityService(), new ReservationJsonRepository());
    }

    public AvailabilityController(
            AvailabilityService availabilityService,
            ReservationJsonRepository reservationJsonRepository
    ) {
        this.availabilityService = availabilityService;
        this.reservationJsonRepository = reservationJsonRepository;
    }

    public List<TimeSlot> getAvailabilityForDay(int spaceId, LocalDate date) {
        List<Reservation> reservations = reservationJsonRepository.loadReservations();
        return availabilityService.getAvailabilityForDay(spaceId, date, reservations);
    }
    
    public List<DatedAvailability> getAvailabilityForDateRange(
            int spaceId,
            LocalDate startDate,
            LocalDate endDate
    ) {
        List<Reservation> reservations = reservationJsonRepository.loadReservations();
        return availabilityService.getAvailabilityForDateRange(
                spaceId,
                startDate,
                endDate,
                reservations
        );
    }
}
package reservationsystem.controller;

import reservationsystem.model.Reservation;
import reservationsystem.persistence.ReservationJsonRepository;
import reservationsystem.service.ReservationService;
import reservationsystem.service.ReservationValidationResult;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;

public class ReservationController {

    private final ReservationJsonRepository reservationJsonRepository;
    private final ReservationService reservationService;

    public ReservationController() {
        this(new ReservationJsonRepository(), new ReservationService());
    }

    public ReservationController(
            ReservationJsonRepository reservationJsonRepository,
            ReservationService reservationService
    ) {
        this.reservationJsonRepository = reservationJsonRepository;
        this.reservationService = reservationService;
    }

    public ReservationValidationResult createReservation(
            int spaceId,
            String userId,
            LocalDate date,
            LocalTime startTime,
            LocalTime endTime
    ) {
        List<Reservation> existingReservations = reservationJsonRepository.loadReservations();

        int newReservationId = getNextReservationId(existingReservations);

        Reservation newReservation = new Reservation(
                newReservationId,
                spaceId,
                userId,
                date,
                startTime,
                endTime
        );

        ReservationValidationResult validationResult =
                reservationService.validateReservation(newReservation, existingReservations);

        if (!validationResult.isValid()) {
            return validationResult;
        }

        existingReservations.add(newReservation);
        reservationJsonRepository.saveReservations(existingReservations);

        return ReservationValidationResult.valid();
    }

    private int getNextReservationId(List<Reservation> reservations) {
        return reservations.stream()
                .max(Comparator.comparingInt(Reservation::getId))
                .map(reservation -> reservation.getId() + 1)
                .orElse(1);
    }
}
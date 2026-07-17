package reservationsystem.controller;

import reservationsystem.model.Reservation;
import reservationsystem.persistence.ReservationJsonRepository;
import reservationsystem.service.MyReservationsService;

import java.util.List;

public class MyReservationsController {

    private final ReservationJsonRepository reservationJsonRepository;
    private final MyReservationsService myReservationsService;

    public MyReservationsController() {
        this(new ReservationJsonRepository(), new MyReservationsService());
    }

    public MyReservationsController(
            ReservationJsonRepository reservationJsonRepository,
            MyReservationsService myReservationsService
    ) {
        this.reservationJsonRepository = reservationJsonRepository;
        this.myReservationsService = myReservationsService;
    }

    public List<Reservation> getReservationsForUser(String userId) {
        List<Reservation> reservations = reservationJsonRepository.loadReservations();
        return myReservationsService.getReservationsForUser(userId, reservations);
    }
}

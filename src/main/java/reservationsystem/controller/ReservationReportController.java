package reservationsystem.controller;

import reservationsystem.model.Reservation;
import reservationsystem.model.Space;
import reservationsystem.model.User;
import reservationsystem.persistence.ReservationJsonRepository;
import reservationsystem.persistence.SpaceJsonRepository;
import reservationsystem.service.ReservationReportResult;
import reservationsystem.service.ReservationReportService;

import java.util.List;

public class ReservationReportController {
    private final ReservationJsonRepository reservationJsonRepository;
    private final SpaceJsonRepository spaceJsonRepository;
    private final ReservationReportService reservationReportService;

    public ReservationReportController() {
        this(
                new ReservationJsonRepository(),
                new SpaceJsonRepository(),
                new ReservationReportService()
        );
    }

    public ReservationReportController(
            ReservationJsonRepository reservationJsonRepository,
            SpaceJsonRepository spaceJsonRepository,
            ReservationReportService reservationReportService
    ) {
        this.reservationJsonRepository = reservationJsonRepository;
        this.spaceJsonRepository = spaceJsonRepository;
        this.reservationReportService = reservationReportService;
    }

    public ReservationReportResult getAllReservationsReport(User currentUser) {
        List<Reservation> reservations = reservationJsonRepository.loadReservations();
        List<Space> spaces = spaceJsonRepository.loadSpaces();

        return reservationReportService.generateAllReservationsReport(
                currentUser,
                reservations,
                spaces
        );
    }
}

package reservationsystem.controller;

import reservationsystem.model.Reservation;
import reservationsystem.model.Space;
import reservationsystem.persistence.ReservationJsonRepository;
import reservationsystem.persistence.SpaceJsonRepository;
import reservationsystem.service.AuthenticatedUserProvider;
import reservationsystem.service.AuthorizationResult;
import reservationsystem.service.AuthorizationService;
import reservationsystem.service.ReportDateRangeResult;
import reservationsystem.service.ReservationReportResult;
import reservationsystem.service.ReservationReportService;

import java.time.LocalDate;
import java.util.List;

public class ReservationReportController {
    private final ReservationJsonRepository reservationJsonRepository;
    private final SpaceJsonRepository spaceJsonRepository;
    private final ReservationReportService reservationReportService;
    private final AuthenticatedUserProvider authenticatedUserProvider;
    private final AuthorizationService authorizationService;

    public ReservationReportController(
            AuthenticatedUserProvider authenticatedUserProvider
    ) {
        this(
                new ReservationJsonRepository(),
                new SpaceJsonRepository(),
                new ReservationReportService(),
                authenticatedUserProvider,
                new AuthorizationService(authenticatedUserProvider)
        );
    }

    public ReservationReportController(
            ReservationJsonRepository reservationJsonRepository,
            SpaceJsonRepository spaceJsonRepository,
            ReservationReportService reservationReportService,
            AuthenticatedUserProvider authenticatedUserProvider,
            AuthorizationService authorizationService
    ) {
        if (reservationJsonRepository == null) {
            throw new IllegalArgumentException("Reservation repository cannot be null");
        }

        if (spaceJsonRepository == null) {
            throw new IllegalArgumentException("Space repository cannot be null");
        }

        if (reservationReportService == null) {
            throw new IllegalArgumentException("Reservation report service cannot be null");
        }

        if (authenticatedUserProvider == null) {
            throw new IllegalArgumentException("Authenticated user provider cannot be null");
        }

        if (authorizationService == null) {
            throw new IllegalArgumentException("Authorization service cannot be null");
        }

        this.reservationJsonRepository = reservationJsonRepository;
        this.spaceJsonRepository = spaceJsonRepository;
        this.reservationReportService = reservationReportService;
        this.authenticatedUserProvider = authenticatedUserProvider;
        this.authorizationService = authorizationService;
    }

    public ReservationReportResult getAllReservationsReport() {
        return getAllReservationsReport(null, null, false);
    }

    public ReservationReportResult getAllReservationsReport(
            LocalDate startDate,
            LocalDate endDate
    ) {
        return getAllReservationsReport(startDate, endDate, true);
    }

    private ReservationReportResult getAllReservationsReport(
            LocalDate startDate,
            LocalDate endDate,
            boolean filtered
    ) {
        AuthorizationResult authorizationResult = authorizationService.checkAdminAccess();

        if (!authorizationResult.isAuthorized()) {
            return ReservationReportResult.unauthorized(authorizationResult.getMessage());
        }

        if (filtered) {
            ReportDateRangeResult dateRangeResult =
                    ReportDateRangeResult.validate(startDate, endDate);

            if (!dateRangeResult.isValid()) {
                return ReservationReportResult.invalidDateRange(
                        dateRangeResult.getMessage()
                );
            }
        }

        List<Reservation> reservations = reservationJsonRepository.loadReservations();
        List<Space> spaces = spaceJsonRepository.loadSpaces();

        if (filtered) {
            return reservationReportService.generateAllReservationsReport(
                    authenticatedUserProvider.getCurrentUser(),
                    reservations,
                    spaces,
                    startDate,
                    endDate
            );
        }

        return reservationReportService.generateAllReservationsReport(
                authenticatedUserProvider.getCurrentUser(),
                reservations,
                spaces
        );
    }
}

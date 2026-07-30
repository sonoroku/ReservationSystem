package reservationsystem.controller;

import reservationsystem.model.Reservation;
import reservationsystem.model.Space;
import reservationsystem.persistence.ReservationJsonRepository;
import reservationsystem.persistence.SpaceJsonRepository;
import reservationsystem.service.AuthenticatedUserProvider;
import reservationsystem.service.AuthorizationResult;
import reservationsystem.service.AuthorizationService;
import reservationsystem.service.ReportDateRangeResult;
import reservationsystem.service.SpaceUsageReportResult;
import reservationsystem.service.SpaceUsageReportService;

import java.time.LocalDate;
import java.util.List;

public class SpaceUsageReportController {

    private final SpaceJsonRepository spaceJsonRepository;
    private final ReservationJsonRepository reservationJsonRepository;
    private final SpaceUsageReportService spaceUsageReportService;
    private final AuthorizationService authorizationService;

    public SpaceUsageReportController(
            AuthenticatedUserProvider authenticatedUserProvider
    ) {
        this(
                new SpaceJsonRepository(),
                new ReservationJsonRepository(),
                new SpaceUsageReportService(),
                new AuthorizationService(authenticatedUserProvider)
        );
    }

    public SpaceUsageReportController(
            SpaceJsonRepository spaceJsonRepository,
            ReservationJsonRepository reservationJsonRepository,
            SpaceUsageReportService spaceUsageReportService,
            AuthorizationService authorizationService
    ) {
        if (spaceJsonRepository == null) {
            throw new IllegalArgumentException(
                    "Space repository cannot be null"
            );
        }

        if (reservationJsonRepository == null) {
            throw new IllegalArgumentException(
                    "Reservation repository cannot be null"
            );
        }

        if (spaceUsageReportService == null) {
            throw new IllegalArgumentException(
                    "Space usage report service cannot be null"
            );
        }

        if (authorizationService == null) {
            throw new IllegalArgumentException(
                    "Authorization service cannot be null"
            );
        }

        this.spaceJsonRepository = spaceJsonRepository;
        this.reservationJsonRepository = reservationJsonRepository;
        this.spaceUsageReportService = spaceUsageReportService;
        this.authorizationService = authorizationService;
    }

    public SpaceUsageReportResult getSpaceUsageReport() {
        return getSpaceUsageReport(null, null, false);
    }

    public SpaceUsageReportResult getSpaceUsageReport(
            LocalDate startDate,
            LocalDate endDate
    ) {
        return getSpaceUsageReport(startDate, endDate, true);
    }

    private SpaceUsageReportResult getSpaceUsageReport(
            LocalDate startDate,
            LocalDate endDate,
            boolean filtered
    ) {
        AuthorizationResult authorizationResult =
                authorizationService.checkAdminAccess();

        if (!authorizationResult.isAuthorized()) {
            return SpaceUsageReportResult.unauthorized(
                    authorizationResult.getMessage()
            );
        }

        if (filtered) {
            ReportDateRangeResult dateRangeResult =
                    ReportDateRangeResult.validate(startDate, endDate);

            if (!dateRangeResult.isValid()) {
                return SpaceUsageReportResult.invalidDateRange(
                        dateRangeResult.getMessage()
                );
            }
        }

        List<Space> spaces = spaceJsonRepository.loadSpaces();
        List<Reservation> reservations =
                reservationJsonRepository.loadReservations();

        if (filtered) {
            return spaceUsageReportService.createReport(
                    spaces,
                    reservations,
                    startDate,
                    endDate
            );
        }

        return spaceUsageReportService.createReport(spaces, reservations);
    }
}

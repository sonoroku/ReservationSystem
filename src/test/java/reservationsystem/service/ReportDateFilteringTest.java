package reservationsystem.service;

import org.junit.jupiter.api.Test;
import reservationsystem.model.DailyReservationSummary;
import reservationsystem.model.Reservation;
import reservationsystem.model.Space;
import reservationsystem.model.SpaceUsageReportRow;
import reservationsystem.model.User;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ReportDateFilteringTest {

    private final User admin = new User("admin", "admin123", true);
    private final List<Space> spaces = List.of(
            space(1, "Auditorium"),
            space(2, "Study Room")
    );

    @Test
    void sharedDateRangeUsesInclusiveOneDayAndBoundaryPolicy() {
        LocalDate day = LocalDate.of(2026, 8, 11);
        ReportDateRangeResult result =
                ReportDateRangeResult.validate(day, day);

        assertTrue(result.isValid());
        assertTrue(result.getDateRange().includes(day));
        assertFalse(result.getDateRange().includes(day.minusDays(1)));
        assertFalse(result.getDateRange().includes(day.plusDays(1)));
    }

    @Test
    void reservationReportIncludesBothBoundariesAcrossMultipleDays() {
        ReservationReportResult result = new ReservationReportService()
                .generateAllReservationsReport(
                        admin,
                        List.of(
                                reservation(1, 1, 10),
                                reservation(2, 1, 11),
                                reservation(3, 2, 12),
                                reservation(4, 2, 13)
                        ),
                        spaces,
                        date(10),
                        date(12)
                );

        assertEquals(ReservationReportResult.Status.SUCCESS, result.getStatus());
        assertEquals(
                List.of(1, 2, 3),
                result.getEntries().stream()
                        .map(ReservationReportEntry::getReservationId)
                        .toList()
        );
    }

    @Test
    void usageReportRecalculatesCountsAndRetainsZeroCountSpaces() {
        SpaceUsageReportResult result = new SpaceUsageReportService()
                .createReport(
                        spaces,
                        List.of(
                                reservation(1, 1, 9),
                                reservation(2, 1, 10),
                                reservation(3, 2, 12)
                        ),
                        date(10),
                        date(11)
                );

        assertEquals(SpaceUsageReportResult.Status.SUCCESS, result.getStatus());
        assertEquals(
                List.of(1, 0),
                result.getRows().stream()
                        .map(SpaceUsageReportRow::getReservationCount)
                        .toList()
        );
    }

    @Test
    void missingDatesReturnControlledInvalidResults() {
        ReservationReportResult reservationResult =
                new ReservationReportService()
                        .generateAllReservationsReport(
                                admin,
                                List.of(),
                                spaces,
                                null,
                                date(10)
                        );
        SpaceUsageReportResult usageResult =
                new SpaceUsageReportService()
                        .createReport(
                                spaces,
                                List.of(),
                                date(10),
                                null
                        );

        assertEquals(
                ReservationReportResult.Status.INVALID_DATE_RANGE,
                reservationResult.getStatus()
        );
        assertEquals(
                SpaceUsageReportResult.Status.INVALID_DATE_RANGE,
                usageResult.getStatus()
        );
        assertEquals(
                "Start date and end date are required",
                reservationResult.getMessage()
        );
        assertEquals(reservationResult.getMessage(), usageResult.getMessage());
    }

    @Test
    void reversedDatesReturnOneConsistentControlledMessage() {
        ReservationReportResult reservationResult =
                new ReservationReportService()
                        .generateAllReservationsReport(
                                admin,
                                List.of(),
                                spaces,
                                date(12),
                                date(10)
                        );
        SpaceUsageReportResult usageResult =
                new SpaceUsageReportService()
                        .createReport(
                                spaces,
                                List.of(),
                                date(12),
                                date(10)
                        );

        assertEquals(
                "Start date cannot be after end date",
                reservationResult.getMessage()
        );
        assertEquals(reservationResult.getMessage(), usageResult.getMessage());
    }

    @Test
    void reservationReportStillPrioritizesAdministratorAuthorization() {
        ReservationReportResult result = new ReservationReportService()
                .generateAllReservationsReport(
                        new User("student", "student123", false),
                        List.of(),
                        spaces,
                        null,
                        null
                );

        assertEquals(
                ReservationReportResult.Status.UNAUTHORIZED,
                result.getStatus()
        );
    }

    @Test
    void validRangeWithNoReservationsReturnsControlledEmptyData() {
        List<Reservation> reservations = List.of(reservation(1, 1, 10));

        ReservationReportResult reservationResult =
                new ReservationReportService()
                        .generateAllReservationsReport(
                                admin,
                                reservations,
                                spaces,
                                date(20),
                                date(21)
                        );
        SpaceUsageReportResult usageResult =
                new SpaceUsageReportService()
                        .createReport(
                                spaces,
                                reservations,
                                date(20),
                                date(21)
                        );

        assertEquals(
                ReservationReportResult.Status.EMPTY,
                reservationResult.getStatus()
        );
        assertTrue(reservationResult.getEntries().isEmpty());
        assertEquals(
                List.of(0, 0),
                usageResult.getRows().stream()
                        .map(SpaceUsageReportRow::getReservationCount)
                        .toList()
        );
    }

    @Test
    void dailySummariesContinueToIncludeAllReservationDates() {
        DailyReservationSummaryResult result =
                new DailyReservationSummaryService().summarize(List.of(
                        reservation(1, 1, 10),
                        reservation(2, 1, 12)
                ));

        assertEquals(
                List.of(date(10), date(12)),
                result.getSummaries().stream()
                        .map(DailyReservationSummary::getDate)
                        .toList()
        );
    }

    private LocalDate date(int day) {
        return LocalDate.of(2026, 8, day);
    }

    private Space space(int id, String name) {
        return new Space(id, name, "Building", 10, List.of());
    }

    private Reservation reservation(int id, int spaceId, int day) {
        return new Reservation(
                id,
                spaceId,
                "student",
                date(day),
                LocalTime.of(9, 0),
                LocalTime.of(10, 0)
        );
    }
}

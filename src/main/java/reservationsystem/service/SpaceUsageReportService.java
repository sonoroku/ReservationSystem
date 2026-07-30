package reservationsystem.service;

import reservationsystem.model.Reservation;
import reservationsystem.model.Space;
import reservationsystem.model.SpaceUsageReportRow;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SpaceUsageReportService {

    public SpaceUsageReportResult createReport(
            List<Space> spaces,
            List<Reservation> reservations
    ) {
        return createReport(spaces, reservations, null);
    }

    public SpaceUsageReportResult createReport(
            List<Space> spaces,
            List<Reservation> reservations,
            LocalDate startDate,
            LocalDate endDate
    ) {
        ReportDateRangeResult dateRangeResult =
                ReportDateRangeResult.validate(startDate, endDate);

        if (!dateRangeResult.isValid()) {
            return SpaceUsageReportResult.invalidDateRange(
                    dateRangeResult.getMessage()
            );
        }

        return createReport(
                spaces,
                reservations,
                dateRangeResult.getDateRange()
        );
    }

    private SpaceUsageReportResult createReport(
            List<Space> spaces,
            List<Reservation> reservations,
            ReportDateRange dateRange
    ) {
        if (spaces == null) {
            throw new IllegalArgumentException("Spaces cannot be null");
        }

        if (reservations == null) {
            throw new IllegalArgumentException(
                    "Reservations cannot be null"
            );
        }

        Map<Integer, Integer> reservationCounts = new HashMap<>();

        for (Reservation reservation : reservations) {
            if (dateRange != null
                    && !dateRange.includes(reservation.getDate())) {
                continue;
            }

            reservationCounts.merge(
                    reservation.getSpaceId(),
                    1,
                    Integer::sum
            );
        }

        List<Space> orderedSpaces = new ArrayList<>(spaces);
        orderedSpaces.sort(
                Comparator.comparing(
                                Space::getName,
                                String.CASE_INSENSITIVE_ORDER
                        )
                        .thenComparing(Space::getName)
                        .thenComparingInt(Space::getId)
        );

        List<SpaceUsageReportRow> rows = new ArrayList<>();

        for (Space space : orderedSpaces) {
            rows.add(new SpaceUsageReportRow(
                    space,
                    reservationCounts.getOrDefault(space.getId(), 0)
            ));
        }

        return SpaceUsageReportResult.success(rows);
    }
}

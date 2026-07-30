package reservationsystem.service;

import java.time.LocalDate;

public class ReportDateRangeResult {

    private final ReportDateRange dateRange;
    private final String message;

    private ReportDateRangeResult(
            ReportDateRange dateRange,
            String message
    ) {
        this.dateRange = dateRange;
        this.message = message;
    }

    public static ReportDateRangeResult validate(
            LocalDate startDate,
            LocalDate endDate
    ) {
        if (startDate == null || endDate == null) {
            return invalid("Start date and end date are required");
        }

        if (startDate.isAfter(endDate)) {
            return invalid("Start date cannot be after end date");
        }

        return new ReportDateRangeResult(
                new ReportDateRange(startDate, endDate),
                ""
        );
    }

    private static ReportDateRangeResult invalid(String message) {
        return new ReportDateRangeResult(null, message);
    }

    public boolean isValid() {
        return dateRange != null;
    }

    public ReportDateRange getDateRange() {
        if (!isValid()) {
            throw new IllegalStateException(
                    "Invalid date range does not have a value"
            );
        }

        return dateRange;
    }

    public String getMessage() {
        return message;
    }
}

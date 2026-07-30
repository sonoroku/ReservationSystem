package reservationsystem.service;

import reservationsystem.model.SpaceUsageReportRow;

import java.util.List;

public class SpaceUsageReportResult {

    public enum Status {
        SUCCESS,
        EMPTY,
        UNAUTHORIZED,
        INVALID_DATE_RANGE
    }

    private final Status status;
    private final List<SpaceUsageReportRow> rows;
    private final String message;

    private SpaceUsageReportResult(
            Status status,
            List<SpaceUsageReportRow> rows,
            String message
    ) {
        this.status = status;
        this.rows = List.copyOf(rows);
        this.message = message;
    }

    public static SpaceUsageReportResult success(
            List<SpaceUsageReportRow> rows
    ) {
        if (rows == null) {
            throw new IllegalArgumentException(
                    "Usage report rows cannot be null"
            );
        }

        if (rows.isEmpty()) {
            return new SpaceUsageReportResult(
                    Status.EMPTY,
                    List.of(),
                    "No spaces are available."
            );
        }

        return new SpaceUsageReportResult(
                Status.SUCCESS,
                rows,
                ""
        );
    }

    public static SpaceUsageReportResult unauthorized(String message) {
        return new SpaceUsageReportResult(
                Status.UNAUTHORIZED,
                List.of(),
                message
        );
    }

    public static SpaceUsageReportResult invalidDateRange(String message) {
        return new SpaceUsageReportResult(
                Status.INVALID_DATE_RANGE,
                List.of(),
                message
        );
    }

    public Status getStatus() {
        return status;
    }

    public List<SpaceUsageReportRow> getRows() {
        return rows;
    }

    public String getMessage() {
        return message;
    }

    public boolean isSuccessful() {
        return status == Status.SUCCESS;
    }
}

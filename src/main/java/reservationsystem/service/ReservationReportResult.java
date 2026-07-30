package reservationsystem.service;

import java.util.List;

public class ReservationReportResult {
    public enum Status {
        SUCCESS,
        EMPTY,
        UNAUTHORIZED,
        INVALID_DATE_RANGE
    }

    private final Status status;
    private final String message;
    private final List<ReservationReportEntry> entries;

    private ReservationReportResult(
            Status status,
            String message,
            List<ReservationReportEntry> entries
    ) {
        this.status = status;
        this.message = message;
        this.entries = List.copyOf(entries);
    }

    public static ReservationReportResult success(List<ReservationReportEntry> entries) {
        if (entries == null) {
            throw new IllegalArgumentException("Report entries cannot be null");
        }

        if (entries.isEmpty()) {
            return new ReservationReportResult(
                    Status.EMPTY,
                    "No reservations found",
                    entries
            );
        }

        return new ReservationReportResult(
                Status.SUCCESS,
                "Reservations found",
                entries
        );
    }

    public static ReservationReportResult unauthorized(String message) {
        return new ReservationReportResult(
                Status.UNAUTHORIZED,
                message,
                List.of()
        );
    }

    public static ReservationReportResult invalidDateRange(String message) {
        return new ReservationReportResult(
                Status.INVALID_DATE_RANGE,
                message,
                List.of()
        );
    }

    public Status getStatus() {
        return status;
    }

    public boolean isSuccessful() {
        return status == Status.SUCCESS || status == Status.EMPTY;
    }

    public String getMessage() {
        return message;
    }

    public List<ReservationReportEntry> getEntries() {
        return entries;
    }
}

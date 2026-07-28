package reservationsystem.service;

import java.util.List;

public class ReservationReportResult {
    private final boolean successful;
    private final String message;
    private final List<ReservationReportEntry> entries;

    private ReservationReportResult(
            boolean successful,
            String message,
            List<ReservationReportEntry> entries
    ) {
        this.successful = successful;
        this.message = message;
        this.entries = List.copyOf(entries);
    }

    public static ReservationReportResult success(List<ReservationReportEntry> entries) {
        if (entries == null) {
            throw new IllegalArgumentException("Report entries cannot be null");
        }

        String message = entries.isEmpty()
                ? "No reservations found"
                : "Reservations found";

        return new ReservationReportResult(true, message, entries);
    }

    public static ReservationReportResult unauthorized(String message) {
        return new ReservationReportResult(false, message, List.of());
    }

    public boolean isSuccessful() {
        return successful;
    }

    public String getMessage() {
        return message;
    }

    public List<ReservationReportEntry> getEntries() {
        return entries;
    }
}

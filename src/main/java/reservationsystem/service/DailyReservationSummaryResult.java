package reservationsystem.service;

import reservationsystem.model.DailyReservationSummary;

import java.util.List;

public class DailyReservationSummaryResult {

    public enum Status {
        SUCCESS,
        EMPTY
    }

    private final Status status;
    private final List<DailyReservationSummary> summaries;
    private final String message;

    private DailyReservationSummaryResult(
            Status status,
            List<DailyReservationSummary> summaries,
            String message
    ) {
        this.status = status;
        this.summaries = List.copyOf(summaries);
        this.message = message;
    }

    public static DailyReservationSummaryResult from(
            List<DailyReservationSummary> summaries
    ) {
        if (summaries == null) {
            throw new IllegalArgumentException(
                    "Daily summaries cannot be null"
            );
        }

        if (summaries.isEmpty()) {
            return new DailyReservationSummaryResult(
                    Status.EMPTY,
                    List.of(),
                    "No reservations found."
            );
        }

        return new DailyReservationSummaryResult(
                Status.SUCCESS,
                summaries,
                ""
        );
    }

    public Status getStatus() {
        return status;
    }

    public List<DailyReservationSummary> getSummaries() {
        return summaries;
    }

    public String getMessage() {
        return message;
    }

    public boolean isEmpty() {
        return status == Status.EMPTY;
    }
}

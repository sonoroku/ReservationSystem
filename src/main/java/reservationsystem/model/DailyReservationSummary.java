package reservationsystem.model;

import java.time.LocalDate;
import java.util.List;

public class DailyReservationSummary {

    private final LocalDate date;
    private final List<Reservation> reservations;

    public DailyReservationSummary(
            LocalDate date,
            List<Reservation> reservations
    ) {
        if (date == null) {
            throw new IllegalArgumentException(
                    "Summary date cannot be null"
            );
        }

        if (reservations == null) {
            throw new IllegalArgumentException(
                    "Summary reservations cannot be null"
            );
        }

        this.date = date;
        this.reservations = List.copyOf(reservations);
    }

    public LocalDate getDate() {
        return date;
    }

    public List<Reservation> getReservations() {
        return reservations;
    }

    public int getReservationCount() {
        return reservations.size();
    }
}

package reservationsystem.model;

public class SpaceUsageReportRow {

    private final Space space;
    private final int reservationCount;

    public SpaceUsageReportRow(
            Space space,
            int reservationCount
    ) {
        if (space == null) {
            throw new IllegalArgumentException(
                    "Report space cannot be null"
            );
        }

        if (reservationCount < 0) {
            throw new IllegalArgumentException(
                    "Reservation count cannot be negative"
            );
        }

        this.space = space;
        this.reservationCount = reservationCount;
    }

    public Space getSpace() {
        return space;
    }

    public int getReservationCount() {
        return reservationCount;
    }
}

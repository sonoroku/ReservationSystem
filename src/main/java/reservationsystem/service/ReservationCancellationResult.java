package reservationsystem.service;

public class ReservationCancellationResult {
	private final boolean successful;
    private final String message;

    private ReservationCancellationResult(boolean successful, String message) {
        this.successful = successful;
        this.message = message;
    }

    public static ReservationCancellationResult success() {
        return new ReservationCancellationResult(true, "Reservation cancelled successfully");
    }

    public static ReservationCancellationResult notFound() {
        return new ReservationCancellationResult(false, "Reservation was not found");
    }

    public static ReservationCancellationResult notOwned() {
        return new ReservationCancellationResult(false, "Reservation does not belong to current user");
    }

    public boolean isSuccessful() {
        return successful;
    }

    public String getMessage() {
        return message;
    }

}

package reservationsystem.service;

public class AdminReservationCancellationResult {

    public enum Status {
        SUCCESS,
        UNAUTHORIZED,
        NOT_FOUND
    }

    private final Status status;
    private final String message;

    private AdminReservationCancellationResult(
            Status status,
            String message
    ) {
        this.status = status;
        this.message = message;
    }

    public static AdminReservationCancellationResult success() {
        return new AdminReservationCancellationResult(
                Status.SUCCESS,
                "Reservation cancelled successfully"
        );
    }

    public static AdminReservationCancellationResult unauthorized(
            String message
    ) {
        return new AdminReservationCancellationResult(
                Status.UNAUTHORIZED,
                message
        );
    }

    public static AdminReservationCancellationResult notFound() {
        return new AdminReservationCancellationResult(
                Status.NOT_FOUND,
                "Reservation was not found"
        );
    }

    public Status getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public boolean isSuccessful() {
        return status == Status.SUCCESS;
    }
}

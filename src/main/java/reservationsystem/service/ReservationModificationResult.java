package reservationsystem.service;

public class ReservationModificationResult {

    public enum Status {
        SUCCESS,
        VALIDATION_FAILED,
        NOT_FOUND,
        NOT_OWNED
    }

    private final Status status;
    private final String message;

    private ReservationModificationResult(Status status, String message) {
        this.status = status;
        this.message = message;
    }

    public static ReservationModificationResult success() {
        return new ReservationModificationResult(
                Status.SUCCESS,
                "Reservation modified successfully"
        );
    }

    public static ReservationModificationResult validationFailed(String message) {
        return new ReservationModificationResult(Status.VALIDATION_FAILED, message);
    }

    public static ReservationModificationResult notFound() {
        return new ReservationModificationResult(
                Status.NOT_FOUND,
                "Reservation was not found"
        );
    }

    public static ReservationModificationResult notOwned() {
        return new ReservationModificationResult(
                Status.NOT_OWNED,
                "Reservation does not belong to current user"
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

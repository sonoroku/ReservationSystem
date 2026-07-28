package reservationsystem.service;

import reservationsystem.model.Reservation;

public class AdminReservationModificationResult {
	
	public enum Status {
        SUCCESS,
        UNAUTHORIZED,
        NOT_FOUND,
        VALIDATION_FAILED
    }

    private final Status status;
    private final String message;
    private final Reservation updatedReservation;

    private AdminReservationModificationResult(
            Status status,
            String message,
            Reservation updatedReservation
    ) {
        this.status = status;
        this.message = message;
        this.updatedReservation = updatedReservation;
    }

    public static AdminReservationModificationResult success(
            Reservation updatedReservation
    ) {
        return new AdminReservationModificationResult(
                Status.SUCCESS,
                "Reservation modified successfully",
                updatedReservation
        );
    }

    public static AdminReservationModificationResult unauthorized(
            String message
    ) {
        return new AdminReservationModificationResult(
                Status.UNAUTHORIZED,
                message,
                null
        );
    }

    public static AdminReservationModificationResult notFound() {
        return new AdminReservationModificationResult(
                Status.NOT_FOUND,
                "Reservation was not found",
                null
        );
    }

    public static AdminReservationModificationResult validationFailed(
            String message
    ) {
        return new AdminReservationModificationResult(
                Status.VALIDATION_FAILED,
                message,
                null
        );
    }

    public Status getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public Reservation getUpdatedReservation() {
        return updatedReservation;
    }

    public boolean isSuccessful() {
        return status == Status.SUCCESS;
    }

}

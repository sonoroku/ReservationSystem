package reservationsystem.service;

import reservationsystem.model.Reservation;

public class AdminReservationCreationResult {
	
	public enum Status {
        SUCCESS,
        INVALID_USER,
        UNAUTHORIZED,
        VALIDATION_FAILED
    }

    private final Status status;
    private final String message;
    private final Reservation createdReservation;

    private AdminReservationCreationResult(
            Status status,
            String message,
            Reservation createdReservation
    ) {
        this.status = status;
        this.message = message;
        this.createdReservation = createdReservation;
    }

    public static AdminReservationCreationResult success(
            Reservation reservation
    ) {
        return new AdminReservationCreationResult(
                Status.SUCCESS,
                "Reservation created successfully",
                reservation
        );
    }

    public static AdminReservationCreationResult invalidUser() {
        return new AdminReservationCreationResult(
                Status.INVALID_USER,
                "Selected user was not found",
                null
        );
    }

    public static AdminReservationCreationResult unauthorized(
            String message
    ) {
        return new AdminReservationCreationResult(
                Status.UNAUTHORIZED,
                message,
                null
        );
    }

    public static AdminReservationCreationResult validationFailed(
            String message
    ) {
        return new AdminReservationCreationResult(
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

    public Reservation getCreatedReservation() {
        return createdReservation;
    }

    public boolean isSuccessful() {
        return status == Status.SUCCESS;
    }

}

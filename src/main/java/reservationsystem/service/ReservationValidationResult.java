package reservationsystem.service;

public class ReservationValidationResult {
	
	private final boolean valid;
    private final String message;

    private ReservationValidationResult(boolean valid, String message) {
        this.valid = valid;
        this.message = message;
    }

    public static ReservationValidationResult valid() {
        return new ReservationValidationResult(true, "Reservation is valid");
    }

    public static ReservationValidationResult invalid(String message) {
        return new ReservationValidationResult(false, message);
    }

    public boolean isValid() {
        return valid;
    }

    public String getMessage() {
        return message;
    }

}

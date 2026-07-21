package reservationsystem.service;

public class RegistrationResult {
	
	 private final boolean successful;
	    private final String message;

	    private RegistrationResult(boolean successful, String message) {
	        this.successful = successful;
	        this.message = message;
	    }

	    public static RegistrationResult success() {
	        return new RegistrationResult(
	                true,
	                "Account registered successfully"
	        );
	    }

	    public static RegistrationResult failure(String message) {
	        return new RegistrationResult(false, message);
	    }

	    public boolean isSuccessful() {
	        return successful;
	    }

	    public String getMessage() {
	        return message;
	    }

}

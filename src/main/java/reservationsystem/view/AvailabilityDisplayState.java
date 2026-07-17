package reservationsystem.view;

public class AvailabilityDisplayState {
	
	private final String statusText;
    private final String styleClass;

    public AvailabilityDisplayState(String statusText, String styleClass) {
        this.statusText = statusText;
        this.styleClass = styleClass;
    }

    public String getStatusText() {
        return statusText;
    }

    public String getStyleClass() {
        return styleClass;
    }
}



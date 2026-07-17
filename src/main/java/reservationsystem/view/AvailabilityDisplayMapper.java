package reservationsystem.view;

import reservationsystem.model.TimeSlot;

public class AvailabilityDisplayMapper {
	
	private static final String RESERVED_TEXT = "Reserved";
    private static final String AVAILABLE_TEXT = "Available";

    private static final String RESERVED_STYLE_CLASS = "availability-slot-reserved";
    private static final String AVAILABLE_STYLE_CLASS = "availability-slot-available";

    public AvailabilityDisplayState map(TimeSlot timeSlot) {
        if (timeSlot == null) {
            throw new IllegalArgumentException("Time slot cannot be null");
        }

        if (timeSlot.isReserved()) {
            return new AvailabilityDisplayState(RESERVED_TEXT, RESERVED_STYLE_CLASS);
        }

        return new AvailabilityDisplayState(AVAILABLE_TEXT, AVAILABLE_STYLE_CLASS);
    }

}

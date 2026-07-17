package reservationsystem.model;

import java.time.LocalDate;
import java.util.List;

public class DatedAvailability {
	
	private final LocalDate date;
    private final List<TimeSlot> timeSlots;

    public DatedAvailability(LocalDate date, List<TimeSlot> timeSlots) {
        if (date == null) {
            throw new IllegalArgumentException("Date cannot be null");
        }

        if (timeSlots == null) {
            throw new IllegalArgumentException("Time slots cannot be null");
        }

        this.date = date;
        this.timeSlots = timeSlots;
    }

    public LocalDate getDate() {
        return date;
    }

    public List<TimeSlot> getTimeSlots() {
        return timeSlots;
    }

}

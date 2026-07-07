package reservationsystem.model;

import java.time.LocalDate;
import java.time.LocalTime;

public class Reservation {
	
	private final int id;
    private final int spaceId;
    private final String userId;
    private final LocalDate date;
    private final LocalTime startTime;
    private final LocalTime endTime;

    public Reservation(int id, int spaceId, String userId, LocalDate date,
                       LocalTime startTime, LocalTime endTime) {
        this.id = id;
        this.spaceId = spaceId;
        this.userId = userId;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public int getId() {
        return id;
    }

    public int getSpaceId() {
        return spaceId;
    }

    public String getUserId() {
        return userId;
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

}

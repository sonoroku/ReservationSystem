package reservationsystem.model;

import java.time.LocalDateTime;

public class Reservation {
	
	 private final int id;
	    private final int spaceId;
	    private final String reserverName;
	    private final String purpose;
	    private final LocalDateTime startTime;
	    private final LocalDateTime endTime;

	    public Reservation(int id, int spaceId, String reserverName, String purpose,
	                       LocalDateTime startTime, LocalDateTime endTime) {
	        this.id = id;
	        this.spaceId = spaceId;
	        this.reserverName = reserverName;
	        this.purpose = purpose;
	        this.startTime = startTime;
	        this.endTime = endTime;
	    }

	    public int getId() {
	        return id;
	    }

	    public int getSpaceId() {
	        return spaceId;
	    }

	    public String getReserverName() {
	        return reserverName;
	    }

	    public String getPurpose() {
	        return purpose;
	    }

	    public LocalDateTime getStartTime() {
	        return startTime;
	    }

	    public LocalDateTime getEndTime() {
	        return endTime;
	    }

}

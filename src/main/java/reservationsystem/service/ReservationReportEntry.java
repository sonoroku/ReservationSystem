package reservationsystem.service;

import java.time.LocalDate;
import java.time.LocalTime;

public class ReservationReportEntry {
    private final int reservationId;
    private final int spaceId;
    private final String spaceName;
    private final String building;
    private final String userId;
    private final LocalDate date;
    private final LocalTime startTime;
    private final LocalTime endTime;

    public ReservationReportEntry(
            int reservationId,
            int spaceId,
            String spaceName,
            String building,
            String userId,
            LocalDate date,
            LocalTime startTime,
            LocalTime endTime
    ) {
        this.reservationId = reservationId;
        this.spaceId = spaceId;
        this.spaceName = spaceName;
        this.building = building;
        this.userId = userId;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public int getReservationId() {
        return reservationId;
    }

    public int getSpaceId() {
        return spaceId;
    }

    public String getSpaceName() {
        return spaceName;
    }

    public String getBuilding() {
        return building;
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

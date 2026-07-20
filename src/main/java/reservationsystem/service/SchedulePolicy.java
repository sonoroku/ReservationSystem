package reservationsystem.service;

import java.time.LocalTime;

public class SchedulePolicy {
	
	public static final LocalTime DAY_START_TIME = LocalTime.of(8, 0);
    public static final LocalTime DAY_END_TIME = LocalTime.of(20, 0);

    public static final int TIME_INCREMENT_MINUTES = 30;
    public static final int MAX_RESERVATION_MINUTES = 120;
    public static final int BUFFER_MINUTES = 10;

    private SchedulePolicy() {
    }

}

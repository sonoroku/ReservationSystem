package reservationsystem.model;

import java.time.LocalTime;

public class TimeSlot {
  private final LocalTime startTime;
  private final LocalTime endTime;
  private boolean reserved;
  private Reservation reservation;

public TimeSlot(LocalTime startTime, LocalTime endTime) {
  if (startTime == null) {
    throw new IllegalArgumentException("Start time cannot be null");
  }

  if (endTime == null) {
    throw new IllegalArgumentException("End time cannot be null");
  }

  if (!startTime.isBefore(endTime)) {
    throw new IllegalArgumentException("Start time must be before end time");
  }

  this.startTime = startTime;
  this.endTime = endTime;
  this.reserved = false;
  this.reservation = null;
}
  public LocalTime getStartTime() {
    return startTime;
  }

public LocalTime getEndTime () {
  return endTime;
}

public boolean isReserved() {
  return reserved;
}

public boolean isAvailable() {
  return !reserved;
}

public Reservation getReservation() {
  return reservation;
}

public void markReserved(Reservation reservation) {
  if (reservation == null) {
    throw new IllegalArgumentException("Reservation cannot be null");
  }

  this.reserved = true;
  this.reservation = reservation;
}
  public void markAvailable() {
    this.reserved = false;
    this.reservation = null;
  }

public boolean overlaps(LocalTime otherStartTime, LocalTime otherEndTime) {
  if (otherStartTime == null || otherEndTime == null) {
    throw new IllegalArgumentException("Times cannot be null");
  }

  return startTime.isBefore(otherEndTime) && otherStartTime.isBefore(endTime);
}
}

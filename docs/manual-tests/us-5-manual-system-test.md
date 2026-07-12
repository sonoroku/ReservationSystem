# US-5 Manual System Test: View Day Availability

## User Story

As a student, I want to view a space's availability for a selected day so that I can choose an open time slot.

## Test Environment

- Application: ReservationSystem JavaFX app
- Command used to run app: `.\mvnw.cmd javafx:run`
- Tester: zb
- Date tested: July 11, 2026

## Test Case 1: Availability Schedule Displays 8 AM to 8 PM

### Steps

1. Run the application using `.\mvnw.cmd javafx:run`.
2. Open the availability view.
3. Select a space.
4. Select a date.
5. Load the availability schedule.
6. Observe the displayed time slots.

### Expected Result

The application displays availability from 8:00 AM to 8:00 PM in 30-minute slots.

### Actual Result

The application displayed the availability schedule from 8:00 AM to 8:00 PM in 30-minute slots.

### Status

Pass

## Test Case 2: Existing Reservations Mark Slots Reserved

### Steps

1. Run the application using `.\mvnw.cmd javafx:run`.
2. Open the availability view.
3. Select a space that has an existing reservation in `reservations.json`.
4. Select the matching reservation date.
5. Load the availability schedule.
6. Observe the time slots that overlap the reservation.

### Expected Result

Slots that overlap an existing reservation are displayed as reserved.

### Actual Result

The application displayed the matching reserved slots for the selected space and date.

### Status

Pass

## Test Case 3: Remaining Slots Display Available

### Steps

1. Run the application using `.\mvnw.cmd javafx:run`.
2. Open the availability view.
3. Select a space and date with a partial reservation.
4. Load the availability schedule.
5. Observe slots before and after the reserved time.

### Expected Result

Slots that do not overlap an existing reservation are displayed as available.

### Actual Result

The application displayed non-reserved time slots as available.

### Status

Pass

## Bugs Found

No bugs found during manual testing.

## Follow-Up Issues

No follow-up issues were created.

## Demo Readiness

US-5 availability display is ready to demonstrate in the Sprint 1 video.
# US-8 Manual System Test: Create Reservation

## User Story

As a student, I want to create a reservation for an available space so that I can reserve a campus space for a specific date and time.

## Test Environment

- Application: ReservationSystem JavaFX app
- Command used to run app: `.\mvnw.cmd javafx:run`
- Sprint 1 active user: `student` (assigned automatically; no user ID input)
- Tester: ao
- Date tested: July 11, 2026

## Test Case 1: Valid Input Creates and Stores a Reservation

### Steps

1. Run the application using `.\mvnw.cmd javafx:run`.
2. Open the Create Reservation tab.
3. Select a space.
4. Select a date.
5. Enter a valid start time.
6. Enter a valid end time.
7. Click the create reservation button.
8. Open the Availability tab.
9. Select the same space and date.
10. Load the availability schedule.
11. Confirm the new reservation time appears as reserved.

### Example Test Data

- Space: Student Union Conference Room 1
- Date: 2026-07-08
- Start Time: 16:00
- End Time: 17:00

### Expected Result

The application creates the reservation, displays a success message, stores the reservation, and shows the reserved time in the availability view.

### Actual Result

The application created the reservation successfully and displayed the reserved time in the availability view.

### Status

Pass

## Test Case 2: Overlapping Reservation Is Rejected

### Steps

1. Run the application using `.\mvnw.cmd javafx:run`.
2. Open the Create Reservation tab.
3. Select a space that already has a reservation.
4. Select the matching reservation date.
5. Enter a start time and end time that overlap an existing reservation.
6. Click the create reservation button.

### Example Test Data

- Space: Student Union Conference Room 1
- Date: 2026-07-08
- Start Time: 09:30
- End Time: 10:30

### Expected Result

The application rejects the reservation and displays a clear conflict message.

### Actual Result

The application rejected the reservation and displayed a conflict message.

### Status

Pass

## Test Case 3: End Time Before Start Time Is Rejected

### Steps

1. Run the application using `.\mvnw.cmd javafx:run`.
2. Open the Create Reservation tab.
3. Select a space.
4. Select a date.
5. Enter a start time that is after the end time.
6. Click the create reservation button.

### Example Test Data

- Space: Student Union Conference Room 1
- Date: 2026-07-08
- Start Time: 11:00
- End Time: 10:00

### Expected Result

The application rejects the reservation and displays a validation message explaining that the end time must be after the start time.

### Actual Result

The application rejected the reservation and displayed a validation message.

### Status

Pass

## Test Case 4: Reservation Longer Than 2 Hours Is Rejected

### Steps

1. Run the application using `.\mvnw.cmd javafx:run`.
2. Open the Create Reservation tab.
3. Select a space.
4. Select a date.
5. Enter a start time and end time more than 2 hours apart.
6. Click the create reservation button.

### Example Test Data

- Space: Student Union Conference Room 1
- Date: 2026-07-08
- Start Time: 13:00
- End Time: 15:01

### Expected Result

The application rejects the reservation and displays a validation message explaining that reservations cannot be longer than 2 hours.

### Actual Result

The application rejected the reservation and displayed a validation message.

### Status

Pass

## Test Case 5: Reservation Less Than 10 Minutes Away Is Rejected

### Steps

1. Run the application using `.\mvnw.cmd javafx:run`.
2. Open the Create Reservation tab.
3. Select a space that already has a reservation.
4. Select the matching reservation date.
5. Enter a start time that is less than 10 minutes after an existing reservation ends.
6. Click the create reservation button.

### Example Test Data

- Existing Reservation: 09:00 to 10:00
- Space: Student Union Conference Room 1
- Date: 2026-07-08
- Start Time: 10:05
- End Time: 11:00

### Expected Result

The application rejects the reservation and displays a validation message explaining that reservations must be at least 10 minutes away from another reservation.

### Actual Result

The application rejected the reservation and displayed a validation message.

### Status

Pass

## Bugs Found

No bugs found during manual testing.

## Follow-Up Issues

No follow-up issues were created.

## Demo Readiness

US-8 create reservation workflow is ready to demonstrate in the Sprint 1 video.

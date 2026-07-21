# US-12 Manual System Test: Suggest Available Times

## User Story

As a student, I want the system to suggest available reservation times so that I can quickly choose an open time slot.

## Test Environment

- Application: ReservationSystem JavaFX app
- Command used to run app: `.\mvnw.cmd javafx:run`
- Tester: Almondmlk
- Date tested: [Add date]

## Test Case 1: Suggest Times With Available Slots

### Steps

1. Run the application using `.\mvnw.cmd javafx:run`.
2. Open the Create Reservation tab.
3. Select a space.
4. Select a date.
5. Enter a valid duration in minutes.
6. Click Suggest Times.
7. Observe the suggested time list.

### Expected Result

The application displays available time suggestions in chronological order.

### Actual Result

The application displayed available time suggestions in chronological order.

### Status

Pass

## Test Case 2: Selecting a Suggested Time Populates Start and End Fields

### Steps

1. Run the application using `.\mvnw.cmd javafx:run`.
2. Open the Create Reservation tab.
3. Select a space.
4. Select a date.
5. Enter a valid duration in minutes.
6. Click Suggest Times.
7. Select one of the suggested times.

### Expected Result

The selected suggestion fills in the existing start time and end time fields without creating a reservation.

### Actual Result

The selected suggestion filled in the start time and end time fields without saving a reservation.

### Status

Pass

## Test Case 3: Partial Availability Avoids Reserved Times

### Steps

1. Run the application using `.\mvnw.cmd javafx:run`.
2. Open the Create Reservation tab.
3. Select a space and date that already have an existing reservation in `reservations.json`.
4. Enter a valid duration in minutes.
5. Click Suggest Times.
6. Observe the suggested times.

### Expected Result

The application only displays suggested times that do not overlap existing reservations.

### Actual Result

The application displayed only suggested times that did not overlap existing reservations.

### Status

Pass

## Test Case 4: Suggestions Respect Conflicts and Buffer Rules

### Steps

1. Run the application using `.\mvnw.cmd javafx:run`.
2. Open the Create Reservation tab.
3. Select a space and date with an existing reservation.
4. Enter a duration that would create suggestions near the existing reservation.
5. Click Suggest Times.
6. Check whether the suggestions avoid conflicting or too-close times.

### Expected Result

The application does not suggest times that overlap an existing reservation or violate the required buffer rule.

### Actual Result

The application did not suggest times that conflicted with an existing reservation or violated the buffer rule.

### Status

Pass

## Test Case 5: Duration Boundary

### Steps

1. Run the application using `.\mvnw.cmd javafx:run`.
2. Open the Create Reservation tab.
3. Select a space.
4. Select a date.
5. Enter a valid duration, such as `60`.
6. Click Suggest Times.

### Expected Result

The application displays suggestions that match the requested duration and stay within the available reservation hours.

### Actual Result

The application displayed suggestions that matched the requested duration and stayed within the reservation schedule.

### Status

Pass

## Test Case 6: Opening and Closing Boundary

### Steps

1. Run the application using `.\mvnw.cmd javafx:run`.
2. Open the Create Reservation tab.
3. Select a space.
4. Select a date.
5. Enter a duration that fits within the schedule.
6. Click Suggest Times.
7. Check the earliest and latest suggested times.

### Expected Result

The application only suggests times within the allowed schedule and does not suggest times before opening or after closing.

### Actual Result

The application only displayed suggestions within the allowed schedule.

### Status

Pass

## Test Case 7: No Valid Times Available

### Steps

1. Run the application using `.\mvnw.cmd javafx:run`.
2. Open the Create Reservation tab.
3. Select a space and date where no valid suggestions are available, or enter a duration that cannot fit.
4. Click Suggest Times.
5. Observe the message shown by the application.

### Expected Result

The application displays a no-times or empty-state message.

### Actual Result

The application displayed a message showing that no available times were found.

### Status

Pass

## Test Case 8: Invalid Duration Input

### Steps

1. Run the application using `.\mvnw.cmd javafx:run`.
2. Open the Create Reservation tab.
3. Select a space.
4. Select a date.
5. Enter invalid duration input, such as letters or a blank value.
6. Click Suggest Times.

### Expected Result

The application does not load suggestions and displays a validation message.

### Actual Result

The application displayed a validation message and did not load suggestions.

### Status

Pass

## Test Case 9: Create Reservation Still Works After Using Suggestions

### Steps

1. Run the application using `.\mvnw.cmd javafx:run`.
2. Open the Create Reservation tab.
3. Select a space.
4. Select a date.
5. Enter a valid duration.
6. Click Suggest Times.
7. Select a suggested time.
8. Click Create Reservation.

### Expected Result

The application creates the reservation successfully after the suggested start and end times are populated.

### Actual Result

The application created the reservation successfully after selecting a suggested time.

### Status

Pass

## Bugs Found

No bugs found during manual testing.

## Follow-Up Issues

No follow-up issues were created.

## Demo Readiness

US-12 available-time suggestions are ready to demonstrate in the Sprint 1 video.

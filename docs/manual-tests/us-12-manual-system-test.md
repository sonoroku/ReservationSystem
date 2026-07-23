# US-12 Manual System Test: Suggest Available Times

## User Story

As a student, I want the system to suggest available reservation times so that I can quickly choose an open time slot.

## Test Environment

- Application: ReservationSystem JavaFX app
- Command used to run app: `.\mvnw.cmd javafx:run`
- Tester: Almondmlk
- Date tested: July 22, 2026
- Tested branch: main
- Tested commit/SHA: ceaf2b5

## Test Case 1: Suggest Times With Available Slots

### Inputs Used

- Space: Nevins Hall Computer Lab
- Date: 2026-07-08
- Duration: 60 minutes

### Steps

1. Ran the application using `.\mvnw.cmd javafx:run`.
2. Opened the Create Reservation tab.
3. Selected `Nevins Hall Computer Lab`.
4. Selected `2026-07-08`.
5. Entered `60` in the duration field.
6. Clicked Suggest Times.
7. Observed the suggested available times.

### Expected Result

The application displays available 60-minute time suggestions in chronological order.

### Actual Result

The application displayed available 60-minute suggestions in chronological order, including `08:00 - 09:00`, `08:30 - 09:30`, `09:00 - 10:00`, `09:30 - 10:30`, `10:00 - 11:00`, and `10:30 - 11:30`.

### Status

Pass

## Test Case 2: Selecting a Suggested Time Populates Start and End Fields

### Inputs Used

- Space: Nevins Hall Computer Lab
- Date: 2026-07-08
- Duration: 60 minutes
- Selected suggestion: 09:00 - 10:00

### Steps

1. Ran the application using `.\mvnw.cmd javafx:run`.
2. Opened the Create Reservation tab.
3. Selected `Nevins Hall Computer Lab`.
4. Selected `2026-07-08`.
5. Entered `60` in the duration field.
6. Clicked Suggest Times.
7. Selected `09:00 - 10:00` from the suggested available times list.
8. Checked the start time and end time fields.

### Expected Result

The selected suggestion fills in the existing start time and end time fields without creating a reservation.

### Actual Result

Selecting `09:00 - 10:00` filled the start time field with `09:00` and the end time field with `10:00`. The application displayed the message `Suggested time selected. Review the times, then create the reservation.` No reservation was created from selecting the suggestion alone.

### Status

Pass

## Test Case 3: Partial Availability Avoids Reserved Times

### Inputs Used

- Space: Nevins Hall Computer Lab
- Date: 2026-07-08
- Duration: 60 minutes
- Existing reservation checked against: reservation data in `reservations.json`

### Steps

1. Ran the application using `.\mvnw.cmd javafx:run`.
2. Opened the Create Reservation tab.
3. Selected `Nevins Hall Computer Lab`.
4. Selected `2026-07-08`.
5. Entered `60` in the duration field.
6. Clicked Suggest Times.
7. Observed the suggested available times and checked that suggestions did not overlap reserved times.

### Expected Result

The application only displays suggested times that do not overlap existing reservations.

### Actual Result

The application displayed suggested times for `Nevins Hall Computer Lab` on `2026-07-08` and did not show suggestions that conflicted with existing reservations for the selected space and date.

### Status

Pass

## Test Case 4: Suggestions Respect Conflicts and Buffer Rules

### Inputs Used

- Space: Nevins Hall Computer Lab
- Date: 2026-07-08
- Duration: 60 minutes
- Existing reservation data source: `reservations.json`

### Steps

1. Ran the application using `.\mvnw.cmd javafx:run`.
2. Opened the Create Reservation tab.
3. Selected `Nevins Hall Computer Lab`.
4. Selected `2026-07-08`.
5. Entered `60` in the duration field.
6. Clicked Suggest Times.
7. Checked whether the suggestions avoided conflicting or too-close times.

### Expected Result

The application does not suggest times that overlap an existing reservation or violate the required buffer rule.

### Actual Result

The application displayed available suggestions and did not suggest times that conflicted with existing reservation data or violated the required buffer rule.

### Status

Pass

## Test Case 5: Duration Boundary

### Inputs Used

- Space: Nevins Hall Computer Lab
- Date: 2026-07-08
- Duration: 60 minutes

### Steps

1. Ran the application using `.\mvnw.cmd javafx:run`.
2. Opened the Create Reservation tab.
3. Selected `Nevins Hall Computer Lab`.
4. Selected `2026-07-08`.
5. Entered `60` in the duration field.
6. Clicked Suggest Times.
7. Checked that the suggested times matched the requested duration.

### Expected Result

The application displays suggestions that match the requested duration and stay within the available reservation hours.

### Actual Result

The application displayed 60-minute suggestions such as `08:00 - 09:00`, `08:30 - 09:30`, `09:00 - 10:00`, `09:30 - 10:30`, `10:00 - 11:00`, and `10:30 - 11:30`.

### Status

Pass

## Test Case 6: Opening and Closing Boundary

### Inputs Used

- Space: Nevins Hall Computer Lab
- Date: 2026-07-08
- Duration: 60 minutes
- Reservation schedule checked: 8:00 AM to 8:00 PM

### Steps

1. Ran the application using `.\mvnw.cmd javafx:run`.
2. Opened the Create Reservation tab.
3. Selected `Nevins Hall Computer Lab`.
4. Selected `2026-07-08`.
5. Entered `60` in the duration field.
6. Clicked Suggest Times.
7. Checked the earliest and latest suggested times.

### Expected Result

The application only suggests times within the allowed schedule and does not suggest times before opening or after closing.

### Actual Result

The application displayed suggestions starting within the allowed schedule. The visible suggestions began at `08:00 - 09:00`, and no suggestions before 8:00 AM were shown.

### Status

Pass

## Test Case 7: No Valid Times Available

### Inputs Used

- Space: Nevins Hall Computer Lab
- Date: 2026-07-08
- Duration: 1000 minutes

### Steps

1. Ran the application using `.\mvnw.cmd javafx:run`.
2. Opened the Create Reservation tab.
3. Selected `Nevins Hall Computer Lab`.
4. Selected `2026-07-08`.
5. Entered `1000` in the duration field.
6. Clicked Suggest Times.
7. Observed the message shown by the application.

### Expected Result

The application displays a no-times or empty-state message.

### Actual Result

The application displayed a message showing that no available times were found because the requested duration could not fit within the available schedule.

### Status

Pass

## Test Case 8: Invalid Duration Input

### Inputs Used

- Space: Nevins Hall Computer Lab
- Date: 2026-07-08
- Duration: abc

### Steps

1. Ran the application using `.\mvnw.cmd javafx:run`.
2. Opened the Create Reservation tab.
3. Selected `Nevins Hall Computer Lab`.
4. Selected `2026-07-08`.
5. Entered `abc` in the duration field.
6. Clicked Suggest Times.

### Expected Result

The application does not load suggestions and displays a validation message.

### Actual Result

The application displayed a validation message and did not load time suggestions.

### Status

Pass

## Test Case 9: Blank Duration Input

### Inputs Used

- Space: Nevins Hall Computer Lab
- Date: 2026-07-08
- Duration: blank

### Steps

1. Ran the application using `.\mvnw.cmd javafx:run`.
2. Opened the Create Reservation tab.
3. Selected `Nevins Hall Computer Lab`.
4. Selected `2026-07-08`.
5. Left the duration field blank.
6. Clicked Suggest Times.

### Expected Result

The application does not load suggestions and displays a validation message.

### Actual Result

The application displayed a validation message and did not load time suggestions.

### Status

Pass

## Test Case 10: Create Reservation Still Works After Using Suggestions

### Inputs Used

- Space: Nevins Hall Computer Lab
- Date: 2026-07-08
- Duration: 60 minutes
- Selected suggestion: 09:00 - 10:00
- Start time populated: 09:00
- End time populated: 10:00

### Steps

1. Ran the application using `.\mvnw.cmd javafx:run`.
2. Opened the Create Reservation tab.
3. Selected `Nevins Hall Computer Lab`.
4. Selected `2026-07-08`.
5. Entered `60` in the duration field.
6. Clicked Suggest Times.
7. Selected `09:00 - 10:00`.
8. Confirmed that the start time field showed `09:00`.
9. Confirmed that the end time field showed `10:00`.
10. Clicked Create Reservation.

### Expected Result

The application creates the reservation successfully after the suggested start and end times are populated.

### Actual Result

The selected suggestion populated the start and end time fields. The reservation was not created until the Create Reservation button was clicked.

### Status

Pass

## Bugs Found

No bugs found during manual testing.

## Follow-Up Issues

No follow-up issues were created.

## Demo Readiness

US-12 available-time suggestions are ready to demonstrate in the Sprint 1 video.

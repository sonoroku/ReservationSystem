# US-9 Manual System Test: My Reservations

## User Story

As a student, I want to view my reservations so that I can confirm the spaces and times I have already reserved.

## Test Environment

- Application: ReservationSystem JavaFX app
- Command used to run app: `.\mvnw.cmd javafx:run`
- Tester: Almondmlk
- Date tested: July 19, 2026

## Test Case 1: User With Existing Reservations

### Steps

1. Run the application using `.\mvnw.cmd javafx:run`.
2. Open the My Reservations tab.
3. Enter a user ID that has reservations in `reservations.json`.
4. Click View My Reservations.
5. Observe the displayed reservations.

### Expected Result

The application displays only the reservations that belong to the selected user.

### Actual Result

The application displayed only the selected user's reservations.

### Status

Pass

## Test Case 2: User With No Reservations

### Steps

1. Run the application using `.\mvnw.cmd javafx:run`.
2. Open the My Reservations tab.
3. Enter a user ID that has no reservations.
4. Click View My Reservations.
5. Observe the displayed results.

### Expected Result

The application displays an empty-state message showing that no reservations were found for the selected user.

### Actual Result

The application displayed a message saying no reservations were found for the selected user.

### Status

Pass

## Test Case 3: Reservation Details Display Correctly

### Steps

1. Run the application using `.\mvnw.cmd javafx:run`.
2. Open the My Reservations tab.
3. Enter a user ID with existing reservations.
4. Click View My Reservations.
5. Check the displayed reservation details.

### Expected Result

Each reservation displays the reservation ID, space ID, date, start time, and end time.

### Actual Result

The application displayed the reservation ID, space ID, date, start time, and end time for the selected user's reservations.

### Status

Pass

## Test Case 4: Refresh Loads Reservation Data Again

### Steps

1. Run the application using `.\mvnw.cmd javafx:run`.
2. Open the My Reservations tab.
3. Enter a user ID with existing reservations.
4. Click View My Reservations.
5. Click Refresh.
6. Observe the displayed reservations.

### Expected Result

The application reloads the selected user's reservations from persistence and keeps the reservation list displayed.

### Actual Result

The application refreshed the reservation list and continued displaying the selected user's reservations.

### Status

Pass

## Test Case 5: Blank User ID Shows Validation Message

### Steps

1. Run the application using `.\mvnw.cmd javafx:run`.
2. Open the My Reservations tab.
3. Leave the user ID field blank.
4. Click View My Reservations.
5. Observe the message shown by the application.

### Expected Result

The application does not load reservations and displays a message saying that the user ID is required.

### Actual Result

The application displayed a message saying that the user ID is required.

### Status

Pass

## Test Case 6: Rows Remain Selectable

### Steps

1. Run the application using `.\mvnw.cmd javafx:run`.
2. Open the My Reservations tab.
3. Enter a user ID with existing reservations.
4. Click View My Reservations.
5. Click one of the displayed reservation rows.

### Expected Result

The reservation rows remain selectable for future US-10 or US-11 behavior.

### Actual Result

The reservation rows were selectable in the My Reservations list.

### Status

Pass

## Bugs Found

No bugs found during manual testing.

## Follow-Up Issues

No follow-up issues were created.

## Demo Readiness

US-9 My Reservations is ready to demonstrate in the Sprint 1 video.

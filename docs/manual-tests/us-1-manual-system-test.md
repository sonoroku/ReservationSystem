# US-1 Manual System Test: View All Spaces

## User Story

As a student, I want to view all reservable campus spaces so that I can choose a space for a reservation.

## Test Environment

- Application: ReservationSystem JavaFX app
- Command used to run app: `.\mvnw.cmd javafx:run`
- Tester: zb
- Date tested: July 11, 2026

## Test Case 1: Spaces Exist

### Steps

1. Open the ReservationSystem JavaFX application.
2. Navigate to the space list view.
3. Observe the list of displayed spaces.

### Expected Result

The application displays each available space with its name, building, and capacity.

### Actual Result

The application displayed the available spaces with name, building, and capacity.

### Status

Pass

## Test Case 2: Multiple Spaces Appear Alphabetically

### Steps

1. Open the ReservationSystem JavaFX application.
2. Navigate to the space list view.
3. Compare the order of the displayed space names.

### Expected Result

The spaces are displayed alphabetically by name.

### Actual Result

The spaces appeared alphabetically by name.

### Status

Pass

## Test Case 3: No Spaces Empty State

### Steps

1. Temporarily replace the contents of `src/main/resources/data/spaces.json` with an empty JSON array: `[]`.
2. Run the application using `.\mvnw.cmd javafx:run`.
3. Navigate to the space list view.
4. Observe the message shown when no spaces are available.
5. Restore the original `spaces.json` contents after testing.

### Expected Result

The application displays a clear empty message instead of crashing or showing a blank/broken view.

### Actual Result

The application displayed a clear empty message when no spaces were available.

### Status

Pass

## Bugs Found

No bugs found during manual testing.

## Demo Readiness

US-1 is ready to demonstrate in the Sprint 1 video.
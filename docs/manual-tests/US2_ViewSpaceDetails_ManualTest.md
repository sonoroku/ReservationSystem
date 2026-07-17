# US-2 Manual System Test: View Space Details

## User Story

As a user, I want to view the details of a selected reservable space so that I can determine whether it meets my needs.

## Test Environment

- Application: ReservationSystem JavaFX app
- Commands used to run the app: `.\mvnw.cmd javafx:run` and `.\mvnw.cmd clean javafx:run`
- Manual tester: Alex O'Rourke
- Test setup and documentation assistance: Codex
- Date tested: July 17, 2026

## Test Case 1: Request Details Without Selecting a Space

### Steps

1. Run the application using `.\mvnw.cmd javafx:run`.
2. Open the Spaces tab.
3. Before selecting a table row, click **View Details**.
4. Observe the details message.

### Expected Result

The application displays a visible prompt asking the user to select a space and does not crash.

### Actual Result

The application displayed **Please select a space.** when details were requested before a table row was selected.

### Status

Pass

## Test Case 2: Selected Space Displays Complete Details

### Steps

1. Run the application using `.\mvnw.cmd javafx:run` with the committed starter data.
2. Open the Spaces tab.
3. Select **Student Union Conference Room 1**.
4. Click **View Details**.
5. Compare the displayed values with `src/main/resources/data/spaces.json`.

### Expected Result

The details panel displays the selected space name, building, capacity, and complete feature list.

### Actual Result

The details panel displayed:

- Name: Student Union Conference Room 1
- Building: Student Union
- Capacity: 10
- Features: Interactive Whiteboard, PC, Conference Table, Wi-Fi

### Status

Pass

## Test Case 3: Space With No Features Displays the Empty-Features Message

### Test Setup

The committed starter data did not contain a space with an empty feature list. For this test only, the `features` value for **Odum Library Study Room** was temporarily changed to `[]` in `src/main/resources/data/spaces.json`.

### Steps

1. Temporarily set the Odum Library Study Room feature list to `[]`.
2. Run the application using `.\mvnw.cmd clean javafx:run` so the changed resource is copied into the build output.
3. Open the Spaces tab.
4. Select **Odum Library Study Room**.
5. Click **View Details**.
6. Observe the building, capacity, and features values.
7. Stop the application and restore the original feature list.

### Expected Result

The details panel displays building **Odum Library**, capacity **8**, and **Features: No features available**.

### Actual Result

The details panel displayed building **Odum Library**, capacity **8**, and **Features: No features available**.

### Status

Pass

## Test Data Restoration

The original Odum Library Study Room feature list was restored to:

- Whiteboard
- Study Table
- Wi-Fi

No temporary starter-data changes are included in this manual-test branch.

## Test Notes

The space table does not provide a dedicated clear-selection control. The no-selection scenario was therefore performed before selecting a row. Restarting the application would also restore the initial no-selection state.

## Bugs Found

No bugs were found during manual testing.

## Follow-Up Issues

No follow-up issues were required.

## Demo Readiness

US-2 space-details behavior is ready to demonstrate in the Sprint 1 correction/demo video.

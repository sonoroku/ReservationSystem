# US-6 Manual System Test: Date-Range Availability

## Purpose

Verify that a user can view space availability across an inclusive date range and that the system correctly displays available and reserved time slots without duplicating backend reservation rules in the JavaFX view.

## Test Environment

- Application: ReservationSystem JavaFX app
- Command used to run app: `.\mvnw.cmd javafx:run`
- Tester: zb
- Date tested: July 17, 2026

## Preconditions

- The application launches successfully.
- The Availability view is accessible.
- The committed sample space data is available.
- The committed sample reservation data is available.
- US-6 backend date-range availability logic is implemented.
- US-7 reserved/available styling is active.
- The application uses `app-data/reservations.json` when that runtime file exists; otherwise it seeds the runtime file from the committed starter data.

## Scenario 1: Same-Day Date Range

| Field | Value |
|---|---|
| Selected space | Student Union Conference Room 1 |
| Start date | 2026-07-08 |
| End date | 2026-07-08 |

### Steps

1. Launch the JavaFX application.
2. Open the availability view.
3. Select `Student Union Conference Room 1`.
4. Select `2026-07-08` as the start date.
5. Select `2026-07-08` as the end date.
6. Click `View Range Availability`.

### Expected Result

- The application displays availability for only July 8, 2026.
- Time slots are shown from 8:00 AM to 8:00 PM.
- Reserved and available slots are visually distinct.
- The reservation from 9:00 AM to 10:00 AM is shown as reserved.

### Actual Result

- Availability for July 8, 2026 displayed correctly.
- Time slots appeared in 30-minute increments.
- Reserved and available slots were clearly labeled and styled.
- The 9:00 AM to 10:00 AM reservation appeared as reserved.

### Status

Pass

## Scenario 2: Normal Multi-Day Date Range

| Field | Value |
|---|---|
| Selected space | Student Union Conference Room 1 |
| Start date | 2026-07-08 |
| End date | 2026-07-10 |

### Steps

1. Select `Student Union Conference Room 1`.
2. Select `2026-07-08` as the start date.
3. Select `2026-07-10` as the end date.
4. Click `View Range Availability`.

### Expected Result

- The application displays July 8, July 9, and July 10.
- Dates appear in chronological order.
- Each date contains time slots from 8:00 AM to 8:00 PM.
- Reserved slots are shown only on dates with matching reservations.
- Available slots remain visible for all other times.

### Actual Result

- The application displayed all three dates.
- Dates appeared in chronological order.
- Each date showed the expected schedule.
- Reserved and available slots displayed correctly.

### Status

Pass

## Scenario 3: Seven-Day Maximum Range

| Field | Value |
|---|---|
| Selected space | Student Union Conference Room 1 |
| Start date | 2026-07-08 |
| End date | 2026-07-14 |

### Steps

1. Select `Student Union Conference Room 1`.
2. Select `2026-07-08` as the start date.
3. Select `2026-07-14` as the end date.
4. Click `View Range Availability`.

### Expected Result

- The application accepts the seven-day inclusive range.
- Dates from July 8 through July 14 are displayed.
- Dates appear chronologically.
- Each date shows availability slots.

### Actual Result

- The seven-day range was accepted.
- Dates from July 8 through July 14 displayed correctly.
- Date sections appeared in chronological order.
- Availability slots displayed for each date.

### Status

Pass

## Scenario 4: Reversed Date Range

| Field | Value |
|---|---|
| Selected space | Student Union Conference Room 1 |
| Start date | 2026-07-10 |
| End date | 2026-07-08 |

### Steps

1. Select `Student Union Conference Room 1`.
2. Select `2026-07-10` as the start date.
3. Select `2026-07-08` as the end date.
4. Click `View Range Availability`.

### Expected Result

- The application rejects the reversed date range.
- A clear validation message is displayed.
- No misleading availability schedule is shown.

### Actual Result

- The reversed date range was rejected.
- The validation message `Start date cannot be after end date` was displayed.
- No incorrect schedule was shown.

### Status

Pass

## Scenario 5: Over-Seven-Day Date Range

| Field | Value |
|---|---|
| Selected space | Student Union Conference Room 1 |
| Start date | 2026-07-08 |
| End date | 2026-07-15 |

### Steps

1. Select `Student Union Conference Room 1`.
2. Select `2026-07-08` as the start date.
3. Select `2026-07-15` as the end date.
4. Click `View Range Availability`.

### Expected Result

- The application rejects the date range because it is longer than seven days.
- A clear validation message is displayed.
- No misleading availability schedule is shown.

### Actual Result

- The over-seven-day range was rejected.
- The validation message `Date range cannot be longer than 7 days` was displayed.
- No incorrect schedule was shown.

### Status

Pass

## Scenario 6: Chronological Date Display

| Field | Value |
|---|---|
| Selected space | Student Union Conference Room 1 |
| Start date | 2026-07-08 |
| End date | 2026-07-12 |

### Steps

1. Select `Student Union Conference Room 1`.
2. Select `2026-07-08` as the start date.
3. Select `2026-07-12` as the end date.
4. Click `View Range Availability`.
5. Review the order of displayed date sections.

### Expected Result

- Date sections display in chronological order.
- No dates are skipped within the selected range.

### Actual Result

- Date sections appeared in chronological order.
- No dates were skipped.

### Status

Pass

## Scenario 7: Correct Reserved Slots

| Field | Value |
|---|---|
| Selected space | Student Union Conference Room 1 |
| Start date | 2026-07-08 |
| End date | 2026-07-08 |

### Steps

1. Select `Student Union Conference Room 1`.
2. Select `2026-07-08` as the start date.
3. Select `2026-07-08` as the end date.
4. Click `View Range Availability`.
5. Review the reserved slots.

### Expected Result

- The reservation from 9:00 AM to 10:00 AM appears as reserved.
- Non-overlapping slots remain available.
- Reservation logic matches the existing US-5 daily availability behavior.

### Actual Result

- The 9:00 AM to 10:00 AM reservation appeared as reserved.
- Non-overlapping slots appeared as available.
- Behavior matched the existing daily availability flow.

### Status

Pass

## Scenario 8: Reserved and Available Styling

### Steps

1. Open a date range containing both reserved and available slots.
2. Compare the display of reserved slots against available slots.
3. Confirm the distinction is visible across multiple dates.

### Expected Result

- Reserved and available slots are visually distinct.
- Both states include text labels.
- Meaning does not rely on color alone.
- Styling remains consistent with US-7.

### Actual Result

- Reserved and available slots were visually distinct.
- Both states included text labels.
- The display did not rely only on color.
- Styling matched the US-7 display-state behavior.

### Status

Pass

## Scenario 9: Runtime Data Remains Unchanged

### Steps

1. Launch the application.
2. View availability for a date range containing known sample reservations.
3. Close the application.
4. Launch the application again.
5. View the same date range.
6. Confirm that the availability-only workflow did not create, modify, or cancel any reservation.

### Expected Result

- Reservation data loads correctly after restarting the application.
- Availability results remain consistent between runs.
- Runtime reservation data remains unchanged because this workflow is read-only.

### Actual Result

- Reservation data loaded correctly after restarting the application.
- Availability results remained consistent.
- No reservation create, modify, or cancel action was performed, so no runtime-data restoration was required.

### Status

Pass

## Defects and Follow-Up Work

No defects were found during this manual test.

## Demo Readiness

US-6 date-range availability is ready to demonstrate in the Sprint video. The demo should show a valid same-day range, a multi-day range, a seven-day maximum range, and at least one invalid range such as reversed dates or more than seven days.

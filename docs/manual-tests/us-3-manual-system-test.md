# US-3 Manual System Test: Minimum-Capacity Filtering

## Purpose

Verify that a user can filter the existing Spaces table by minimum capacity, receive clear validation and no-match feedback, clear the filter, and continue using the US-1 space list and US-2 space-details behavior.

## Test Environment

- Application: ReservationSystem JavaFX app
- Command: `.\mvnw.cmd javafx:run`
- Test date: July 18, 2026
- Data source: committed `src/main/resources/data/spaces.json`
- Automated regression command: `.\mvnw.cmd test`
- Automated regression result: 71 tests passed; 0 failures, 0 errors, and 0 skipped

## Preconditions

- Issues #74 and #76 are merged.
- The application launches successfully.
- The Spaces tab is selected.
- The committed starter spaces are available.
- The Spaces tab shows one table with Name, Building, and Capacity columns.

## Starter-Space Baseline

| Space | Capacity |
|---|---:|
| Nevins Hall Computer Lab | 30 |
| Odum Library Study Room | 8 |
| Student Union Conference Room 1 | 10 |
| Student Union Multipurpose Room | 850 |
| University Center North Meeting Room | 40 |

## Scenario 1: Normal Minimum Returns Multiple Spaces

### Steps

1. Enter `30` in the Minimum capacity field.
2. Click `Apply`.

### Expected Result

- Only spaces with capacity greater than or equal to 30 remain.
- Results stay alphabetical.
- No validation message is displayed.

### Actual Result

- The table displayed Nevins Hall Computer Lab (30), Student Union Multipurpose Room (850), and University Center North Meeting Room (40).
- The three results remained alphabetical.
- No validation message was displayed.

### Status

Pass

## Scenario 2: Exact Capacity Match Is Included

### Steps

1. Enter `40` in the Minimum capacity field.
2. Click `Apply`.

### Expected Result

- University Center North Meeting Room, whose capacity is exactly 40, is included.
- Student Union Multipurpose Room is also included because its capacity is greater than 40.
- Lower-capacity spaces are excluded.

### Actual Result

- University Center North Meeting Room (40) and Student Union Multipurpose Room (850) were displayed.
- The exact match was included and all lower-capacity spaces were excluded.

### Status

Pass

## Scenario 3: Valid Minimum With No Matches

### Steps

1. Attempt to choose a valid minimum from 1 through 500 that is greater than every starter-space capacity.
2. Click `Apply`.

### Expected Result

- The existing table becomes empty.
- The message `No spaces match the filter.` is displayed.
- The table shows the no-match placeholder.

### Actual Result

- This scenario could not be produced with the committed system data.
- The largest accepted minimum is 500, while Student Union Multipurpose Room has capacity 850, so at least that space matches every valid minimum.
- The controlled empty result remains covered by the headless `SpaceControllerTest`, but the end-to-end empty UI state is not manually demonstrable with starter data.

### Status

Blocked by test data and validation-range mismatch; follow-up required

## Scenario 4: Nonnumeric Input

### Steps

1. Enter `people` in the Minimum capacity field.
2. Click `Apply`.

### Expected Result

- The current table rows remain unchanged.
- A clear whole-number validation message is displayed.

### Actual Result

- The current table rows remained unchanged.
- The message `Minimum capacity must be a whole number from 1 to 500.` was displayed.

### Status

Pass

## Scenario 5: Negative Input

### Steps

1. Enter `-1` in the Minimum capacity field.
2. Click `Apply`.

### Expected Result

- The current table rows remain unchanged.
- A clear range validation message is displayed.

### Actual Result

- The current table rows remained unchanged.
- The message `Minimum capacity must be between 1 and 500.` was displayed.

### Status

Pass

## Scenario 6: Zero Input

### Steps

1. Enter `0` in the Minimum capacity field.
2. Click `Apply`.

### Expected Result

- The current table rows remain unchanged.
- A clear range validation message is displayed.

### Actual Result

- The current table rows remained unchanged.
- The message `Minimum capacity must be between 1 and 500.` was displayed.

### Status

Pass

## Scenario 7: Input Above 500

### Steps

1. Enter `501` in the Minimum capacity field.
2. Click `Apply`.

### Expected Result

- The current table rows remain unchanged.
- A clear range validation message is displayed.

### Actual Result

- The current table rows remained unchanged.
- The message `Minimum capacity must be between 1 and 500.` was displayed.

### Status

Pass

## Scenario 8: Clear Restores All Spaces

### Steps

1. Enter `40` in the Minimum capacity field.
2. Click `Apply` and confirm that two spaces remain.
3. Click `Clear`.

### Expected Result

- The Minimum capacity field is cleared.
- Filter feedback is cleared.
- All five starter spaces return in alphabetical order.

### Actual Result

- The field and feedback were cleared.
- All five spaces returned in this order: Nevins Hall Computer Lab, Odum Library Study Room, Student Union Conference Room 1, Student Union Multipurpose Room, and University Center North Meeting Room.

### Status

Pass

## Scenario 9: US-1 Spaces-List Regression

### Steps

1. Launch the application and open the Spaces tab.
2. Review the initial rows and columns without applying a filter.

### Expected Result

- The existing Name, Building, and Capacity columns are present.
- All five starter spaces appear in the existing table.
- Rows are alphabetical by space name.
- No second spaces list is present.

### Actual Result

- The three existing columns and all five starter spaces were present.
- Rows were alphabetical.
- Capacity filtering reused the original table; no second list appeared.

### Status

Pass

## Scenario 10: US-2 Selection and Details Regression

### Steps

1. Clear any active filter.
2. Select `Odum Library Study Room`.
3. Click `View Details`.
4. Apply minimum capacity `30`.
5. Select `University Center North Meeting Room`.
6. Click `View Details` again.

### Expected Result

- Before filtering, the selected Odum Library space shows its name, building, capacity 8, and features.
- After filtering, the View Details action remains available for a qualifying row.
- The selected University Center space shows its name, building, capacity 40, and features.

### Actual Result

- Odum Library Study Room details displayed correctly before filtering.
- Filtering did not remove or duplicate the View Details interaction.
- University Center North Meeting Room details displayed correctly after filtering.

### Status

Pass

## Defects and Follow-Up Work

The valid no-match UI scenario cannot be executed with the current starter data and the required input range. The filter accepts only 1 through 500, but a starter space has capacity 850. A follow-up should reconcile these constraints by changing the agreed maximum, providing controlled UI-level test data, or adding another supported way to exercise the empty state. Starter JSON was not changed during this test.

No other defects were identified.

## Demo Readiness

US-3 is ready to demonstrate for normal filtering, inclusive exact matches, validation errors, Clear, alphabetical listing, and space details. The valid no-match state is implemented and covered by headless tests, but it is not ready for a starter-data-based manual demo until the documented data/range mismatch is resolved.

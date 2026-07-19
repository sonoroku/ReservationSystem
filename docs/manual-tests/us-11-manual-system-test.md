# US-11 Manual System Test: Reservation Modification

## Purpose

Verify that the logged-in user can modify an owned reservation through My Reservations, that the form is prepopulated and supports partial or full changes, and that validation, persistence, and availability remain correct.

## Test Environment

- Application: ReservationSystem JavaFX app
- Branch: `issue-117-us11-modification-manual-tests`
- Parent feature branch: `issue-114-us11-modify-reservation-view`
- Command to run app: `.\mvnw.cmd javafx:run`
- Automated regression command: `.\mvnw.cmd test`
- Automated regression result: 84 tests passed; 0 failures, 0 errors, and 0 skipped
- Manual test date: July 19, 2026
- Tester: Alex O'Rourke with Codex-guided execution

## Preconditions

- Issue #113 is merged.
- Issue #114 is present through the parent feature branch.
- The default logged-in user is `student`.
- The application launches successfully.
- At least two spaces and two reservations exist for controlled conflict testing.
- `app-data/reservations.json` is backed up before testing.
- The committed starter file under `src/main/resources/data` is not modified.

## Scenario 1: Modify With No Reservation Selected

### Steps

1. Launch the application and open My Reservations.
2. Click `View My Reservations`.
3. Do not select a row.
4. Click `Modify Selected Reservation`.

### Expected Result

- No modification form opens.
- No reservation changes.
- The message `Select a reservation before modifying.` appears.

### Actual Result

The modification form remained closed, no reservation changed, and the message `Select a reservation before modifying.` appeared.

### Status

Pass

## Scenario 2: Submit Unchanged Data

### Steps

1. Select a reservation and record all displayed values.
2. Click `Modify Selected Reservation`.
3. Confirm that space, date, start time, and end time are prepopulated.
4. Click `Save Changes` without editing a field.

### Expected Result

- The unchanged reservation is accepted because it does not conflict with itself.
- The list refreshes and retains the same values and reservation ID.
- The message `Reservation modified successfully` appears.

### Actual Result

The form opened with reservation 1's space, date, start time, and end time prepopulated. Saving without edits succeeded, refreshed the list, retained the same reservation ID and values, and displayed `Reservation modified successfully`.

### Status

Pass

## Scenario 3: Modify Times Only

### Steps

1. Open the modification form for an owned reservation.
2. Leave space and date unchanged.
3. Enter a valid new start and end time.
4. Click `Save Changes`.

### Expected Result

- The modification succeeds.
- Space, date, reservation ID, and user ID remain unchanged.
- My Reservations refreshes with the new times.

### Actual Result

Reservation 1 was changed from `09:00`-`10:00` to `10:30`-`11:30`. Space 1, date `2026-07-08`, reservation ID, and user ID remained unchanged. The refreshed row displayed the new times and the success message.

### Status

Pass

## Scenario 4: Modify Space and Date

### Steps

1. Open the modification form.
2. Select a different space and date.
3. Leave the prepopulated times unchanged.
4. Click `Save Changes`.

### Expected Result

- The modification succeeds when the target interval is valid.
- Times, reservation ID, and user ID remain unchanged.
- The refreshed row displays the new space and date.

### Actual Result

Reservation 1 was moved to Odum Library Study Room on `2026-07-10` while retaining `10:30`-`11:30`. The refreshed row showed only the requested space and date changes.

### Status

Pass

## Scenario 5: Modify Every Editable Field

### Steps

1. Open the modification form.
2. Select a new space and date.
3. Enter new valid start and end times.
4. Click `Save Changes`.

### Expected Result

- The full modification succeeds.
- The refreshed row displays every new value.
- Reservation ID and user ID remain unchanged.

### Actual Result

Reservation 1 was moved to Nevins Hall Computer Lab on `2026-07-11` from `14:00` to `15:00`. The refreshed row displayed every new value while retaining reservation ID 1 and the current-user ownership.

### Status

Pass

## Scenario 6: Reject a Conflicting Interval

### Steps

1. Record another reservation's space, date, and interval.
2. Edit the selected reservation into an overlapping interval for that space and date.
3. Click `Save Changes`.

### Expected Result

- The backend rejects the modification.
- The message `Reservation conflicts with an existing reservation` appears.
- The form remains available for correction.
- Persisted reservation data remains unchanged.

### Actual Result

Moving reservation 1 into reservation 2's interval produced `Reservation conflicts with an existing reservation`. The form remained open and persisted data remained unchanged.

### Status

Pass

## Scenario 7: Reject End Time Before Start Time

### Steps

1. Open the modification form.
2. Enter a start time later than the end time.
3. Click `Save Changes`.

### Expected Result

- The message `End time must be after start time` appears.
- No reservation data changes.

### Actual Result

Entering `15:00` as the start and `14:00` as the end produced `End time must be after start time`. No reservation data changed.

### Status

Pass

## Scenario 8: Reject Excessive Duration

### Steps

1. Open the modification form.
2. Enter an interval longer than two hours.
3. Click `Save Changes`.

### Expected Result

- The message `Reservation cannot be longer than 2 hours` appears.
- No reservation data changes.

### Actual Result

Entering `09:00`-`11:01` produced `Reservation cannot be longer than 2 hours`. No reservation data changed.

### Status

Pass

## Scenario 9: Reject a Buffer Violation

### Steps

1. Record another reservation's interval for the same space and date.
2. Modify the selected reservation to begin or end less than ten minutes away.
3. Click `Save Changes`.

### Expected Result

- The message `Reservation must be at least 10 minutes away from another reservation` appears.
- No reservation data changes.

### Actual Result

Entering `14:35`-`15:30` after the existing `13:00`-`14:30` reservation produced `Reservation must be at least 10 minutes away from another reservation`. No reservation data changed.

### Status

Pass

## Scenario 10: Modification Persists After Restart

### Steps

1. Complete a successful modification and record the new values.
2. Close and relaunch the application.
3. Open My Reservations and reload the list.

### Expected Result

- The modified values remain after restart.
- Other reservations remain unchanged.

### Actual Result

After closing and relaunching the application, reservation 1 remained at Nevins Hall Computer Lab on `2026-07-11` from `14:00` to `15:00`. Other reservations continued to load.

### Status

Pass

## Scenario 11: Availability Moves With the Reservation

### Steps

1. Record the original and new space, date, and interval for a successful modification.
2. Open Availability and load the original space and date.
3. Load the new space and date.

### Expected Result

- The original interval is available.
- The new interval is reserved.
- Unrelated intervals are unchanged.

### Actual Result

After restart, Availability showed the original `09:00`-`10:00` interval for Student Union Conference Room 1 on `2026-07-08` as available. It showed the new `14:00`-`15:00` interval for Nevins Hall Computer Lab on `2026-07-11` as reserved. Unrelated reservations remained unchanged.

### Status

Pass

## Scenario 12: Cancel Editing

### Steps

1. Open the modification form and change one or more fields.
2. Click `Cancel Edit`.
3. Refresh My Reservations.

### Expected Result

- The form closes without calling the backend.
- The reservation retains its original values.

### Actual Result

Clicking `Cancel Edit` closed the form without saving. Refreshing My Reservations showed reservation 1 still at Nevins Hall Computer Lab on `2026-07-11` from `14:00` to `15:00`. The runtime JSON confirmed the same retained values.

### Status

Pass

## Scenario 13: Restore Controlled Runtime Data

### Steps

1. Close the application.
2. Restore the backed-up `app-data/reservations.json` file.
3. Confirm that `src/main/resources/data/reservations.json` was not changed.
4. Relaunch the application and reload My Reservations and Availability.

### Expected Result

- Original runtime reservations are restored.
- Committed starter data remains unchanged.
- My Reservations and Availability reflect the restored records.

### Actual Result

The saved runtime backup was restored byte-for-byte; SHA-256 hashes matched. Git confirmed that `src/main/resources/data/reservations.json` was unchanged. After relaunch, My Reservations correctly showed no records for the default `student` because the restored runtime contains `user001`, `user002`, and `user003`. Availability again showed the restored `09:00`-`10:00` and `16:00`-`17:00` reservations for Student Union Conference Room 1 on `2026-07-08`.

### Status

Pass

## Defects and Follow-Up Work

No defects were found during the manual test. All 84 automated tests also passed with no failures, errors, or skipped tests.

## Demo Readiness

US-11 reservation modification is ready to demonstrate in the Sprint video. The demo should show the no-selection message, prepopulated form, a successful partial modification, a backend validation error, refreshed My Reservations data, and the moved availability interval.

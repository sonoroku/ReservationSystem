# US-10 Manual System Test: Reservation Cancellation

## Purpose

Verify that the logged-in user can cancel an owned reservation through the My Reservations view, that confirmation is required, and that cancellation updates persistence and releases the reserved availability.

## Test Environment

- Application: ReservationSystem JavaFX app
- Command used to run app: `.\mvnw.cmd javafx:run`
- Tester: zb
- Date tested: July 19, 2026

## Preconditions

- US-10 cancellation backend is implemented.
- The My Reservations cancellation interface is implemented.
- The default logged-in user is `student`.
- The application launches successfully.
- At least one reservation belonging to `student` exists.
- At least one unrelated reservation exists when testing record preservation.
- `app-data/reservations.json` is backed up before cancellation testing.
- The committed starter file under `src/main/resources/data` is not modified.

## Scenario 1: Cancel With No Reservation Selected

### Steps

1. Launch the JavaFX application.
2. Open the My Reservations tab.
3. Click `View My Reservations`.
4. Do not select a reservation.
5. Click `Cancel Selected Reservation`.

### Expected Result

- No reservation is cancelled.
- The application displays a clear selection message.
- No confirmation dialog appears.
- Persisted reservation data remains unchanged.

### Actual Result

- No reservation was cancelled.
- The message `Select a reservation before cancelling.` appeared.
- No confirmation dialog appeared.
- Reservation data remained unchanged.

### Status

Pass

## Scenario 2: Dismiss Cancellation Confirmation

### Steps

1. Select a reservation from the My Reservations list.
2. Click `Cancel Selected Reservation`.
3. Review the confirmation dialog.
4. Dismiss or cancel the confirmation.

### Expected Result

- A confirmation dialog identifies the selected reservation.
- The dialog warns that cancellation cannot be undone.
- Dismissing the dialog leaves the reservation unchanged.
- The application displays cancellation-not-confirmed feedback.

### Actual Result

- The confirmation dialog identified the selected reservation.
- The dialog stated that the action could not be undone.
- Dismissing the dialog preserved the reservation.
- The message `Cancellation was not confirmed.` appeared.

### Status

Pass

## Scenario 3: Confirm Reservation Cancellation

### Steps

1. Select a reservation belonging to the logged-in `student` user.
2. Record its reservation ID, space, date, start time, and end time.
3. Click `Cancel Selected Reservation`.
4. Confirm the cancellation.

### Expected Result

- The selected reservation is cancelled.
- A success message is displayed.
- The My Reservations list refreshes automatically.
- The cancelled reservation no longer appears.
- Other reservations remain visible.

### Actual Result

- The selected reservation was cancelled.
- The message `Reservation cancelled successfully` appeared.
- The My Reservations list refreshed automatically.
- The cancelled reservation disappeared from the list.
- Other reservations remained visible.

### Status

Pass

## Scenario 4: Cancellation Persists After Restart

### Steps

1. Complete a successful cancellation.
2. Close the JavaFX application.
3. Launch the application again with `.\mvnw.cmd javafx:run`.
4. Open My Reservations.
5. Click `View My Reservations`.
6. Search for the cancelled reservation.

### Expected Result

- The cancelled reservation does not return after restarting.
- Other persisted reservations continue to load.
- Cancellation was saved to runtime persistence.

### Actual Result

- The cancelled reservation did not return after restarting.
- Other reservations loaded correctly.
- The cancellation remained saved in runtime persistence.

### Status

Pass

## Scenario 5: Unrelated Reservations Are Preserved

### Steps

1. Before cancellation, record another reservation not being cancelled.
2. Cancel the selected test reservation.
3. Refresh My Reservations.
4. Confirm that the unrelated reservation remains.
5. Restart the application and check again.

### Expected Result

- Only the selected reservation is removed.
- Unrelated reservations are unchanged.
- Unrelated reservations remain after restarting.

### Actual Result

- Only the selected reservation was removed.
- The unrelated reservation remained unchanged.
- The unrelated reservation remained after restarting.

### Status

Pass

## Scenario 6: Cancelled Time Becomes Available

### Steps

1. Record the cancelled reservation’s space, date, start time, and end time.
2. Open the Availability tab.
3. Select the same space and date.
4. Load availability.
5. Review the cancelled time interval.

### Expected Result

- The cancelled interval is shown as available.
- Unrelated reserved intervals remain reserved.
- Availability reloads the updated persisted reservation data.

### Actual Result

- The cancelled interval appeared as available.
- Unrelated reserved intervals remained reserved.
- The Availability view reflected the updated persisted data.

### Status

Pass

## Scenario 7: Empty My Reservations State

### Steps

1. Back up the runtime reservation file.
2. Cancel the remaining reservations belonging to `student`.
3. Allow the My Reservations list to refresh.
4. Click `Refresh` again.

### Expected Result

- The list becomes empty after the final owned reservation is cancelled.
- The application displays a clear empty-state message.
- No unrelated user’s reservations appear.

### Actual Result

- The list became empty after the final `student` reservation was cancelled.
- The message `No reservations found for this user.` appeared after refreshing.
- No reservations belonging to another user were displayed.

### Status

Pass

## Scenario 8: Restore Controlled Runtime Data

### Steps

1. Close the JavaFX application.
2. Restore the backed-up `app-data/reservations.json` file.
3. Confirm that `src/main/resources/data/reservations.json` was not changed.
4. Launch the application again.
5. Open My Reservations and reload the list.
6. Check Availability for one of the restored reservations.

### Expected Result

- The original runtime reservations are restored.
- The committed starter JSON remains unchanged.
- My Reservations displays the restored records.
- Availability displays the restored reserved intervals.

### Actual Result

- The original runtime reservation data was restored successfully.
- The committed starter JSON remained unchanged.
- My Reservations displayed the restored records.
- Availability displayed the restored reserved intervals.

### Status

Pass

## Defects and Follow-Up Work

No defects were found during this manual test.

## Demo Readiness

US-10 reservation cancellation is ready to demonstrate in the Sprint video. The demo should show the no-selection message, dismissed confirmation, successful cancellation, automatic list refresh, and the released availability interval.
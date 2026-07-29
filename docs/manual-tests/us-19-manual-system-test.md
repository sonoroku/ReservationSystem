# US-19 Admin Reservation Cancellation Manual System Test

## Test Information

- Application: ReservationSystem JavaFX app
- Command used to run app: `.\mvnw.cmd javafx:run`
- Tester: zb
- Date tested: July 28, 2026
- Environment: Windows, JDK 25, Maven Wrapper, JavaFX
- Runtime data: `app-data/reservations.json`
- Backup file: `app-data/reservations-us19-backup.json`

## Purpose

Verify that an administrator can cancel reservations belonging to any user while confirmation, authorization, persistence, availability, and unrelated reservation data continue working correctly.

## Test Results

### Test 1: Cancel an administrator-owned reservation

**Steps:**

1. Log in as an administrator.
2. Open the administrator reservation-management view.
3. Select a reservation owned by the administrator.
4. Click the cancellation button.
5. Confirm the cancellation.

**Expected result:**  
The selected reservation is removed and a clear success message is displayed.

**Actual result:**  
The reservation was successfully canceled and removed from the displayed list. A clear success message was shown.

**Result:** PASS

### Test 2: Cancel another user's reservation

**Steps:**

1. Log in as an administrator.
2. Select a reservation owned by a regular user.
3. Request cancellation.
4. Confirm the cancellation.

**Expected result:**  
The administrator can cancel the other user's reservation, and the list refreshes after cancellation.

**Actual result:**  
The other user's reservation was successfully canceled, and the reservation list refreshed correctly.

**Result:** PASS

### Test 3: Decline cancellation confirmation

**Steps:**

1. Select an existing reservation.
2. Click the cancellation button.
3. Decline the confirmation prompt.

**Expected result:**  
The reservation remains unchanged and is not removed from persistence.

**Actual result:**  
The reservation remained in the list and no cancellation occurred.

**Result:** PASS

### Test 4: Attempt cancellation with no selection

**Steps:**

1. Open the administrator reservation-management view.
2. Do not select a reservation.
3. Click the cancellation button.

**Expected result:**  
The application displays a clear message requesting that a reservation be selected.

**Actual result:**  
A clear no-selection message was displayed, and no reservation data changed.

**Result:** PASS

### Test 5: Regular-user authorization denial

**Steps:**

1. Log out of the administrator account.
2. Log in as a regular user.
3. Attempt to access or perform the administrator cancellation workflow.

**Expected result:**  
The administrator-only workflow is unavailable or the attempted action is rejected with a clear access-denied message.

**Actual result:**  
The regular user could not perform an administrator cancellation. Access was correctly denied.

**Result:** PASS

### Test 6: Cancellation persists after restart

**Steps:**

1. Log in as an administrator.
2. Cancel a selected reservation and confirm the action.
3. Close the application.
4. Restart the application.
5. Log in again and reload the reservations.

**Expected result:**  
The canceled reservation remains removed after the application restarts.

**Actual result:**  
The canceled reservation remained absent after restarting and reloading the application.

**Result:** PASS

### Test 7: Unrelated reservations remain unchanged

**Steps:**

1. Record the reservations displayed before cancellation.
2. Cancel one selected reservation.
3. Reload the reservation list.
4. Compare the remaining reservations with the original list.

**Expected result:**  
Only the selected reservation is removed. All unrelated reservations retain their original owners, spaces, dates, and times.

**Actual result:**  
Only the selected reservation was removed. All unrelated reservations remained unchanged.

**Result:** PASS

### Test 8: Canceled time becomes available

**Steps:**

1. Record the space, date, start time, and end time of a reservation.
2. Cancel that reservation as an administrator.
3. Open the availability view.
4. Load availability for the same space and date.

**Expected result:**  
The time previously occupied by the canceled reservation is displayed as available.

**Actual result:**  
The canceled reservation's time was released and displayed as available when availability was reloaded.

**Result:** PASS

## Runtime Data Restoration

Before testing, `app-data/reservations.json` was backed up as `app-data/reservations-us19-backup.json`.

After testing, the original runtime reservation data was restored using:

`Copy-Item ".\app-data\reservations-us19-backup.json" ".\app-data\reservations.json" -Force`

The application was relaunched, and the original reservations were confirmed to be present.

## Defects and Follow-Up Work

No defects were discovered during this manual test.

## Demo Readiness

US-19 is ready for demonstration. Administrator cancellation, confirmation, authorization denial, persistence, unrelated-record preservation, and released availability all behaved as expected.
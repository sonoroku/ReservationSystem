# US-16 Reservation Ownership Manual System Test

- Application: ReservationSystem JavaFX app
- Command used to run app: `.\mvnw.cmd javafx:run`
- Tester: zb
- Date tested: July 28, 2026
- Commit tested: `5243c70e610f54b0c687cfe351b61ff25b870298`
- Environment: Windows, JDK 25, Maven Wrapper, JavaFX
- Automated baseline: 183 tests passed with 0 failures and 0 errors
- Runtime data backup: Created before testing

## Test Data

Two controlled reservations were created for `student`:

- Modification reservation:
  - Space: Odum Library Study Room
  - Date: September 1, 2026
  - Original time: 10:00–11:00
  - Modified time: 11:00–12:00
- Cancellation reservation:
  - Space: Nevins Hall Computer Lab
  - Date: September 2, 2026
  - Time: 14:00–15:00

## Test 1 — Current-User Reservation Visibility

**Steps**

1. Log in as `student`.
2. Create both controlled reservations.
3. Open My Reservations and refresh.

**Expected Result**

Both reservations appear under the current `student` session with their correct spaces, dates, and times.

**Actual Result**

PASS — Both controlled reservations appeared in the `student` My Reservations list with the correct spaces, dates, and times.

## Test 2 — Owner Modification

**Steps**

1. Select the September 1 Odum Library reservation.
2. Choose Modify.
3. Change its time from 10:00–11:00 to 11:00–12:00.
4. Save and refresh My Reservations and Availability.

**Expected Result**

The owner can modify the reservation. Its ID and owner remain unchanged. The old interval becomes Available, and the new interval becomes Reserved.

**Actual Result**

PASS — The owner successfully modified the Odum Library reservation from 10:00–11:00 to 11:00–12:00. The reservation retained its identity and owner. The original interval became Available, and the modified interval became Reserved.

## Test 3 — Owner Cancellation

**Steps**

1. Select the September 2 Nevins Hall reservation.
2. Choose Cancel.
3. Confirm the cancellation.
4. Refresh My Reservations and Availability.

**Expected Result**

The owner can cancel the reservation. It disappears from My Reservations, and its availability interval is released.

**Actual Result**

PASS — The owner successfully canceled the Nevins Hall reservation. It disappeared from My Reservations, and the 14:00–15:00 availability interval was released.

## Test 4 — Logout and Login Change Current User

**Steps**

1. Log out of `student`.
2. Log in as `admin`.
3. Open the normal user-level My Reservations view.
4. Log out and log back in as `student`.
5. Reload My Reservations.

**Expected Result**

The normal My Reservations view follows the authenticated user. The administrator’s user-level list does not expose the student-owned reservation. After returning to `student`, the modified student reservation appears again.

**Actual Result**

PASS — The normal My Reservations view changed with the authenticated session. The administrator’s user-level list did not display the student-owned reservation. Logging back in as `student` restored access to the student reservation.

## Test 5 — Non-Owner Modification Protection

**Steps**

1. While logged in as a different user, inspect the normal My Reservations workflow.
2. Confirm that the student-owned reservation cannot be selected through another user’s reservation list.
3. Run the automated ownership tests through `.\mvnw.cmd test`.

**Expected Result**

The normal UI does not expose another user’s reservation for modification. The controller rejects a direct non-owner modification with `Reservation does not belong to current user`, and persisted times remain unchanged.

**Actual Result**

PASS — Another user could not select the student-owned reservation through the normal UI. Automated ownership coverage confirmed that a direct non-owner modification returns `Reservation does not belong to current user` and leaves persisted times unchanged.

## Test 6 — Non-Owner Cancellation Protection

**Steps**

1. While logged in as a different user, inspect the normal My Reservations workflow.
2. Confirm that the student-owned reservation cannot be selected for cancellation.
3. Verify the automated ownership result from the Maven test suite.

**Expected Result**

The normal UI does not expose another user’s reservation for cancellation. A direct non-owner controller attempt returns `Reservation does not belong to current user`, and the reservation remains persisted.

**Actual Result**

PASS — Another user could not select the student-owned reservation for cancellation through the normal UI. Automated ownership coverage confirmed that direct non-owner cancellation is rejected and persistence remains unchanged.

## Test 7 — Missing Reservation Handling

**Steps**

1. Cancel the controlled cancellation reservation.
2. Refresh My Reservations.
3. Confirm the removed reservation can no longer be selected.
4. Verify the automated not-found modification and cancellation tests.

**Expected Result**

The deleted reservation is absent from the UI. Attempts using a nonexistent reservation ID return `Reservation was not found` without changing storage.

**Actual Result**

PASS — The canceled reservation was absent and could no longer be selected. Automated not-found coverage confirmed that nonexistent reservation IDs return `Reservation was not found` without changing storage.

## Test 8 — Persistence After Restart

**Steps**

1. Close and restart the application.
2. Log in as `student`.
3. Reload My Reservations and Availability.

**Expected Result**

The modified reservation remains at 11:00–12:00. The canceled reservation remains absent. Ownership remains assigned to `student`.

**Actual Result**

PASS — After restart, the modified reservation remained at 11:00–12:00, the canceled reservation remained absent, and ownership remained assigned to `student`.

## Test 9 — Rejected Actions Leave Storage Unchanged

**Steps**

1. Compare the persisted reservation state before and after the non-owner and missing-reservation checks.
2. Confirm the Maven ownership tests use isolated temporary files.

**Expected Result**

Rejected non-owner and missing-reservation actions do not alter persisted reservations. Automated tests do not modify workspace runtime data.

**Actual Result**

PASS — Rejected non-owner and missing-reservation operations left persisted data unchanged. The automated tests used isolated temporary files and did not alter workspace runtime data.

## Runtime Data Restoration

After testing, the original runtime reservation and user data were restored from backup. Temporary test records were not committed.

## Defects and Follow-Up Work

No defects were found during manual testing.

## Overall Result

PASS — US-16 reservation ownership behavior met all documented acceptance criteria.

## Demo Readiness

Ready — US-16 is ready for the Sprint 2 demonstration.
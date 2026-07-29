# US-17 Admin Reservation Creation Manual System Test

- Application: ReservationSystem JavaFX app
- Command used to run app: `.\mvnw.cmd javafx:run`
- Tester: zb
- Date tested: July 28, 2026
- Commit tested: `bc81c4ce12360947580856fc6c92ab09324b70f4`
- Environment: Windows, JDK 25, Maven Wrapper, JavaFX
- Automated baseline: 170 tests passed with 0 failures and 0 errors
- Runtime data backup: Created before testing

## Test Data

- Administrator account: `admin`
- Regular account: `student`
- Selected user: `student`
- Space: Odum Library Study Room
- Date: August 25, 2026
- Valid time: 10:00–11:00

## Test 1 — Administrator Access and User Selection

**Steps**

1. Log in as `admin`.
2. Confirm the Admin Create Reservation tab is visible.
3. Open the tab and inspect the user selector and reservation fields.

**Expected Result**

The administrator can access the workflow. The user selector contains valid persisted users, and the form provides space, date, start-time, and end-time controls.

**Actual Result**

PASS — The administrator-only tab was visible. The selector contained `admin` and `student`, and all required reservation fields were available.

## Test 2 — Create a Reservation for Another User

**Steps**

1. Load Availability for Odum Library Study Room on August 25, 2026.
2. Confirm 10:00–11:00 is available.
3. Open Admin Create Reservation.
4. Select `student`, Odum Library Study Room, and August 25, 2026.
5. Enter `10:00` and `11:00`.
6. Submit.

**Expected Result**

The reservation is created for `student`, a success message appears, and loaded availability refreshes to show the interval as Reserved.

**Actual Result**

PASS — The application displayed `Reservation created successfully`. Availability refreshed and showed the 10:00–11:00 interval as Reserved.

## Test 3 — Selected-User Association and My Reservations

**Steps**

1. Log out of the administrator account.
2. Log in as `student`.
3. Open and refresh My Reservations.

**Expected Result**

The newly created reservation appears under `student` with the correct space, date, and times.

**Actual Result**

PASS — The reservation appeared under the `student` account with Odum Library Study Room, August 25, 2026, and 10:00–11:00.

## Test 4 — Persistence After Restart

**Steps**

1. Close the application.
2. Restart it with `.\mvnw.cmd javafx:run`.
3. Log in as `student`.
4. Reload My Reservations and Availability.

**Expected Result**

The reservation remains associated with `student`, and the corresponding availability remains Reserved.

**Actual Result**

PASS — The reservation remained present after restart, retained the `student` owner, and continued to reserve the correct availability interval.

## Test 5 — Overlapping Reservation Rejection

**Steps**

1. Log in as `admin`.
2. Select a valid user, Odum Library Study Room, and August 25, 2026.
3. Enter `10:30` and `11:30`.
4. Submit.

**Expected Result**

The overlapping reservation is rejected, and no additional reservation is saved.

**Actual Result**

PASS — The application displayed `Reservation conflicts with an existing reservation`. No additional reservation was persisted.

## Test 6 — Invalid Time Order

**Steps**

1. Keep a valid user, space, and date selected.
2. Enter start time `12:00`.
3. Enter end time `11:00`.
4. Submit.

**Expected Result**

The reservation is rejected because the end time is not after the start time.

**Actual Result**

PASS — The application displayed `End time must be after start time`, and persistence remained unchanged.

## Test 7 — Duration Longer Than Two Hours

**Steps**

1. Enter start time `09:00`.
2. Enter end time `11:30`.
3. Submit.

**Expected Result**

The reservation is rejected because its duration exceeds two hours.

**Actual Result**

PASS — The application displayed `Reservation cannot be longer than 2 hours`. No reservation was saved.

## Test 8 — Ten-Minute Buffer Violation

**Steps**

1. Enter start time `11:05`.
2. Enter end time `12:00`.
3. Submit.

**Expected Result**

The reservation is rejected because it is less than 10 minutes from the existing reservation.

**Actual Result**

PASS — The application displayed `Reservation must be at least 10 minutes away from another reservation`. Persistence remained unchanged.

## Test 9 — Missing or Invalid User Selection

**Steps**

1. Clear or omit the selected user.
2. Complete the remaining reservation fields.
3. Submit.

**Expected Result**

The form displays controlled feedback and does not save a reservation. Arbitrary invalid user IDs cannot be entered because users are provided through a controller-populated selector.

**Actual Result**

PASS — The application displayed `Please select a user.` No reservation was saved. The selector prevented arbitrary invalid user IDs, while the backend invalid-user outcome remains covered by automated tests.

## Test 10 — Regular-User Access Denial

**Steps**

1. Log out.
2. Log in as `student`.
3. Inspect the available tabs.

**Expected Result**

The Admin Create Reservation tab is hidden from regular users. The existing user-level Create Reservation workflow remains available.

**Actual Result**

PASS — The admin tab was not visible to `student`, while the normal Create Reservation workflow remained accessible. Automated authorization tests also confirmed that direct regular-user controller attempts are rejected without saving.

## Persistence Verification

The runtime reservation data was inspected after successful creation. The reservation retained `student` as its owner. Conflict, invalid-time, duration, buffer, missing-user, and unauthorized attempts did not create or alter reservations.

## Runtime Data Restoration

The original `app-data/reservations.json` and `app-data/users.json` data were restored from backup after testing. Temporary test data was not committed.

## Defects and Follow-Up Work

No defects were found during manual testing.

## Overall Result

PASS — The US-17 administrator reservation-creation workflow met all documented acceptance criteria.

## Demo Readiness

Ready — US-17 is ready for the Sprint 2 demonstration.
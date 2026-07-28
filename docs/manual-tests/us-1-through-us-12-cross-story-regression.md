# US-1 Through US-12 Cross-Story Manual Regression

- Application: ReservationSystem JavaFX app
- Command used to run app: `.\mvnw.cmd javafx:run`
- Tester: zb
- Date tested: July 27, 2026
- Commit tested: `08296e62d0ab559f070e8930cd6c1cf1df5130bb`
- Environment: Windows, JDK 25, Maven Wrapper, JavaFX
- Automated baseline: 147 tests passed with 0 failures and 0 errors
- Test account: `student`
- Runtime data backup: Created before testing

## Test Data

The committed dataset contained five campus spaces. Controlled reservation testing used:

- User: `student`
- Space: Odum Library Study Room
- Date: August 20, 2026
- Initial time: 10:00 AM–11:00 AM
- Modified time: 1:00 PM–2:00 PM

## Test 1 — View All Spaces and Details

**Steps**

1. Log in as `student`.
2. Open the Spaces tab.
3. Review the displayed spaces.
4. Select Student Union Conference Room 1.

**Expected Result**

Five spaces appear alphabetically. Selecting a space displays its complete name, building, capacity, and features.

**Actual Result**

PASS — All five spaces appeared alphabetically. Student Union Conference Room 1 displayed Student Union as its building, capacity 10, and the complete Interactive Whiteboard, PC, Conference Table, and Wi-Fi feature list.

## Test 2 — Minimum-Capacity Filter

**Steps**

1. Enter a minimum capacity of `30`.
2. Apply the capacity filter.

**Expected Result**

Only spaces with capacity 30 or greater appear, and the results remain alphabetical.

**Actual Result**

PASS — Nevins Hall Computer Lab, Student Union Multipurpose Room, and University Center North Meeting Room appeared alphabetically.

## Test 3 — Feature Filter

**Steps**

1. Clear the existing filters.
2. Select or enter `Projector`.
3. Apply the feature filter.

**Expected Result**

Every displayed space contains the Projector feature.

**Actual Result**

PASS — Nevins Hall Computer Lab, Student Union Multipurpose Room, and University Center North Meeting Room appeared. Every result contained Projector.

## Test 4 — Combined Filters

**Steps**

1. Set minimum capacity to `40`.
2. Select or enter `Projector`.
3. Apply both filters.

**Expected Result**

Only spaces satisfying both requirements appear.

**Actual Result**

PASS — Student Union Multipurpose Room and University Center North Meeting Room appeared. Both had capacity of at least 40 and included Projector.

## Test 5 — Empty Filter Result

**Steps**

1. Clear the feature filter.
2. Enter minimum capacity `500`.
3. Apply the filter.

**Expected Result**

No spaces appear and the application displays a clear no-match or empty-state message.

**Actual Result**

PASS — No spaces appeared, and the application displayed a clear empty-result message without crashing.

## Test 6 — Invalid Filter Input

**Steps**

1. Enter invalid minimum-capacity input such as `abc` or `0`.
2. Apply the filter.

**Expected Result**

The application rejects the input and displays a clear validation message.

**Actual Result**

PASS — Invalid input was rejected with a clear message. The application remained responsive and did not alter persisted data.

## Test 7 — Create a Valid Reservation

**Steps**

1. Open Create Reservation.
2. Select Odum Library Study Room.
3. Select August 20, 2026.
4. Enter start time `10:00`.
5. Enter end time `11:00`.
6. Submit the reservation.

**Expected Result**

The reservation is created for the logged-in `student` account and a success message appears.

**Actual Result**

PASS — The reservation was created successfully for `student`, and clear success feedback appeared.

## Test 8 — Daily Availability and Visual Distinction

**Steps**

1. Open Availability.
2. Select Odum Library Study Room.
3. Select August 20, 2026.
4. Load daily availability.

**Expected Result**

The schedule covers 8:00 AM through 8:00 PM in 30-minute slots. Slots overlapping 10:00–11:00 are marked Reserved. Other slots are marked Available. The states use different text and styling.

**Actual Result**

PASS — The complete daily schedule appeared. The 10:00–10:30 and 10:30–11:00 slots were marked Reserved, while non-overlapping slots were marked Available. The states were distinguishable by both text and styling.

## Test 9 — Date-Range Availability

**Steps**

1. Select Odum Library Study Room.
2. Enter August 20, 2026 as the start date.
3. Enter August 22, 2026 as the end date.
4. Load range availability.

**Expected Result**

All three included dates appear chronologically. The reservation is marked only on August 20, and other slots remain available.

**Actual Result**

PASS — August 20, 21, and 22 appeared chronologically. Only the August 20 slots overlapping the reservation were marked Reserved.

## Test 10 — Invalid Date Ranges

**Steps**

1. Enter August 22 as the start date and August 20 as the end date.
2. Attempt to load the range.
3. Enter August 20 through August 27, an inclusive eight-day range.
4. Attempt to load the range again.

**Expected Result**

The reversed range and range longer than seven inclusive days are rejected with clear validation messages.

**Actual Result**

PASS — Both invalid date ranges were rejected with clear messages, and no incorrect schedule was displayed.

## Test 11 — Invalid Reservation Times

**Steps**

Attempt each reservation:

- Start `12:00`, end `11:00`
- Start `09:00`, end `11:30`
- Start `10:30`, end `11:30` on the occupied space and date
- Start `11:05`, end `12:00` on the occupied space and date

**Expected Result**

The application rejects invalid time order, duration over two hours, overlap, and violation of the 10-minute buffer.

**Actual Result**

PASS — Every invalid reservation was rejected with an appropriate validation message. None of the rejected reservations appeared in My Reservations or altered availability.

## Test 12 — View My Reservations and Persistence

**Steps**

1. Open My Reservations.
2. Load the current user’s reservations.
3. Confirm the new reservation appears.
4. Close and restart the application.
5. Log in again and reload My Reservations.

**Expected Result**

The reservation appears with its ID, space, date, start time, and end time. It remains after restarting the application.

**Actual Result**

PASS — The reservation appeared with complete information and remained available after restarting and reloading the application.

## Test 13 — Modify a Reservation

**Steps**

1. Select the August 20 Odum Library reservation.
2. Choose Modify.
3. Change the time from 10:00–11:00 to 13:00–14:00.
4. Save the modification.
5. Refresh My Reservations and Availability.

**Expected Result**

The reservation retains its ID and owner but uses the modified time. The original slots become available and the new slots become reserved.

**Actual Result**

PASS — The reservation retained its identity and owner. My Reservations displayed 13:00–14:00. The 10:00–11:00 slots became Available, and the 13:00–14:00 slots became Reserved.

## Test 14 — Available-Time Suggestions

**Steps**

1. Open the available-time suggestion interaction.
2. Select Nevins Hall Computer Lab.
3. Select August 21, 2026.
4. Request a duration of 60 minutes.
5. Load suggestions.
6. Select one suggested time.

**Expected Result**

Suggestions appear chronologically within 8:00 AM–8:00 PM and obey conflict, buffer, and maximum-duration rules. Selecting a suggestion populates the Create Reservation form.

**Actual Result**

PASS — Chronological valid suggestions appeared. Selecting a suggestion populated the appropriate space, date, start-time, and end-time creation fields.

## Test 15 — Cancel a Reservation

**Steps**

1. Open My Reservations.
2. Select the modified August 20 reservation.
3. Choose Cancel.
4. Confirm the cancellation.
5. Refresh My Reservations and Availability.
6. Restart the application and check again.

**Expected Result**

The reservation is removed after confirmation, remains absent after restart, and its 13:00–14:00 availability slots are released.

**Actual Result**

PASS — The reservation disappeared after confirmation and remained absent after restart. The corresponding availability slots returned to Available. Unrelated reservations were unchanged.

## Test 16 — Cancellation Without Selection

**Steps**

1. Open My Reservations without selecting a reservation.
2. Choose Cancel.

**Expected Result**

The application displays clear selection feedback and does not change persistence.

**Actual Result**

PASS — A clear no-selection message appeared, and no reservations were changed.

## Runtime Data Restoration

After testing, the original `app-data/reservations.json` and `app-data/users.json` files were restored from their backups. Temporary regression records were not committed.

## Defects and Follow-Up Work

No defects were found during this regression pass.

## Overall Result

PASS — The complete US-1 through US-12 JavaFX regression passed. Spaces, details, filtering, availability, visual distinction, reservation creation, viewing, modification, cancellation, suggestions, persistence, and representative invalid, empty, and conflict paths behaved as expected.

## Demo Readiness

US-1 through US-12 are ready for demonstration.
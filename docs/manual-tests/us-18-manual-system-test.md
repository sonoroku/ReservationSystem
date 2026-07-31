# US-18 Manual System Test: Admin Modify Reservation

## User Story

As an administrator, I want to modify any reservation so that I can correct reservation details when needed.

## Test Environment

- Application: ReservationSystem JavaFX app
- Command used to run app: `.\mvnw.cmd javafx:run`
- Tester: Almondmlk
- Date tested: July 30, 2026
- Tested branch: main
- Tested commit/SHA: ec6cea2
- Logged-in user: admin
- User role: Administrator

## Test Case 1: Admin Can View System-Wide Reservation List

### Inputs Used

- User type: Administrator
- View opened: Admin Modify Reservation
- Data source: `reservations.json`

### Steps

1. Ran the application using `.\mvnw.cmd javafx:run`.
2. Logged in as `admin`.
3. Opened the Admin Modify Reservation tab.
4. Observed the reservation list.

### Expected Result

The application displays a system-wide list of reservations with owner information.

### Actual Result

The application displayed a selectable list of reservations. The list included:

- Reservation ID: 1 | User ID: student | Space ID: 1 | Date: 2026-07-08 | Start: 09:00 | End: 10:00
- Reservation ID: 2 | User ID: admin | Space ID: 2 | Date: 2026-07-08 | Start: 13:00 | End: 14:30
- Reservation ID: 3 | User ID: student | Space ID: 3 | Date: 2026-07-09 | Start: 11:00 | End: 12:00

### Status

Pass

## Test Case 2: Selecting a Reservation Populates the Edit Form

### Inputs Used

- Selected reservation: 1
- Original user ID: student
- Original space: Space ID 1
- Original date: 2026-07-08
- Original start time: 09:00
- Original end time: 10:00

### Steps

1. Ran the application using `.\mvnw.cmd javafx:run`.
2. Logged in as `admin`.
3. Opened the Admin Modify Reservation tab.
4. Selected reservation ID `1` from the reservation list.
5. Checked the space, date, start time, and end time fields.

### Expected Result

The selected reservation populates the edit form with its current space, date, start time, and end time.

### Actual Result

Selecting reservation ID `1` populated the edit form with `Student Union Conference Room 1`, date `2026-07-08`, start time `09:00`, and end time `10:00`. The application displayed `Reservation selected for modification.`

### Status

Pass

## Test Case 3: Admin Can Modify a Reservation With Partial Changes

### Inputs Used

- Selected reservation: 1
- User ID: student
- Space: Student Union Conference Room 1
- Date: 2026-07-08
- Original start time: 09:00
- Original end time: 10:00
- Updated start time: 11:00
- Updated end time: 12:00

### Steps

1. Ran the application using `.\mvnw.cmd javafx:run`.
2. Logged in as `admin`.
3. Opened the Admin Modify Reservation tab.
4. Selected reservation ID `1`.
5. Left the space and date unchanged.
6. Changed the start time from `09:00` to `11:00`.
7. Changed the end time from `10:00` to `12:00`.
8. Clicked Modify Reservation.
9. Observed the message shown by the application.
10. Checked that the reservation list refreshed.

### Expected Result

The application modifies the selected reservation, displays a success message, and refreshes the reservation list.

### Actual Result

The application displayed `Reservation modified successfully` and refreshed the reservation list. Reservation ID `1` updated to start time `11:00` and end time `12:00`.

### Status

Pass

## Test Case 4: Refresh Updates the Reservation List

### Inputs Used

- Action: Refresh Reservations button
- Selected reservation before refresh: Reservation ID 1
- Current reservation data shown: Reservation ID 1 | User ID: student | Space ID: 1 | Date: 2026-07-08 | Start: 11:00 | End: 12:00

### Steps

1. Ran the application using `.\mvnw.cmd javafx:run`.
2. Logged in as `admin`.
3. Opened the Admin Modify Reservation tab.
4. Clicked Refresh Reservations.
5. Observed the reservation list and message.

### Expected Result

The application reloads the reservation list from persistence and displays a refresh message.

### Actual Result

The application refreshed the reservation list and displayed `Reservation list refreshed.` The updated reservation list still showed reservation ID `1` with start time `11:00` and end time `12:00`.

### Status

Pass

## Test Case 5: Invalid Time Format Shows Validation Message

### Inputs Used

- Selected reservation: 1
- Start time: 9:00
- End time: 12:00

### Steps

1. Ran the application using `.\mvnw.cmd javafx:run`.
2. Logged in as `admin`.
3. Opened the Admin Modify Reservation tab.
4. Selected reservation ID `1`.
5. Entered `9:00` in the start time field.
6. Entered `12:00` in the end time field.
7. Clicked Modify Reservation.

### Expected Result

The application does not modify the reservation and displays a validation message for invalid time format.

### Actual Result

The application displayed `Please enter times in HH:mm format, such as 09:00 or 14:30.` The reservation was not modified.

### Status

Pass

## Test Case 6: Duration Constraint Is Rejected

### Inputs Used

- Selected reservation: 1
- Start time: 09:00
- End time: 12:00

### Steps

1. Ran the application using `.\mvnw.cmd javafx:run`.
2. Logged in as `admin`.
3. Opened the Admin Modify Reservation tab.
4. Selected reservation ID `1`.
5. Entered `09:00` in the start time field.
6. Entered `12:00` in the end time field.
7. Clicked Modify Reservation.

### Expected Result

The application rejects the update because the reservation is longer than the allowed duration.

### Actual Result

The application displayed `Reservation cannot be longer than 2 hours` and did not save the update.

### Status

Pass

## Test Case 7: Invalid Time Order Is Rejected

### Inputs Used

- Selected reservation: 1
- Start time: 11:00
- End time: 10:00

### Steps

1. Ran the application using `.\mvnw.cmd javafx:run`.
2. Logged in as `admin`.
3. Opened the Admin Modify Reservation tab.
4. Selected reservation ID `1`.
5. Entered `11:00` in the start time field.
6. Entered `10:00` in the end time field.
7. Clicked Modify Reservation.

### Expected Result

The application rejects the update because the end time is before the start time.

### Actual Result

The application displayed a validation message explaining that the end time must be after the start time. The reservation was not modified.

### Status

Pass

## Test Case 8: Conflict or Buffer Violation Is Rejected

### Inputs Used

- Selected reservation: 1
- Updated space: Space ID 2
- Updated date: 2026-07-08
- Updated start time: 13:00
- Updated end time: 14:00
- Conflicting reservation checked against: Reservation ID 2

### Steps

1. Ran the application using `.\mvnw.cmd javafx:run`.
2. Logged in as `admin`.
3. Opened the Admin Modify Reservation tab.
4. Selected reservation ID `1`.
5. Changed the space to the same space used by reservation ID `2`.
6. Set the date to `2026-07-08`.
7. Entered `13:00` as the start time.
8. Entered `14:00` as the end time.
9. Clicked Modify Reservation.

### Expected Result

The application rejects the update and displays a controlled validation message.

### Actual Result

The application displayed a validation message and did not save the conflicting reservation update.

### Status

Pass

## Test Case 9: Modified Reservation Persists After Refresh

### Inputs Used

- Selected reservation: 1
- Updated space: Space ID 1
- Updated date: 2026-07-08
- Updated start time: 11:00
- Updated end time: 12:00
- Action after update: Refresh Reservations

### Steps

1. Ran the application using `.\mvnw.cmd javafx:run`.
2. Logged in as `admin`.
3. Opened the Admin Modify Reservation tab.
4. Selected reservation ID `1`.
5. Modified the reservation.
6. Clicked Modify Reservation.
7. Clicked Refresh Reservations.
8. Checked the same reservation in the refreshed list.

### Expected Result

The modified reservation remains updated after refreshing from persistence.

### Actual Result

The modified reservation remained updated after the reservation list was refreshed.

### Status

Pass

## Runtime Data Restoration

After testing, temporary reservation changes were restored so the application data returned to its original test state.

### Restored Data

- Restored reservation ID: 1
- Restored space: Space ID 1
- Restored date: 2026-07-08
- Restored start time: 09:00
- Restored end time: 10:00

## Bugs Found

No bugs found during manual testing.

## Follow-Up Issues

No follow-up issues were created.

## Demo Readiness

US-18 Admin Modify Reservation is ready to demonstrate in the Sprint 2 video.

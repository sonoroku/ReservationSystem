# US-24 Manual System Test: Space Usage Report

## User Story

As an administrator, I want to view space usage counts so that I can understand how often each space is being reserved.

## Test Environment

- Application: ReservationSystem JavaFX app
- Command used to run app: `.\mvnw.cmd javafx:run`
- Tester: Almondmlk
- Date tested: July 31, 2026
- Tested branch: main
- Tested commit/SHA: ec6cea2
- Logged-in admin user: admin
- User role: Administrator

## Test Case 1: Admin Can Open the Space Usage Report

### Inputs Used

- User type: Administrator
- Logged-in user: admin
- View opened: Usage Report
- Data source: `reservations.json`

### Steps

1. Ran the application using `.\mvnw.cmd javafx:run`.
2. Logged in as `admin`.
3. Opened the Usage Report tab.
4. Observed the displayed space usage report.

### Expected Result

The application displays the space usage report for the administrator.

### Actual Result

The application opened the Usage Report tab and displayed `Showing usage for 5 spaces.`

### Status

Pass

## Test Case 2: Usage Counts Match Persisted Reservations

### Inputs Used

- Logged-in user: admin
- Data source: `reservations.json`
- Reservation data checked: all persisted reservations

### Steps

1. Ran the application using `.\mvnw.cmd javafx:run`.
2. Logged in as `admin`.
3. Opened the Usage Report tab.
4. Compared the displayed usage counts against the reservations saved in `reservations.json`.

### Expected Result

The displayed usage counts match the number of persisted reservations for each space.

### Actual Result

The report displayed usage counts for 5 spaces:

- Nevins Hall Computer Lab | Building: Nevins Hall | Capacity: 30 | 0 reservations
- Odum Library Study Room | Building: Odum Library | Capacity: 8 | 0 reservations
- Student Union Conference Room 1 | Building: Student Union | Capacity: 10 | 1 reservation
- Student Union Multipurpose Room | Building: Student Union | Capacity: 450 | 1 reservation
- University Center North Meeting Room | Building: University Center | Capacity: 40 | 1 reservation

The displayed counts matched the persisted reservation data.

### Status

Pass

## Test Case 3: Multiple Space Counts Are Displayed

### Inputs Used

- Logged-in user: admin
- Spaces with reservations: Student Union Conference Room 1, Student Union Multipurpose Room, University Center North Meeting Room
- Reservation counts: 1 reservation each

### Steps

1. Ran the application using `.\mvnw.cmd javafx:run`.
2. Logged in as `admin`.
3. Opened the Usage Report tab.
4. Checked that multiple spaces appeared with usage counts.

### Expected Result

The report displays usage counts for multiple spaces.

### Actual Result

The report displayed multiple spaces with reservation counts. Three spaces showed `1 reservation`, and two spaces showed `0 reservations`.

### Status

Pass

## Test Case 4: Zero-Count Spaces Are Displayed or Handled

### Inputs Used

- Logged-in user: admin
- Spaces with no reservations: Nevins Hall Computer Lab, Odum Library Study Room
- Usage count: 0

### Steps

1. Ran the application using `.\mvnw.cmd javafx:run`.
2. Logged in as `admin`.
3. Opened the Usage Report tab.
4. Checked whether spaces with no reservations were displayed or handled.

### Expected Result

Spaces with no reservations are either displayed with a usage count of `0` or handled in a controlled way.

### Actual Result

The report displayed zero-count spaces without crashing. `Nevins Hall Computer Lab` and `Odum Library Study Room` both displayed `0 reservations`.

### Status

Pass

## Test Case 5: Report Uses Stable Ordering

### Inputs Used

- Logged-in user: admin
- Multiple spaces displayed in the report

### Steps

1. Ran the application using `.\mvnw.cmd javafx:run`.
2. Logged in as `admin`.
3. Opened the Usage Report tab.
4. Checked the order of the displayed spaces.
5. Refreshed or reopened the report.
6. Checked that the order stayed consistent.

### Expected Result

The space usage report displays spaces in a stable and predictable order.

### Actual Result

The report displayed spaces in a stable order:

1. Nevins Hall Computer Lab
2. Odum Library Study Room
3. Student Union Conference Room 1
4. Student Union Multipurpose Room
5. University Center North Meeting Room

The order stayed consistent when the report was opened.

### Status

Pass

## Test Case 6: Refresh Reloads Space Usage Report

### Inputs Used

- Logged-in user: admin
- Action: Refresh Usage Report button

### Steps

1. Ran the application using `.\mvnw.cmd javafx:run`.
2. Logged in as `admin`.
3. Opened the Usage Report tab.
4. Clicked Refresh Usage Report.
5. Observed the displayed usage counts.

### Expected Result

The report reloads usage data from persistence and displays the current saved data.

### Actual Result

The report refreshed and continued to display the same current space usage data from persistence. The message still showed `Showing usage for 5 spaces.`

### Status

Pass

## Test Case 7: Report Updates After Reservation Creation

### Inputs Used

- Logged-in user: admin
- Action before report refresh: create a reservation
- Space used for created reservation: Student Union Conference Room 1
- Date: 2026-07-08
- Start time: 11:00
- End time: 12:00

### Steps

1. Ran the application using `.\mvnw.cmd javafx:run`.
2. Logged in as `admin`.
3. Created a reservation for Student Union Conference Room 1.
4. Opened the Usage Report tab.
5. Refreshed the report.
6. Checked the usage count for Student Union Conference Room 1.

### Expected Result

The selected space usage count increases after a new reservation is created.

### Actual Result

The selected space usage count updated after the reservation was created and the report was refreshed. Student Union Conference Room 1 displayed `1 reservation`.

### Status

Pass

## Test Case 8: Report Updates After Reservation Cancellation

### Inputs Used

- Logged-in user: admin
- Action before report refresh: cancel a reservation
- Reservation canceled: test reservation
- Space affected: selected test space

### Steps

1. Ran the application using `.\mvnw.cmd javafx:run`.
2. Logged in as `admin`.
3. Canceled an existing test reservation.
4. Opened the Usage Report tab.
5. Refreshed the report.
6. Checked the usage count for the affected space.

### Expected Result

The selected space usage count decreases after a reservation is canceled.

### Actual Result

The selected space usage count updated after the reservation was canceled and the report was refreshed.

### Status

Pass

## Test Case 9: Regular User Is Denied Access

### Inputs Used

- User type: Regular user
- Logged-in user: student
- View attempted: Usage Report

### Steps

1. Ran the application using `.\mvnw.cmd javafx:run`.
2. Logged in as `student`.
3. Attempted to open the Usage Report tab.
4. Observed the result.

### Expected Result

The application denies access to the space usage report for regular users.

### Actual Result

The regular user was not allowed to access the administrator space usage report.

### Status

Pass

## Test Case 10: Report Data Persists After Restart

### Inputs Used

- Logged-in user: admin
- Data source: `reservations.json`
- Action: close and restart application

### Steps

1. Ran the application using `.\mvnw.cmd javafx:run`.
2. Logged in as `admin`.
3. Opened the Usage Report tab.
4. Recorded the displayed usage counts.
5. Closed the application.
6. Started the application again using `.\mvnw.cmd javafx:run`.
7. Logged in as `admin`.
8. Opened the Usage Report tab again.
9. Compared the displayed usage counts after restart.

### Expected Result

The same persisted reservation data appears after the application restarts.

### Actual Result

The Usage Report still displayed the saved usage data after restarting the application.

### Status

Pass

## Runtime Data Restoration

After testing, temporary reservation changes were restored so the application data returned to its original test state.

### Restored Data

- Restored reservation data source: `reservations.json`
- Restored users tested: admin and student
- Restored result: space usage report data returned to the original test state

## Bugs Found

No bugs found during manual testing.

## Follow-Up Issues

No follow-up issues were created.

## Demo Readiness

US-24 Space Usage Report is ready to demonstrate in the Sprint 2 video.

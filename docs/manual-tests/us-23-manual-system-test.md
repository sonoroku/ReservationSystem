# US-23 Manual System Test: View All Reservations

## User Story

As an administrator, I want to view all reservations so that I can review reservation activity across the whole system.

## Test Environment

- Application: ReservationSystem JavaFX app
- Command used to run app: `.\mvnw.cmd javafx:run`
- Tester: Almondmlk
- Date tested: July 31, 2026
- Tested branch: main
- Tested commit/SHA: ec6cea2
- Logged-in admin user: admin
- User role: Administrator

## Test Case 1: Admin Can Open the All Reservations Report

### Inputs Used

- User type: Administrator
- Logged-in user: admin
- View opened: Reservations Report
- Data source: `reservations.json`

### Steps

1. Ran the application using `.\mvnw.cmd javafx:run`.
2. Logged in as `admin`.
3. Opened the Reservations Report tab.
4. Observed the displayed all-reservations report.

### Expected Result

The application displays an all-reservations report for the administrator.

### Actual Result

The application opened the Reservations Report tab and displayed `Reservations found`.

### Status

Pass

## Test Case 2: Report Displays All Persisted Reservations

### Inputs Used

- Logged-in user: admin
- Data source: `reservations.json`
- Expected reservations: all reservations currently saved in persistence

### Steps

1. Ran the application using `.\mvnw.cmd javafx:run`.
2. Logged in as `admin`.
3. Opened the Reservations Report tab.
4. Compared the displayed report rows to the saved reservation data.

### Expected Result

Every persisted reservation is displayed in the report.

### Actual Result

The report displayed three persisted reservations:

- Reservation ID: 1 | User ID: student | Space: Student Union Conference Room 1 | Space ID: 1 | Building: Student Union | Date: 2026-07-08 | Start: 11:00 | End: 12:00
- Reservation ID: 2 | User ID: admin | Space: Student Union Multipurpose Room | Space ID: 2 | Building: Student Union | Date: 2026-07-08 | Start: 13:00 | End: 14:30
- Reservation ID: 3 | User ID: student | Space: University Center North Meeting Room | Space ID: 3 | Building: University Center | Date: 2026-07-09 | Start: 11:00 | End: 12:00

### Status

Pass

## Test Case 3: Report Includes Owner and Space Information

### Inputs Used

- Logged-in user: admin
- Owner/user ID field
- Space name and space ID fields
- Building field
- Date and time fields

### Steps

1. Ran the application using `.\mvnw.cmd javafx:run`.
2. Logged in as `admin`.
3. Opened the Reservations Report tab.
4. Checked the displayed reservation rows for owner and space information.

### Expected Result

Each reservation row includes owner information and space information.

### Actual Result

Each displayed reservation included reservation ID, user ID, space name, space ID, building, date, start time, and end time.

### Status

Pass

## Test Case 4: Report Is Sorted Chronologically

### Inputs Used

- Logged-in user: admin
- Multiple reservations with different dates and times

### Steps

1. Ran the application using `.\mvnw.cmd javafx:run`.
2. Logged in as `admin`.
3. Opened the Reservations Report tab.
4. Checked the order of the displayed reservations.

### Expected Result

Reservations are displayed in chronological order by date and time.

### Actual Result

The report displayed reservation ID `1` on `2026-07-08` from `11:00 - 12:00`, then reservation ID `2` on `2026-07-08` from `13:00 - 14:30`, then reservation ID `3` on `2026-07-09` from `11:00 - 12:00`. This order is chronological by date and start time.

### Status

Pass

## Test Case 5: Multiple Users Are Represented Accurately

### Inputs Used

- Logged-in user: admin
- Reservations owned by `student`
- Reservations owned by `admin`

### Steps

1. Ran the application using `.\mvnw.cmd javafx:run`.
2. Logged in as `admin`.
3. Opened the Reservations Report tab.
4. Checked that reservations for multiple users appeared.

### Expected Result

The report shows reservations owned by different users and does not limit the report to only the logged-in admin.

### Actual Result

The report displayed reservations owned by both `student` and `admin`.

### Status

Pass

## Test Case 6: Refresh Reloads Report Data

### Inputs Used

- Logged-in user: admin
- Action: Refresh Reservations Report button

### Steps

1. Ran the application using `.\mvnw.cmd javafx:run`.
2. Logged in as `admin`.
3. Opened the Reservations Report tab.
4. Clicked Refresh Reservations Report.
5. Observed the displayed reservation rows.

### Expected Result

The report reloads reservation data from persistence and displays the current saved data.

### Actual Result

The report refreshed and continued to display the current reservation data from persistence. The message still showed `Reservations found`.

### Status

Pass

## Test Case 7: Regular User Is Denied Access

### Inputs Used

- User type: Regular user
- Logged-in user: student
- View attempted: Reservations Report

### Steps

1. Ran the application using `.\mvnw.cmd javafx:run`.
2. Logged in as `student`.
3. Attempted to open the Reservations Report tab.
4. Observed the result.

### Expected Result

The application denies access to the all-reservations report for regular users.

### Actual Result

The regular user was not allowed to access the administrator all-reservations report.

### Status

Pass

## Test Case 8: Report Data Persists After Restart

### Inputs Used

- Logged-in user: admin
- Data source: `reservations.json`
- Action: close and restart application

### Steps

1. Ran the application using `.\mvnw.cmd javafx:run`.
2. Logged in as `admin`.
3. Opened the Reservations Report tab.
4. Recorded the displayed reservations.
5. Closed the application.
6. Started the application again using `.\mvnw.cmd javafx:run`.
7. Logged in as `admin`.
8. Opened the Reservations Report tab again.
9. Compared the displayed reservations after restart.

### Expected Result

The same persisted reservation data appears after the application restarts.

### Actual Result

The Reservations Report still displayed the saved reservation data after restarting the application.

### Status

Pass

## Runtime Data Restoration

After testing, temporary reservation data changes were restored so the application data returned to its original test state.

### Restored Data

- Restored reservation data source: `reservations.json`
- Restored users tested: admin and student
- Restored result: reservation report data returned to the original test state

## Bugs Found

No bugs found during manual testing.

## Follow-Up Issues

No follow-up issues were created.

## Demo Readiness

US-23 All Reservations Reporting is ready to demonstrate in the Sprint 2 video.

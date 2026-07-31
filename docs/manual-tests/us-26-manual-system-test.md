# US-26 Manual System Test: CSV Report Export

## User Story

As an administrator, I want to export reports as CSV files so that I can save or review report data outside of the application.

## Test Environment

- Application: ReservationSystem JavaFX app
- Command used to run app: `.\mvnw.cmd javafx:run`
- Tester: Almondmlk
- Date tested: July 31, 2026
- Tested branch: main
- Tested commit/SHA: ec6cea2
- Logged-in admin user: admin
- User role: Administrator

## Test Case 1: Admin Can Export the All Reservations Report

### Inputs Used

- User type: Administrator
- Logged-in user: admin
- Report opened: Reservations Report
- Export action: Export CSV
- Exported file name: `reservations-report.csv`
- Data source: `reservations.json`

### Steps

1. Ran the application using `.\mvnw.cmd javafx:run`.
2. Logged in as `admin`.
3. Opened the Reservations Report tab.
4. Confirmed that reservation report data was displayed.
5. Clicked Export CSV.
6. Chose a save location and file name.
7. Opened the exported CSV file.

### Expected Result

The application exports the currently displayed all-reservations report to a CSV file.

### Actual Result

The application exported the Reservations Report successfully and displayed `Report exported successfully`.

### Status

Pass

## Test Case 2: All Reservations CSV Contains Correct Headers and Fields

### Inputs Used

- Report opened: Reservations Report
- Exported file: `reservations-report.csv`

### Steps

1. Opened the exported all-reservations CSV file.
2. Checked the header row.
3. Checked the reservation rows and fields.

### Expected Result

The CSV file includes headers and fields for reservation ID, user ID, space information, building, date, start time, and end time.

### Actual Result

The exported Reservations Report CSV included the correct headers:

`Reservation ID, Space ID, Space Name, Building, User ID, Date, Start Time, End Time`

The CSV contained these rows:

- Reservation ID: 1 | Space ID: 1 | Space Name: Student Union Conference Room 1 | Building: Student Union | User ID: student | Date: 2026-07-08 | Start Time: 11:00 | End Time: 12:00
- Reservation ID: 2 | Space ID: 2 | Space Name: Student Union Multipurpose Room | Building: Student Union | User ID: admin | Date: 2026-07-08 | Start Time: 13:00 | End Time: 14:30
- Reservation ID: 3 | Space ID: 3 | Space Name: University Center North Meeting Room | Building: University Center | User ID: student | Date: 2026-07-09 | Start Time: 11:00 | End Time: 12:00

### Status

Pass

## Test Case 3: All Reservations CSV Matches Displayed Report Data

### Inputs Used

- Logged-in user: admin
- Displayed report rows:
  - Reservation ID: 1 | User ID: student | Space: Student Union Conference Room 1 | Space ID: 1 | Building: Student Union | Date: 2026-07-08 | Start: 11:00 | End: 12:00
  - Reservation ID: 2 | User ID: admin | Space: Student Union Multipurpose Room | Space ID: 2 | Building: Student Union | Date: 2026-07-08 | Start: 13:00 | End: 14:30
  - Reservation ID: 3 | User ID: student | Space: University Center North Meeting Room | Space ID: 3 | Building: University Center | Date: 2026-07-09 | Start: 11:00 | End: 12:00
- Exported file: `reservations-report.csv`

### Steps

1. Opened the Reservations Report tab.
2. Recorded the displayed reservation rows.
3. Exported the report as a CSV file.
4. Opened the exported CSV file.
5. Compared the CSV rows against the displayed report rows.

### Expected Result

The CSV contents match the currently displayed Reservations Report data.

### Actual Result

The exported CSV rows matched the displayed reservation report data.

### Status

Pass

## Test Case 4: Admin Can Export the Space Usage Report

### Inputs Used

- User type: Administrator
- Logged-in user: admin
- Report opened: Usage Report
- Export action: Export CSV
- Exported file name: `space-usage-report.csv`
- Data source: `reservations.json`

### Steps

1. Ran the application using `.\mvnw.cmd javafx:run`.
2. Logged in as `admin`.
3. Opened the Usage Report tab.
4. Confirmed that space usage data was displayed.
5. Clicked Export CSV.
6. Chose a save location and file name.
7. Opened the exported CSV file.

### Expected Result

The application exports the currently displayed space usage report to a CSV file.

### Actual Result

The application exported the Usage Report successfully and displayed `Report exported successfully`.

### Status

Pass

## Test Case 5: Usage Report CSV Contains Correct Headers and Fields

### Inputs Used

- Report opened: Usage Report
- Exported file: `space-usage-report.csv`

### Steps

1. Opened the exported usage report CSV file.
2. Checked the header row.
3. Checked the space usage rows and fields.

### Expected Result

The CSV file includes headers and fields for space ID, space name, building, capacity, and reservation count.

### Actual Result

The exported Usage Report CSV included the correct headers:

`Space ID, Space Name, Building, Capacity, Reservation Count`

The CSV contained these rows:

- Space ID: 5 | Space Name: Nevins Hall Computer Lab | Building: Nevins Hall | Capacity: 30 | Reservation Count: 0
- Space ID: 4 | Space Name: Odum Library Study Room | Building: Odum Library | Capacity: 8 | Reservation Count: 0
- Space ID: 1 | Space Name: Student Union Conference Room 1 | Building: Student Union | Capacity: 10 | Reservation Count: 1
- Space ID: 2 | Space Name: Student Union Multipurpose Room | Building: Student Union | Capacity: 450 | Reservation Count: 1
- Space ID: 3 | Space Name: University Center North Meeting Room | Building: University Center | Capacity: 40 | Reservation Count: 1

### Status

Pass

## Test Case 6: Usage Report CSV Matches Displayed Report Data

### Inputs Used

- Logged-in user: admin
- Displayed usage report rows:
  - Nevins Hall Computer Lab | Building: Nevins Hall | Capacity: 30 | 0 reservations
  - Odum Library Study Room | Building: Odum Library | Capacity: 8 | 0 reservations
  - Student Union Conference Room 1 | Building: Student Union | Capacity: 10 | 1 reservation
  - Student Union Multipurpose Room | Building: Student Union | Capacity: 450 | 1 reservation
  - University Center North Meeting Room | Building: University Center | Capacity: 40 | 1 reservation
- Exported file: `space-usage-report.csv`

### Steps

1. Opened the Usage Report tab.
2. Recorded the displayed usage report rows.
3. Exported the report as a CSV file.
4. Opened the exported CSV file.
5. Compared the CSV rows against the displayed usage report rows.

### Expected Result

The CSV contents match the currently displayed Usage Report data.

### Actual Result

The exported CSV rows matched the displayed Usage Report data.

### Status

Pass

## Test Case 7: Filtered Report Export Matches Filtered Data

### Inputs Used

- Logged-in user: admin
- Report opened: Reservations Report or Usage Report
- Start date filter: 2026-07-08
- End date filter: 2026-07-08
- Export action: Export CSV

### Steps

1. Ran the application using `.\mvnw.cmd javafx:run`.
2. Logged in as `admin`.
3. Opened the report tab.
4. Entered `2026-07-08` as the start date.
5. Entered `2026-07-08` as the end date.
6. Clicked Apply.
7. Confirmed that the report displayed filtered data.
8. Clicked Export CSV.
9. Opened the exported CSV file.
10. Compared the CSV rows against the filtered report.

### Expected Result

The exported CSV only includes the currently displayed filtered report data.

### Actual Result

The exported CSV matched the filtered report data shown in the application.

### Status

Pass

## Test Case 8: CSV Handles Empty or No-Data Report State

### Inputs Used

- Logged-in user: admin
- Date filter with no matching reservations: no matching test date range
- Export action: Export CSV

### Steps

1. Ran the application using `.\mvnw.cmd javafx:run`.
2. Logged in as `admin`.
3. Opened the report tab.
4. Entered a date range with no matching reservation data.
5. Clicked Apply.
6. Confirmed that the report displayed a controlled no-data state.
7. Clicked Export CSV.
8. Opened the exported CSV file or observed the application message.

### Expected Result

The application handles exporting an empty report without crashing.

### Actual Result

The application handled the no-data export case without crashing.

### Status

Pass

## Test Case 9: File Chooser Cancellation Is Controlled

### Inputs Used

- Logged-in user: admin
- Report opened: Reservations Report or Usage Report
- Export action: Export CSV
- File chooser action: Cancel

### Steps

1. Ran the application using `.\mvnw.cmd javafx:run`.
2. Logged in as `admin`.
3. Opened a report tab.
4. Clicked Export CSV.
5. Canceled the file chooser instead of saving a file.
6. Observed the application behavior.

### Expected Result

The application cancels the export without crashing or creating an unwanted file.

### Actual Result

Canceling the file chooser did not crash the application and no unwanted CSV file was created.

### Status

Pass

## Test Case 10: Regular User Is Denied CSV Export Access

### Inputs Used

- User type: Regular user
- Logged-in user: student
- View attempted: Administrator report export

### Steps

1. Ran the application using `.\mvnw.cmd javafx:run`.
2. Logged in as `student`.
3. Attempted to access administrator report export functionality.
4. Observed the result.

### Expected Result

The application denies access to administrator report export features for regular users.

### Actual Result

The regular user was not allowed to access the administrator CSV report export functionality.

### Status

Pass

## Test Case 11: Temporary Export Files Are Cleaned Up

### Inputs Used

- Exported files:
  - `reservations-report.csv`
  - `space-usage-report.csv`

### Steps

1. Exported the report CSV files.
2. Verified the file contents.
3. Deleted temporary CSV files after testing.

### Expected Result

Temporary files created during testing are removed after verification.

### Actual Result

Temporary CSV files were removed after testing.

### Status

Pass

## Runtime Data Restoration

After testing, temporary reservation or report data changes were restored so the application returned to its original test state.

### Restored Data

- Restored reservation data source: `reservations.json`
- Restored users tested: admin and student
- Restored result: report data and exported test files were returned to the original test state or removed after testing

## Bugs Found

No bugs found during manual testing.

## Follow-Up Issues

No follow-up issues were created.

## Demo Readiness

US-26 CSV Report Export is ready to demonstrate in the Sprint 2 video.

# Sprint 2 Manual Regression Test

## Scope

This document records one end-to-end manual JavaFX regression pass across the Sprint 2 workflows for US-13 through US-26. The purpose of this test was to verify that the major Sprint 2 user stories still work together after implementation, testing, and documentation work was completed.

## Test Environment

- Application: ReservationSystem JavaFX Maven app
- Command used to run app: `./mvnw javafx:run`
- Tester: Almondmlk
- Date tested: July 31, 2026
- Tested branch: main
- Tested commit/SHA: ec6cea2
- Java version: JDK 26.0.1
- Operating system: Windows
- Shell used: Git Bash
- Admin user tested: admin
- Regular user tested: student

## Commands Run

```bash
cd ~/Documents/ReservationSystem
git checkout main
git pull
git status
git rev-parse --short HEAD
export JAVA_HOME="/c/Program Files/Java/jdk-26.0.1"
chmod +x mvnw
./mvnw javafx:run
git status
```

## Expected Result

The application should complete one end-to-end manual regression pass across Sprint 2 workflows. Login/logout, role visibility, reservation ownership, administrator create/modify/cancel behavior, persistence, daily summary, all-reservations reporting, usage reporting, and CSV export behavior should all work without crashes. The regression pass should include representative success, invalid, unauthorized, empty, and restart paths.

## Manual Regression Results

## Test Case 1: Application Starts Successfully

### Steps

1. Ran the application using `./mvnw javafx:run`.
2. Confirmed that the JavaFX application opened successfully.
3. Logged in as `admin`.

### Expected Result

The application starts without crashing and allows login.

### Actual Result

The application opened successfully and showed `Logged in as: admin (Administrator)`.

### Status

Pass

## Test Case 2: Admin Login and Role Visibility

### Steps

1. Logged in as `admin`.
2. Checked the visible tabs in the JavaFX application.
3. Opened the tab overflow menu to view additional tabs.

### Expected Result

The admin user can access administrator workflows, including reservation management and reports.

### Actual Result

The admin user was able to access administrator features. The visible tabs and overflow menu included Spaces, Availability, Create Reservation, My Reservations, Daily Summary, Admin Create Reservation, Admin Cancel Reservation, Admin Modify Reservation, Reservations Report, and Usage Report.

### Status

Pass

## Test Case 3: Registration Workflow Visibility

### Steps

1. Logged in as `admin`.
2. Checked the visible tabs in the JavaFX application.
3. Opened the tab overflow menu to view hidden tabs.
4. Looked for a registration or user registration workflow.

### Expected Result

If registration is part of the current application UI, the registration workflow should be visible or accessible.

### Actual Result

The visible tabs and overflow menu included Spaces, Availability, Create Reservation, My Reservations, Daily Summary, Admin Create Reservation, Admin Cancel Reservation, Admin Modify Reservation, Reservations Report, and Usage Report. A Registration tab was not visible during this manual regression pass.

### Status

Needs follow-up

## Test Case 4: Availability Workflow

### Steps

1. Opened the Availability tab.
2. Selected Nevins Hall Computer Lab.
3. Selected the date `7/23/2026`.
4. Selected the range end date `7/24/2026`.
5. Loaded availability.

### Expected Result

The application displays availability information for the selected space and date.

### Actual Result

The Availability tab displayed availability for Nevins Hall Computer Lab. The application showed multiple available time slots, including 8:00 AM - 8:30 AM, 8:30 AM - 9:00 AM, 9:00 AM - 9:30 AM, and later available slots.

### Status

Pass

## Test Case 5: Create Reservation Workflow

### Steps

1. Opened the Create Reservation tab.
2. Selected Nevins Hall Computer Lab.
3. Selected the date `7/31/2026`.
4. Entered a duration of `60` minutes.
5. Clicked Suggest Times.
6. Selected the suggested time `09:00 - 10:00`.
7. Created the reservation.

### Expected Result

A valid reservation can be created and saved.

### Actual Result

The application created the reservation successfully and displayed `Reservation created successfully`.

### Status

Pass

## Test Case 6: My Reservations Ownership Behavior

### Steps

1. Opened the My Reservations tab.
2. Clicked View My Reservations.
3. Checked the reservations displayed for the logged-in admin user.

### Expected Result

The My Reservations workflow displays reservations owned by the logged-in user.

### Actual Result

The My Reservations tab displayed reservations for the logged-in admin user, including Reservation ID 2 and the newly created Reservation ID 4 for Nevins Hall Computer Lab on `2026-07-31` from `09:00` to `10:00`.

### Status

Pass

## Test Case 7: Admin Modify Reservation

### Steps

1. Opened the Admin Modify Reservation tab.
2. Selected Reservation ID 1.
3. Modified the reservation time to `10:00` through `11:00`.
4. Clicked Modify Reservation.
5. Checked the confirmation message.

### Expected Result

The administrator can modify an existing reservation, and the updated information is saved.

### Actual Result

The application displayed `Reservation modified successfully`. The reservation list updated to show Reservation ID 1 with Start `10:00` and End `11:00`.

### Status

Pass

## Test Case 8: Admin Cancel Reservation

### Steps

1. Opened the Admin Cancel Reservation tab.
2. Selected the test reservation created during the regression pass.
3. Clicked Cancel Selected Reservation.
4. Checked the confirmation message and reservation list.

### Expected Result

The administrator can cancel an existing reservation.

### Actual Result

The application displayed `Reservation cancelled successfully`. The reservation list updated and the test reservation created during the regression pass was no longer displayed.

### Status

Pass

## Test Case 9: Daily Reservation Summary

### Steps

1. Opened the Daily Summary tab.
2. Refreshed the daily summary.
3. Checked the displayed reservation summary.

### Expected Result

The application displays a daily reservation summary based on saved reservation data.

### Actual Result

The Daily Summary tab displayed reservations grouped by date. The summary showed `2026-07-08 — 1 reservation` and listed Reservation ID 2 for Student Union Multipurpose Room from `13:00` to `14:30`.

### Status

Pass

## Test Case 10: All Reservations Report

### Steps

1. Opened the Reservations Report tab.
2. Reviewed the displayed reservation rows.
3. Confirmed that all saved reservations were displayed across users.

### Expected Result

The administrator can view all persisted reservations across users.

### Actual Result

The Reservations Report displayed all saved reservations and showed `Reservations found`. The report included reservation ID, user ID, space name, space ID, building, date, start time, and end time.

The displayed reservations included:

- Reservation ID: 1 | User ID: student | Space: Student Union Conference Room 1 | Space ID: 1 | Building: Student Union | Date: 2026-07-08 | Start: 10:00 | End: 11:00
- Reservation ID: 2 | User ID: admin | Space: Student Union Multipurpose Room | Space ID: 2 | Building: Student Union | Date: 2026-07-08 | Start: 13:00 | End: 14:30
- Reservation ID: 3 | User ID: student | Space: University Center North Meeting Room | Space ID: 3 | Building: University Center | Date: 2026-07-09 | Start: 11:00 | End: 12:00

### Status

Pass

## Test Case 11: Space Usage Report

### Steps

1. Opened the Usage Report tab.
2. Refreshed the usage report.
3. Reviewed the displayed space usage counts.

### Expected Result

The administrator can view usage counts for spaces.

### Actual Result

The Usage Report displayed `Showing usage for 5 spaces.` The report showed spaces with reservations and spaces with zero reservations.

The displayed usage report included:

- Nevins Hall Computer Lab | Building: Nevins Hall | Capacity: 30 | 0 reservations
- Odum Library Study Room | Building: Odum Library | Capacity: 8 | 0 reservations
- Student Union Conference Room 1 | Building: Student Union | Capacity: 10 | 1 reservation
- Student Union Multipurpose Room | Building: Student Union | Capacity: 450 | 1 reservation
- University Center North Meeting Room | Building: University Center | Capacity: 40 | 1 reservation

### Status

Pass

## Test Case 12: CSV Export

### Steps

1. Opened the Reservations Report tab.
2. Clicked Export CSV.
3. Saved and opened the exported reservations report CSV file.
4. Opened the Usage Report tab.
5. Clicked Export CSV.
6. Saved and opened the exported usage report CSV file.

### Expected Result

The application exports report data as CSV files without crashing, and the exported CSV data matches the displayed report data.

### Actual Result

Both report CSV exports completed successfully. The reservations report CSV included the correct headers:

`Reservation ID,Space ID,Space Name,Building,User ID,Date,Start Time,End Time`

The usage report CSV included the correct headers:

`Space ID,Space Name,Building,Capacity,Reservation Count`

The exported CSV files matched the displayed report data.

### Status

Pass

## Test Case 13: Empty or Zero-Count Path

### Steps

1. Opened the Usage Report tab.
2. Checked spaces with no reservations.

### Expected Result

The application handles spaces with no reservations without crashing.

### Actual Result

The Usage Report displayed zero-count spaces correctly. Nevins Hall Computer Lab and Odum Library Study Room both displayed `0 reservations`.

### Status

Pass

## Test Case 14: Regular User Role Visibility

### Steps

1. Restarted the application.
2. Logged in as `student`.
3. Checked the visible tabs in the JavaFX application.
4. Confirmed which workflows were available to the regular user.

### Expected Result

A regular user should only see regular-user workflows and should not see administrator-only tabs.

### Actual Result

After restarting the app and logging in as `student (Regular User)`, the application displayed the regular user tabs: Spaces, Availability, Create Reservation, My Reservations, and Daily Summary. Administrator-only tabs such as Admin Create Reservation, Admin Cancel Reservation, Admin Modify Reservation, Reservations Report, and Usage Report were not visible.

### Status

Pass

## Test Case 15: Restart and Persistence

### Steps

1. Closed or restarted the application.
2. Logged back into the application as `student`.
3. Opened the Spaces tab.
4. Checked whether saved application data still appeared.

### Expected Result

Saved application data should still appear after the application restarts.

### Actual Result

After restarting the application, the saved data still appeared. The Spaces tab displayed the saved reservable spaces, including Nevins Hall Computer Lab, Odum Library Study Room, Student Union Conference Room 1, Student Union Multipurpose Room, and University Center North Meeting Room.

### Status

Pass

## Test Case 16: Persistence After Changes

### Steps

1. Created a test reservation.
2. Viewed the reservation in My Reservations.
3. Modified an existing reservation as admin.
4. Canceled the test reservation.
5. Checked reports after the changes.

### Expected Result

Saved reservation changes should appear across the correct workflows and reports.

### Actual Result

The created reservation appeared in My Reservations, the modified reservation appeared with the updated time in Admin Modify Reservation and Reservations Report, and the canceled test reservation was removed from the active reservation list.

### Status

Pass

## Repository/Data Restoration

After the manual regression pass, runtime data was checked and restored as needed so the application returned to the expected test state.

### Result

The temporary reservation created during testing was canceled. Exported CSV files were checked after testing. No unintended repository changes were expected from this manual regression pass.

## Bugs Found

No application crash occurred during this manual regression pass.

A follow-up may be needed for registration workflow visibility because the Registration tab was not visible in the JavaFX UI during this regression pass.

## Follow-Up Issues

A follow-up may be needed to confirm whether the registration workflow is expected to appear in the JavaFX UI or whether registration is only covered through backend/service tests.

## Demo Readiness

Sprint 2 manual regression is ready to reference for the Sprint 2 demo and submission evidence. The tested workflows passed, including admin workflows, regular-user visibility, reporting, CSV export, zero-count paths, and restart persistence. A follow-up may still be needed only to confirm whether registration is expected to appear in the JavaFX UI.

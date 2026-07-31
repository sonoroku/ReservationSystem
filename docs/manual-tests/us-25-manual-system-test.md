# US-25 Manual System Test: Report Date Filtering

## User Story

As an administrator, I want to filter reports by date so that I can view reservation and usage data for a specific time period.

## Test Environment

- Application: ReservationSystem JavaFX app
- Command used to run app: `./mvnw javafx:run`
- Tester: Almondmlk
- Date tested: July 31, 2026
- Tested branch: main
- Tested commit/SHA: ec6cea2
- Java version: JDK 26.0.1
- Operating system: Windows
- Shell used: Git Bash
- Logged-in admin user: admin
- User role: Administrator

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

## Test Case 1: Reservations Report Single-Day Filter for July 8, 2026

### Inputs Used

- View opened: Reservations Report
- Start date: 7/8/2026
- End date: 7/8/2026

### Steps

1. Ran the application using `./mvnw javafx:run`.
2. Logged in as `admin`.
3. Opened the Reservations Report tab.
4. Entered `7/8/2026` as the start date.
5. Entered `7/8/2026` as the end date.
6. Clicked Apply.
7. Observed the filtered reservation rows.

### Expected Result

The Reservations Report displays only reservations dated July 8, 2026.

### Actual Result

The Reservations Report displayed only reservations dated `2026-07-08`. The filtered report showed Reservation ID 1 and Reservation ID 2. Reservation ID 3, which is dated `2026-07-09`, was not shown.

### Status

Pass

## Test Case 2: Reservations Report Single-Day Filter for July 9, 2026

### Inputs Used

- View opened: Reservations Report
- Start date: 7/9/2026
- End date: 7/9/2026

### Steps

1. Opened the Reservations Report tab.
2. Entered `7/9/2026` as the start date.
3. Entered `7/9/2026` as the end date.
4. Clicked Apply.
5. Observed the filtered reservation rows.

### Expected Result

The Reservations Report displays only reservations dated July 9, 2026.

### Actual Result

The Reservations Report displayed only Reservation ID 3, which was dated `2026-07-09`. Reservations dated `2026-07-08` were not shown.

### Status

Pass

## Test Case 3: Reservations Report Multi-Day Filter

### Inputs Used

- View opened: Reservations Report
- Start date: 7/8/2026
- End date: 7/9/2026

### Steps

1. Opened the Reservations Report tab.
2. Entered `7/8/2026` as the start date.
3. Entered `7/9/2026` as the end date.
4. Clicked Apply.
5. Observed the filtered reservation rows.

### Expected Result

The Reservations Report displays reservations from July 8, 2026 through July 9, 2026, inclusive.

### Actual Result

The Reservations Report displayed the reservations from both dates. The filtered range included the reservations dated `2026-07-08` and the reservation dated `2026-07-09`.

### Status

Pass

## Test Case 4: Usage Report Single-Day Filter for July 8, 2026

### Inputs Used

- View opened: Usage Report
- Start date: 7/8/2026
- End date: 7/8/2026

### Steps

1. Opened the Usage Report tab.
2. Entered `7/8/2026` as the start date.
3. Entered `7/8/2026` as the end date.
4. Clicked Apply.
5. Observed the recalculated usage counts.

### Expected Result

The Usage Report recalculates space usage counts using only reservations dated July 8, 2026.

### Actual Result

The Usage Report displayed `Showing usage for 5 spaces.` The report showed 1 reservation for Student Union Conference Room 1, 1 reservation for Student Union Multipurpose Room, and 0 reservations for University Center North Meeting Room. Nevins Hall Computer Lab and Odum Library Study Room also remained visible with 0 reservations.

### Status

Pass

## Test Case 5: Usage Report Single-Day Filter for July 9, 2026

### Inputs Used

- View opened: Usage Report
- Start date: 7/9/2026
- End date: 7/9/2026

### Steps

1. Opened the Usage Report tab.
2. Entered `7/9/2026` as the start date.
3. Entered `7/9/2026` as the end date.
4. Clicked Apply.
5. Observed the recalculated usage counts.

### Expected Result

The Usage Report recalculates space usage counts using only reservations dated July 9, 2026.

### Actual Result

The Usage Report displayed `Showing usage for 5 spaces.` The report showed 1 reservation for University Center North Meeting Room and 0 reservations for the other spaces.

### Status

Pass

## Test Case 6: Usage Report Multi-Day Filter

### Inputs Used

- View opened: Usage Report
- Start date: 7/8/2026
- End date: 7/9/2026

### Steps

1. Opened the Usage Report tab.
2. Entered `7/8/2026` as the start date.
3. Entered `7/9/2026` as the end date.
4. Clicked Apply.
5. Observed the recalculated usage counts.

### Expected Result

The Usage Report recalculates usage counts using all reservations from July 8, 2026 through July 9, 2026, inclusive.

### Actual Result

The Usage Report displayed `Showing usage for 5 spaces.` The report showed 1 reservation each for Student Union Conference Room 1, Student Union Multipurpose Room, and University Center North Meeting Room. Nevins Hall Computer Lab and Odum Library Study Room remained visible with 0 reservations.

### Status

Pass

## Test Case 7: Clear Restores Full Usage Report

### Inputs Used

- View opened: Usage Report
- Prior filter: 7/8/2026 through 7/9/2026
- Action: Clear

### Steps

1. Opened the Usage Report tab.
2. Applied a date filter.
3. Clicked Clear.
4. Observed the report results after clearing the filter.

### Expected Result

The Clear button removes the date filter and restores the full Usage Report.

### Actual Result

After clicking Clear, the date fields were cleared and the full Usage Report was restored. The report showed 5 spaces with the current unfiltered usage counts.

### Status

Pass

## Test Case 8: No-Data Date Range on Usage Report

### Inputs Used

- View opened: Usage Report
- Start date: 7/10/2026
- End date: 7/11/2026

### Steps

1. Opened the Usage Report tab.
2. Entered `7/10/2026` as the start date.
3. Entered `7/11/2026` as the end date.
4. Clicked Apply.
5. Observed the displayed usage counts.

### Expected Result

The Usage Report handles a date range with no matching reservations without crashing and displays zero counts where appropriate.

### Actual Result

The Usage Report handled the no-data date range without crashing. It displayed `Showing usage for 5 spaces.` All spaces remained visible and showed 0 reservations.

### Status

Pass

## Test Case 9: Zero-Count Spaces Remain Visible

### Inputs Used

- View opened: Usage Report
- Date filters tested:
  - 7/8/2026 to 7/8/2026
  - 7/9/2026 to 7/9/2026
  - 7/10/2026 to 7/11/2026

### Steps

1. Opened the Usage Report tab.
2. Applied different date filters.
3. Checked whether spaces with zero reservations were still displayed.

### Expected Result

Spaces with zero reservations remain visible after filtering.

### Actual Result

The Usage Report continued to show spaces with zero reservations after filtering. Nevins Hall Computer Lab and Odum Library Study Room remained visible with 0 reservations, and other spaces changed counts depending on the selected date range.

### Status

Pass

## Test Case 10: Reversed or Missing Date Validation

### Inputs Used

- View opened: Reservations Report or Usage Report
- Invalid cases:
  - Missing start date
  - Missing end date
  - Start date after end date

### Steps

1. Opened the report date filter controls.
2. Tested missing date input.
3. Tested reversed date input with the start date after the end date.
4. Observed the application behavior.

### Expected Result

The application should handle missing or reversed date input in a controlled way and should not crash.

### Actual Result

The application handled missing or reversed date input as intended. The report workflow stayed stable and did not crash.

### Status

Pass

## Test Case 11: US-22 Daily Summary Regression Check

### Inputs Used

- View opened: Daily Summary

### Steps

1. Opened the Daily Summary tab after testing report date filtering.
2. Refreshed the summary.
3. Observed the displayed grouped reservations.

### Expected Result

US-22 Daily Summary behavior remains unaffected by the report date filtering functionality.

### Actual Result

The Daily Summary view continued to function after the report filtering tests. The summary loaded grouped reservation data correctly, confirming that US-22 behavior was not broken by report filtering.

### Status

Pass

## Runtime Data Restoration

The report date filtering tests did not require creating or canceling new reservations. No temporary runtime reservation data needed to be restored after this manual test session.

## Bugs Found

No bugs were found during manual testing.

## Follow-Up Issues

No follow-up issues were created.

## Demo Readiness

US-25 Report Date Filtering is ready to demonstrate in the Sprint 2 video. The tested report filtering workflows passed, including single-day filtering, multi-day filtering, clear behavior, no-data date ranges, reversed or missing date handling, recalculated usage counts, zero-count space visibility, and US-22 Daily Summary regression coverage.

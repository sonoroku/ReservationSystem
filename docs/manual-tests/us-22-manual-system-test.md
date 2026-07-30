# US-22 Daily Reservation Summary Manual System Test

## Test Information

- Application: ReservationSystem JavaFX application
- Tester: zb
- Date tested: July 30, 2026
- Tested commit: `72b87adea54bb205358ea39302ddfb63ab365ee6`
- Environment: Windows, JDK 25, Maven Wrapper, JavaFX
- Test command: `.\mvnw.cmd test`
- Application command: `.\mvnw.cmd javafx:run`
- Automated result: 221 tests passed with 0 failures and 0 errors
- Application result: JavaFX application launched and operated successfully

## Purpose

Verify that the daily-summary view groups the current user's reservations by date, displays accurate counts, preserves chronological ordering, excludes other users' reservations, handles empty data, refreshes correctly, and reloads persisted information after restart.

## Test Results

### Test 1: Reservations across multiple dates

**Expected result:**  
The current user's reservations are grouped under their corresponding dates.

**Actual result:**  
Reservations from multiple dates appeared under the correct date groups.

**Result:** PASS

### Test 2: Multiple reservations on the same date

**Expected result:**  
All reservations belonging to the current user on the same date appear in the same daily group.

**Actual result:**  
Multiple same-day reservations were displayed together under the correct date.

**Result:** PASS

### Test 3: Chronological ordering

**Expected result:**  
Date groups appear chronologically, and reservations within each date appear in chronological start-time order.

**Actual result:**  
Dates and their reservations were displayed in chronological order.

**Result:** PASS

### Test 4: Daily reservation counts

**Expected result:**  
Each date displays a count equal to the number of current-user reservations assigned to that date.

**Actual result:**  
The displayed counts matched the reservations shown in each date group.

**Result:** PASS

### Test 5: Other-user exclusion

**Expected result:**  
Reservations owned by other users are excluded from the current user's daily summary.

**Actual result:**  
Only reservations belonging to the authenticated current user appeared in the summary.

**Result:** PASS

### Test 6: Empty summary

**Expected result:**  
A user with no reservations receives a clear empty-state message.

**Actual result:**  
The application displayed a clear empty-state message and remained stable.

**Result:** PASS

### Test 7: Refresh behavior

**Expected result:**  
Refreshing reloads current persistence data and updates the displayed groups and counts.

**Actual result:**  
The summary refreshed successfully and displayed the latest reservation information.

**Result:** PASS

### Test 8: Persistence after restart

**Expected result:**  
Persisted current-user reservations reappear with the same dates, ordering, and counts after restarting the application.

**Actual result:**  
The application reloaded persisted reservations after restart, and the daily summary remained accurate.

**Result:** PASS

## Data Restoration

Runtime reservation data used during verification was restored after testing. No unintended changes remained in `app-data/reservations.json`.

## Automated Regression

The complete automated suite was executed at commit:

`72b87adea54bb205358ea39302ddfb63ab365ee6`

Results:

- Tests run: 221
- Failures: 0
- Errors: 0
- Build result: SUCCESS

## Defects and Follow-Up Work

No daily-summary defects were identified.

## Demo Readiness

US-22 is ready for demonstration. Date grouping, chronological ordering, counts, ownership filtering, empty-state handling, refreshing, and restart persistence behaved as expected.
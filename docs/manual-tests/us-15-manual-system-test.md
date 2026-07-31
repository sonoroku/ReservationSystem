# US-15 Role-Based Access Manual System Test

## Test Information

- Application: ReservationSystem JavaFX application
- Tester: zb
- Date tested: July 30, 2026
- Tested commit: `72b87adea54bb205358ea39302ddfb63ab365ee6`
- Environment: Windows, JDK 25, Maven Wrapper, JavaFX
- Test command: `.\mvnw.cmd test`
- Application command: `.\mvnw.cmd javafx:run`
- Automated result: 221 tests executed with 0 failures and 0 errors
- Application result: JavaFX application launched successfully

## Purpose

Verify that regular users and administrators receive the correct feature access, unauthorized administrator actions are rejected, and rejected actions do not modify persisted data.

This record consolidates role-related behavior previously exercised during the US-14 authentication, US-17 administrator creation, US-18 administrator modification, and US-19 administrator cancellation workflows.

## Test Results

### Test 1: Regular-user feature visibility

**Expected result:**  
A regular user can access normal reservation features but cannot view administrator-only management and reporting features.

**Actual result:**  
After logging in as a regular user, normal reservation functionality was available and administrator-only interactions were not available.

**Result:** PASS

### Test 2: Administrator feature visibility

**Expected result:**  
An administrator can access both normal user functionality and administrator-only reservation-management and reporting functionality.

**Actual result:**  
After logging in as an administrator, normal reservation features and administrator-only features were available.

**Result:** PASS

### Test 3: Regular-user attempt at administrator reservation creation

**Expected result:**  
A regular user cannot create a reservation on behalf of another user. The operation is rejected with controlled access-denied feedback.

**Actual result:**  
The administrator creation workflow rejected regular-user access and did not create a reservation.

**Result:** PASS

### Test 4: Regular-user attempt at administrator reservation modification

**Expected result:**  
A regular user cannot use the administrator override to modify another user's reservation.

**Actual result:**  
The administrator modification operation rejected the regular user and did not modify the reservation.

**Result:** PASS

### Test 5: Regular-user attempt at administrator reservation cancellation

**Expected result:**  
A regular user cannot cancel another user's reservation through the administrator workflow.

**Actual result:**  
The administrator cancellation operation rejected the regular user and displayed controlled access-denied behavior.

**Result:** PASS

### Test 6: Administrator actions

**Expected result:**  
An authenticated administrator can create, modify, and cancel reservations for other users while normal scheduling validation remains enforced.

**Actual result:**  
The administrator workflows permitted authorized actions and continued enforcing reservation validation rules.

**Result:** PASS

### Test 7: Persistence after rejected action

**Expected result:**  
A rejected administrator-only action must not create, modify, or remove persisted reservation data.

**Actual result:**  
Reservation data remained unchanged after unauthorized actions were rejected.

**Result:** PASS

### Test 8: Role change after logout and login

**Expected result:**  
Logging out removes the existing session. Logging in with a different account updates the available features according to the newly authenticated user's role.

**Actual result:**  
Logout returned the application to the login screen. Subsequent regular-user and administrator logins displayed the appropriate role-specific functionality.

**Result:** PASS

## Automated Regression

The complete automated test suite was executed at commit:

`72b87adea54bb205358ea39302ddfb63ab365ee6`

Results:

- Tests run: 221
- Failures: 0
- Errors: 0
- Build result: SUCCESS

The JavaFX application also launched and operated successfully.

## Defects and Follow-Up Work

No role-based access defects were identified.

## Demo Readiness

US-15 is ready for demonstration. Regular-user restrictions, administrator access, controlled authorization failures, session-role changes, and persistence protection behaved as expected.
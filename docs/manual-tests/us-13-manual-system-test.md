# US-13 Registration Manual System Test

## Test Information

- Application: ReservationSystem JavaFX app
- Command used to run app: `.\mvnw.cmd javafx:run`
- Tester: zb
- Date tested: July 23, 2026
- Branch: `docs/us13-registration-manual-test`
- Tested commit: `0bf022c688697531e15f3550044c8d01c0291f3c`
- Environment: Windows 11, Java 25.0.3
- Starter users: `student` (regular user) and `admin` (administrator)
- Runtime users file before testing: Did not exist
- Test account: `us13manualzb` / `secure123`

## Test Results

### 1. Missing Username

- Input: Blank username, password `secure123`, confirmation `secure123`
- Expected: Registration is rejected with a username-required message.
- Actual: Registration was rejected and the username-required message was displayed.
- Result: Pass

### 2. Missing Password

- Input: Username `us13manual`, blank password, blank confirmation
- Expected: Registration is rejected with clear required-field feedback.
- Actual: Registration was rejected and `Please confirm the password.` was displayed.
- Result: Pass

### 3. Password Shorter Than Six Characters

- Input: Username `us13manual`, password and confirmation `12345`
- Expected: Registration is rejected because the password is shorter than six characters.
- Actual: Registration was rejected and the minimum-length message was displayed.
- Result: Pass

### 4. Password Confirmation Mismatch

- Input: Username `us13manual`, password `secure123`, confirmation `secure124`
- Expected: Registration is rejected because the passwords do not match.
- Actual: Registration was rejected and `Passwords do not match.` was displayed.
- Result: Pass

### 5. Duplicate Username With Different Casing

- Input: Username `StUdEnT`, password and confirmation `secure123`
- Expected: Registration is rejected because `student` already exists, regardless of casing.
- Actual: Registration was rejected and the duplicate-username message was displayed.
- Result: Pass

### 6. Successful Registration

- Input: Username `us13manualzb`, password and confirmation `secure123`
- Expected: The account is created and the application returns to the login screen.
- Actual: Registration succeeded, the login screen reopened, and `Registration successful. Please log in.` was displayed.
- Result: Pass

### 7. Persistence After Restart

- Steps: Closed the application, relaunched it, and logged in as `us13manualzb`.
- Expected: The newly registered account remains available after restart.
- Actual: Login succeeded after restarting the application.
- Result: Pass

### 8. Regular-User Role Assignment

- Expected: A newly registered account is assigned the regular-user role.
- Actual: The session bar identified `us13manualzb` as a Regular User, and `app-data/users.json` stored `"isAdmin": false`.
- Result: Pass

### 9. Starter JSON Remains Unchanged

- Check: Ran `git diff -- src/main/resources/data/users.json`.
- Expected: Registration writes only to runtime data and does not modify starter JSON.
- Actual: The command produced no differences.
- Result: Pass

## Runtime Data Restoration

The test-created `app-data/users.json` file was removed after testing because no runtime users file existed before the test. The workspace was restored to its original runtime state.

## Defects and Follow-Up Work

No defects were found during this manual test.

## Demo Readiness

US-13 registration is ready for the Sprint 2 demonstration. Required validation, duplicate detection, successful registration, persistence, role assignment, and starter-data protection all behaved as expected.
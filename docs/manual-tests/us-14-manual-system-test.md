# US-14 Login and Logout Manual System Test

## Test Information

- Application: ReservationSystem JavaFX app
- Command used to run app: `.\mvnw.cmd javafx:run`
- Tester: zb
- Date tested: July 24, 2026
- Branch: `docs/us14-login-logout-manual-test`
- Tested commit: `0bf022c688697531e15f3550044c8d01c0291f3c`
- Environment: Windows 11, Java 25.0.3
- Regular account: `student`
- Administrator account: `admin`
- Runtime users file before testing: Did not exist

## Test Results

### 1. Invalid Username

- Input: Username `nonexistentuser`, password `student123`
- Expected: Login is rejected, an error is displayed, and no session is created.
- Actual: `Invalid username or password` was displayed, and the application remained on the login screen.
- Result: Pass

### 2. Invalid Password

- Input: Username `student`, password `wrongpassword`
- Expected: Login is rejected, an error is displayed, and no session is created.
- Actual: `Invalid username or password` was displayed, and the application remained on the login screen.
- Result: Pass

### 3. Valid Regular-User Login

- Input: Username `student`, password `student123`
- Expected: The main application opens with the regular user authenticated.
- Actual: The main application opened and displayed `Logged in as: student (Regular User)`.
- Result: Pass

### 4. Regular-User Logout

- Steps: Clicked Logout while logged in as `student`.
- Expected: The current session is cleared and the login screen returns.
- Actual: The main application closed, the login screen returned, and no authenticated session remained.
- Result: Pass

### 5. Valid Administrator Login

- Input: Username `admin`, password `admin123`
- Expected: The main application opens with the administrator authenticated.
- Actual: The main application opened and displayed `Logged in as: admin (Administrator)`.
- Result: Pass

### 6. Administrator Logout

- Steps: Clicked Logout while logged in as `admin`.
- Expected: The administrator session is cleared and the login screen returns.
- Actual: The login screen returned and no authenticated session remained.
- Result: Pass

### 7. Main-Screen Navigation

- Expected: Only successful authentication transitions from the login screen to the main application.
- Actual: Both valid accounts opened the main application. Invalid credentials remained on the login screen.
- Result: Pass

### 8. Current-User Identity

- Expected: The session bar displays the identity and role of the authenticated account.
- Actual: The session bar correctly identified `student` as a Regular User and `admin` as an Administrator.
- Result: Pass

## Runtime Data Restoration

The application-created `app-data/users.json` file was removed after testing because no runtime users file existed before the test. The starter users JSON remained unchanged.

## Defects and Follow-Up Work

No defects were found during this manual test.

## Demo Readiness

US-14 login and logout functionality is ready for the Sprint 2 demonstration. Valid regular and administrator credentials, invalid credentials, session identity, main-screen navigation, logout, and return-to-login behavior all worked as expected.
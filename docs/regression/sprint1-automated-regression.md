# Sprint 1 Automated Regression Test

## Test Information

- Tester: Zbreed05
- Test date: July 22, 2026
- Tested commit: `ceaf2b5c3c8b9b6426be3279d9345c86a10f4b1b`
- Environment: Windows, JDK 25, Maven Wrapper
- Command: `.\mvnw.cmd test`
- Working tree before test: Clean

## Full Test-Suite Result

- Tests discovered: 117
- Tests passed: 117
- Failures: 0
- Errors: 0
- Skipped: 0
- Maven result: BUILD SUCCESS

## Required Sprint 1 Regression Checks

### US-1: View All Spaces

- Test: `US1ViewAllSpacesIntegrationTest`
- Result: Pass

### US-5: View Daily Availability

- Test: `US5DayAvailabilityIntegrationTest`
- Result: Pass

### US-8: Create a Reservation

- Test: `US8CreateReservationIntegrationTest`
- Result: Pass

## Runtime Data Verification

A SHA-256 snapshot of every file under `app-data` was recorded before the test suite.

A second snapshot was recorded after the test suite completed.

`Compare-Object` reported no differences between the snapshots. The automated test suite did not modify runtime application data.

## Regressions

No automated-test regressions were found.

## Follow-Up Issues

No follow-up issues were required.

## Conclusion

The complete JUnit 5 test suite passed successfully. Required US-1, US-5, and US-8 integration coverage remained passing, and the test run left runtime application data unchanged. Sprint 1 automated regression is complete.
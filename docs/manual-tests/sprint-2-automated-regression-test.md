# Sprint 2 Automated Regression Test

## Scope

This document records the full automated regression test run for Sprint 2, covering US-13 through US-26 while verifying that earlier US-1 through US-12 regression tests still pass.

## Test Environment

- Application: ReservationSystem JavaFX Maven app
- Test command used: `./mvnw test`
- Tester: Almondmlk
- Date tested: July 31, 2026
- Tested branch: main
- Tested commit/SHA: ec6cea2
- Java version: JDK 26.0.1
- Operating system: Windows
- Shell used: Git Bash

## Commands Run

```bash
cd ~/Documents/ReservationSystem
git checkout main
git pull
git status
git rev-parse --short HEAD
export JAVA_HOME="/c/Program Files/Java/jdk-26.0.1"
chmod +x mvnw
./mvnw test
git status
```

## Expected Result

The full JUnit test suite runs with a nonzero test count, all automated tests pass, no failures or errors are reported, and the regression run does not leave runtime data, app data, or exported report files changed in the repository.

## Actual Result

The automated regression test suite completed successfully.

- Build result: BUILD SUCCESS
- Tests run: 221
- Failures: 0
- Errors: 0
- Skipped: 0
- Tested commit/SHA: ec6cea2
- Finished at: 2026-07-31T07:23:15-04:00
- Total time: 7.192 s

## Regression Coverage Notes

The automated regression run covered Sprint 2 backend and integration behavior for US-13 through US-26. The test run also verified that earlier Sprint 1 behavior for US-1 through US-12 continued to pass.

The regression suite included controller tests, service tests, persistence tests, integration tests, and view-related tests for reservation creation, modification, cancellation, summaries, reporting, CSV export behavior, authentication, authorization, registration, availability, and my-reservations behavior.

## Repository/Data Check

After the test run, `git status` was checked to confirm whether the test suite left changed files behind.

### Result

`git status` showed `nothing to commit, working tree clean`, so the regression run did not leave repository data changed.

## Failures or Follow-Up Issues

No automated test failures were found during this regression run.

No follow-up issues were created.

## Demo Readiness

The automated regression test suite is ready to reference for Sprint 2 demo and submission evidence.

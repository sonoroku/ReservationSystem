# Sprint 2 Traceability Matrix

## Purpose

This document maps Sprint 2 user stories to automated tests, manual system test documentation, and regression evidence. The goal is to make US-13 through US-26 traceable to verification evidence before Sprint 2 submission.

## Test Environment Used for Regression Evidence

- Application: ReservationSystem JavaFX Maven app
- Tester: Almondmlk
- Tested branch: main
- Tested commit/SHA: ec6cea2
- Java version: JDK 26.0.1
- Operating system: Windows
- Shell used: Git Bash
- Automated test command: `./mvnw test`
- Manual run command: `./mvnw javafx:run`

## Automated Regression Summary

The full Sprint 2 automated regression suite was run successfully.

- Tests run: 221
- Failures: 0
- Errors: 0
- Skipped: 0
- Build result: BUILD SUCCESS

Automated regression evidence is documented in:

```text
docs/manual-tests/sprint-2-automated-regression-test.md
```

## Manual Regression Summary

A cross-story manual JavaFX regression pass was completed across Sprint 2 workflows. Manual regression evidence is documented in:

```text
docs/manual-tests/sprint-2-manual-regression-test.md
```

## User Story Traceability Matrix

| User Story | Feature / Workflow | Verification Evidence |
|---|---|---|
| US-13 | Register account | Automated regression suite; README demo-account guidance |
| US-14 | Login and logout | Automated regression suite; Sprint 2 manual regression |
| US-15 | Role-based access | Automated regression suite; Sprint 2 manual regression |
| US-16 | Reservation ownership | Automated regression suite; Sprint 2 manual regression; My Reservations workflow |
| US-17 | Admin create reservation | Automated regression suite; Sprint 2 manual regression |
| US-18 | Admin modify reservation | Automated regression suite; `docs/manual-tests/us-18-manual-system-test.md`; Sprint 2 manual regression |
| US-19 | Admin cancel reservation | Automated regression suite; Sprint 2 manual regression |
| US-20 | Save users | Automated regression suite; README data and persistence documentation |
| US-21 | Save reservations | Automated regression suite; Sprint 2 manual regression restart and persistence check |
| US-22 | Daily reservation summary | Automated regression suite; Sprint 2 manual regression; US-25 regression check |
| US-23 | All-reservations report | Automated regression suite; `docs/manual-tests/us-23-manual-system-test.md`; Sprint 2 manual regression |
| US-24 | Space usage report | Automated regression suite; `docs/manual-tests/us-24-manual-system-test.md`; Sprint 2 manual regression |
| US-25 | Report date filtering | Automated regression suite; `docs/manual-tests/us-25-manual-system-test.md` |
| US-26 | CSV report export | Automated regression suite; `docs/manual-tests/us-26-manual-system-test.md`; Sprint 2 manual regression |

## Manual Test File Naming

Sprint 2 manual test files use the following naming format:

```text
us-##-manual-system-test.md
```

Regression-level test documents use descriptive Sprint-level names:

```text
sprint-2-automated-regression-test.md
sprint-2-manual-regression-test.md
```

## Runtime Data and Exported Files

Manual testing used the JSON data files in:

```text
src/main/resources/data
```

CSV exports were created only for local testing and verification. Temporary exported CSV files should be reviewed and deleted after testing unless intentionally required as evidence.

## Follow-Up Notes

During manual regression, the visible regular-user and administrator tabs were verified. Registration is documented as part of Sprint 2 and is covered by automated regression evidence, but the Registration tab was not visible in the JavaFX UI during the manual regression pass.

## Demo Readiness

Sprint 2 documentation and traceability are ready to support the Sprint 2 demo and submission. US-13 through US-26 are traceable to automated regression, manual system tests, or Sprint-level regression evidence.

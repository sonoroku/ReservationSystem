# ReservationSystem

## Sprint 1 Demo

[Watch the Sprint 1 demo](https://cdnapisec.kaltura.com/p/2229001/embedPlaykitJs/uiconf_id/45744511?iframeembed=true&entry_id=1_uhw09inm&config%5Bprovider%5D=%7B%22widgetId%22%3A%221_gpuatvr7%22%7D&config%5Bplayback%5D=%7B%22startTime%22%3A0%7D)

ReservationSystem is a JavaFX desktop application for finding and reserving
campus spaces. It supports regular-user reservation workflows and
administrator workflows for managing reservations, viewing reports, filtering
report data, and exporting the displayed results to CSV.

The application is built with Java and JavaFX. Project work is organized and
reviewed through GitHub, and automated tests help confirm that each feature
continues to work as expected.

## Sprint Scope

### Sprint 1: User Reservation Experience (US-1–US-12)

- **US-1 — View all spaces:** Browse the complete list of reservable campus
  spaces.
- **US-2 — View space details:** Select a space to view its building,
  capacity, and available features.
- **US-3 — Filter by minimum capacity:** Show only spaces that can hold at
  least the requested number of people, with clear error messages and an easy
  way to reset the filter.
- **US-4 — Filter by features:** Show spaces that include every selected
  feature, with clear error messages and an easy way to reset the filter.
- **US-5 — View daily availability:** Inspect available and reserved time
  intervals for a selected space and date.
- **US-6 — View date-range availability:** Inspect availability across a valid
  date range, including same-day and multi-day ranges.
- **US-7 — Distinguish availability states:** Display reserved and available
  intervals with clearly different visual states.
- **US-8 — Create a reservation:** Reserve an available space and time while
  checking required information, opening hours, and scheduling conflicts.
- **US-9 — View my reservations:** List the authenticated user's reservations
  in a selectable view.
- **US-10 — Cancel my reservation:** Confirm cancellation, persist the removal,
  refresh related views, and release the reserved interval.
- **US-11 — Modify my reservation:** Edit an owned reservation with the same
  information and scheduling checks used when creating one.
- **US-12 — Suggest available times:** Request alternative available intervals
  when planning a reservation.

Sprint 1 also established the application's core structure, starter data,
screens, file-based storage, automated checks, manual test instructions, and a
full review of how the stories work together.

### Sprint 2: Accounts, Administration, Persistence, and Reporting (US-13–US-26)

- **US-13 — Register:** Create a regular-user account while checking for
  missing information and usernames that are already in use, then save the
  account for future sessions.
- **US-14 — Log in and log out:** Authenticate regular users and
  administrators, maintain the active session, and return safely to the login
  screen on logout.
- **US-15 — Enforce role-based access:** Make administrator screens and actions
  available only to signed-in administrators.
- **US-16 — Enforce reservation ownership:** Allow regular users to modify or
  cancel only their own reservations.
- **US-17 — Administrator reservation creation:** Allow an administrator to
  create a reservation for a selected user.
- **US-18 — Administrator reservation modification:** Allow an administrator
  to update any reservation while keeping the same scheduling checks and
  saving the result.
- **US-19 — Administrator reservation cancellation:** Allow an administrator
  to confirm and save the cancellation of any reservation, then update the
  available times.
- **US-20 — Save users:** Keep registered accounts after the application is
  closed and reopened without changing the original starter data.
- **US-21 — Save reservations:** Keep reservation changes after the application
  is closed and reopened without changing the original starter data.
- **US-22 — View daily reservation summaries:** Show reservation totals grouped
  by date.
- **US-23 — View all-reservations report:** Provide administrators with a
  system-wide reservation report containing reservation, user, space,
  building, date, and time details.
- **US-24 — View space-usage report:** Show per-space reservation counts,
  including spaces with zero reservations.
- **US-25 — Filter reports:** Show report information from a selected start
  date through end date, or clear the dates to restore the full report.
- **US-26 — Export reports:** Export exactly the currently displayed report
  rows to a standard CSV spreadsheet file, with clear messages when there is
  no data or the file cannot be saved, and confirmation before replacing an
  existing file.

Sprint 2 builds on the same application structure with user accounts,
permissions, saved data, administrator-only screens, reusable report filters,
CSV file creation, and additional automated testing.

## Demo Accounts

The starter user data includes these accounts:

| Role | Username | Password |
| --- | --- | --- |
| Regular user | `student` | `student123` |
| Administrator | `admin` | `admin123` |

New registrations create regular-user accounts.

## Running the Application

Prerequisites:

- JDK 26.0.1
- No separate Maven installation is required; the Maven Wrapper is included.

On Windows PowerShell:

```powershell
.\mvnw.cmd javafx:run
```

On Windows Git Bash:

```bash
cd ~/Documents/ReservationSystem
export JAVA_HOME="/c/Program Files/Java/jdk-26.0.1"
chmod +x mvnw
./mvnw javafx:run
```

On macOS or Linux:

```bash
./mvnw javafx:run
```

## Running the Automated Tests

On Windows PowerShell:

```powershell
.\mvnw.cmd test
```

On Windows Git Bash:

```bash
cd ~/Documents/ReservationSystem
export JAVA_HOME="/c/Program Files/Java/jdk-26.0.1"
chmod +x mvnw
./mvnw test
```

On macOS or Linux:

```bash
./mvnw test
```

The test suite checks individual rules as well as complete workflows from both
sprints. Tests that save files use temporary locations so project files and
normal application data remain unchanged.

## Sprint 2 Verification Summary

Sprint 2 automated regression testing was completed from a clean `main`
checkout.

```text
Tested commit/SHA: ec6cea2
Tests run: 221
Failures: 0
Errors: 0
Skipped: 0
Build result: BUILD SUCCESS
```

Sprint 2 manual regression testing was also completed across the main JavaFX
workflows, including admin login and role visibility, regular-user visibility,
availability, reservation creation, my-reservations ownership behavior,
administrator modify and cancel workflows, daily summary, all-reservations
reporting, usage reporting, CSV export behavior, report date filtering,
zero-count paths, and restart persistence.

## Sprint 2 Documentation and Traceability

Sprint 2 verification documents are stored in `docs/manual-tests`.

Important Sprint 2 documents include:

- `us-18-manual-system-test.md`
- `us-23-manual-system-test.md`
- `us-24-manual-system-test.md`
- `us-25-manual-system-test.md`
- `us-26-manual-system-test.md`
- `sprint-2-automated-regression-test.md`
- `sprint-2-manual-regression-test.md`

Sprint 2 story-to-test traceability is documented in:

```text
docs/traceability/sprint-2-traceability.md
```

## Data and Persistence

Starter data is stored under `src/main/resources/data`. When the application is
running, account and reservation changes are saved separately so the original
starter files remain unchanged. Space information is loaded from the supplied
space data.

Important data files include:

- `spaces.json`
- `reservations.json`
- `users.json`

The application uses JSON files for local persistence and does not require a
database.

CSV export includes only the information currently shown in the selected
administrator report. The application asks for confirmation before replacing
an existing file.

## Exported File Handling

CSV files exported during manual testing are temporary test artifacts. They
should be reviewed locally and deleted after verification unless they are
intentionally required as evidence.

Exported CSV files should not be committed by default.

## Project Structure

- `src/main/java/reservationsystem/controller` — connects screens to application
  actions
- `src/main/java/reservationsystem/model` — definitions for spaces, users,
  reservations, and related information
- `src/main/java/reservationsystem/persistence` — loads and saves application
  data
- `src/main/java/reservationsystem/service` — scheduling rules, permissions,
  reports, filters, and CSV creation
- `src/main/java/reservationsystem/view` — application screens and user
  interactions
- `src/main/resources/data` — packaged starter data
- `src/test/java/reservationsystem` — automated tests
- `docs/manual-tests` — story-level manual system tests and cross-story
  review instructions
- `docs/regression` — records from full automated test runs
- `docs/traceability` — traceability documents that map user stories to
  verification evidence

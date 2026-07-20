# US-4 Manual System Test: Feature Filtering

## Purpose

Verify that a user can filter the existing Spaces table by one or more required features, receive clear validation and no-match feedback, clear the filter, and continue using the existing space list.

## Test Environment

- Application: ReservationSystem JavaFX app
- Command: `.\mvnw.cmd javafx:run`
- Test date: July 20, 2026
- Data source: committed `src/main/resources/data/spaces.json`
- Automated regression command: `.\mvnw.cmd test`
- Automated regression result: 101 tests passed; 0 failures, 0 errors, and 0 skipped

## Preconditions

- Issues #80 and #82 are merged.
- The application launches successfully.
- The Spaces tab is selected.
- All feature checkboxes are clear.
- The existing table shows all five starter spaces in alphabetical order.

## Starter-Space Feature Baseline

| Space | Relevant persisted features |
|---|---|
| Nevins Hall Computer Lab | Computers, Projector |
| Odum Library Study Room | Whiteboard |
| Student Union Conference Room 1 | Interactive Whiteboard, PC |
| Student Union Multipurpose Room | Projector |
| University Center North Meeting Room | Projector, Whiteboard |

## Scenario 1: Filter by One Feature

### Steps

1. Select `Projector`.
2. Click the feature-filter `Apply` button.

### Expected Result

- The table shows Nevins Hall Computer Lab, Student Union Multipurpose Room, and University Center North Meeting Room.
- The results remain alphabetical.
- No filter message is displayed.

### Actual Result

- The table displayed Nevins Hall Computer Lab, Student Union Multipurpose Room, and University Center North Meeting Room.
- The three results remained alphabetical.
- No filter message was displayed.

### Status

Pass

## Scenario 2: Multiple Features Use AND Matching

### Steps

1. Click the feature-filter `Clear` button.
2. Select `Projector` and `Computer`.
3. Click the feature-filter `Apply` button.

### Expected Result

- Only Nevins Hall Computer Lab is displayed because it has both selected features.
- No filter message is displayed.

### Actual Result

- Only Nevins Hall Computer Lab was displayed.
- No filter message was displayed.

### Status

Pass

## Scenario 3: Partial Matches Are Excluded

### Steps

1. Click the feature-filter `Clear` button.
2. Select `Projector` and `Whiteboard`.
3. Click the feature-filter `Apply` button.

### Expected Result

- Only University Center North Meeting Room is displayed.
- Student Union Multipurpose Room is excluded because it has only Projector.
- Odum Library Study Room and Student Union Conference Room 1 are excluded because they have only a whiteboard variant.

### Actual Result

- Only University Center North Meeting Room was displayed.
- Spaces containing only Projector or a whiteboard variant were excluded.

### Status

Pass

## Scenario 4: Valid Selection With No Matches

### Steps

1. Click the feature-filter `Clear` button.
2. Select `Projector`, `Whiteboard`, and `Computer`.
3. Click the feature-filter `Apply` button.

### Expected Result

- The existing table becomes empty.
- The message `No spaces match the filter.` is displayed.
- The table shows the no-match placeholder.

### Actual Result

- The existing table became empty.
- The message `No spaces match the filter.` and the no-match placeholder were displayed.

### Status

Pass

## Scenario 5: Applying With No Feature Selected

### Steps

1. Click the feature-filter `Clear` button.
2. Without selecting a feature, click the feature-filter `Apply` button.

### Expected Result

- All five table rows remain unchanged.
- The message `Select at least one feature.` is displayed.

### Actual Result

- All five rows remained in the table.
- The message `Select at least one feature.` was displayed.

### Status

Pass

## Scenario 6: Persisted Feature Variants Match Canonical Controls

### Steps

1. Select `Computer` and click the feature-filter `Apply` button.
2. Record the displayed spaces.
3. Click the feature-filter `Clear` button.
4. Select `Whiteboard` and click the feature-filter `Apply` button.

### Expected Result

- Computer displays Nevins Hall Computer Lab (`Computers`) and Student Union Conference Room 1 (`PC`).
- Whiteboard displays Odum Library Study Room (`Whiteboard`), Student Union Conference Room 1 (`Interactive Whiteboard`), and University Center North Meeting Room (`Whiteboard`).
- Both result sets remain alphabetical.

### Actual Result

- Computer displayed Nevins Hall Computer Lab and Student Union Conference Room 1, confirming that `Computers` and `PC` both matched.
- Whiteboard displayed Odum Library Study Room, Student Union Conference Room 1, and University Center North Meeting Room, confirming that `Whiteboard` and `Interactive Whiteboard` both matched.
- Both result sets remained alphabetical.

### Status

Pass

## Scenario 7: Clear Restores All Spaces

### Steps

1. With the Whiteboard filter still applied, click the feature-filter `Clear` button.

### Expected Result

- Projector, Whiteboard, and Computer are all deselected.
- Feature-filter feedback is cleared.
- All five starter spaces return in alphabetical order.

### Actual Result

- All three feature checkboxes were deselected and feature-filter feedback was cleared.
- All five starter spaces returned in alphabetical order.

### Status

Pass

## Defects and Follow-Up Work

No defects or follow-up work were identified during manual execution.

## Demo Readiness

US-4 is ready to demonstrate for one-feature filtering, multi-feature AND matching, partial-match exclusion, the valid no-match state, no-selection validation, persisted feature aliases, and Clear.

# US-7 Manual System Test: Visual Distinction for Availability

## User Story

As a user, I want reserved and available time slots to be visually distinct so that I can quickly understand a space's availability.

## Test Environment

- Application: ReservationSystem JavaFX app
- Command used to run app: `.\mvnw.cmd javafx:run`
- Tester: zb
- Date tested: July 17, 2026

## Test Case 1: Schedule Shows Both Reserved and Available Slots

### Steps

1. Run the application using `.\mvnw.cmd javafx:run`.
2. Open the Availability tab.
3. Select a space with existing reservations.
4. Select a date with existing reservations.
5. Load the availability schedule.
6. Confirm the schedule displays both reserved and available slots.

### Expected Result

The schedule displays both reserved and available time slots.

### Actual Result

The schedule displayed both reserved and available time slots.

### Status

Pass

## Test Case 2: Reserved and Available Slots Are Visually Distinct

### Steps

1. With the availability schedule loaded, compare reserved slots and available slots.
2. Check whether reserved slots use a different visual style from available slots.
3. Check whether the styling is consistent across multiple slots.

### Expected Result

Reserved and available slots are visually distinct across the schedule.

### Actual Result

Reserved and available slots were visually distinct and consistently styled across the schedule.

### Status

Pass

## Test Case 3: Meaning Does Not Depend on Color Alone

### Steps

1. Review the displayed availability schedule.
2. Confirm that each slot includes a text label such as `Reserved` or `Available`.
3. Confirm that the user can understand each slot's state without relying only on color.

### Expected Result

Each slot includes a readable text label indicating whether it is reserved or available.

### Actual Result

Each slot included a readable `Reserved` or `Available` text label, so the meaning did not depend on color alone.

### Status

Pass

## Test Case 4: Readability Check

### Steps

1. Review several reserved and available slots.
2. Confirm that the text is readable against the background.
3. Confirm that selected rows remain readable.

### Expected Result

Both reserved and available states remain readable.

### Actual Result

Both reserved and available states were readable.

### Status

Pass

## Bugs Found

No bugs found during manual testing.

## Follow-Up Issues

No follow-up issues were created.

## Demo Readiness

US-7 visual distinction is ready to demonstrate in the Sprint 1 correction/demo video.
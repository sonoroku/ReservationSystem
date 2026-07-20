package reservationsystem.service;

import org.junit.jupiter.api.Test;
import reservationsystem.model.Reservation;
import reservationsystem.model.TimeSlot;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TimeSuggestionServiceTest {
	
	private static final LocalDate TEST_DATE =
            LocalDate.of(2026, 7, 20);

    private final TimeSuggestionService service =
            new TimeSuggestionService();

    @Test
    void validRequestReturnsChronologicalSuggestionsWithinOperatingHours() {
        TimeSuggestionResult result = service.suggestAvailableTimes(
                1,
                TEST_DATE,
                60,
                List.of()
        );

        assertTrue(result.isSuccessful());
        assertFalse(result.getSuggestions().isEmpty());

        TimeSlot firstSuggestion = result.getSuggestions().get(0);
        TimeSlot lastSuggestion = result.getSuggestions().get(
                result.getSuggestions().size() - 1
        );

        assertEquals(
                LocalTime.of(8, 0),
                firstSuggestion.getStartTime()
        );
        assertEquals(
                LocalTime.of(9, 0),
                firstSuggestion.getEndTime()
        );
        assertEquals(
                LocalTime.of(19, 0),
                lastSuggestion.getStartTime()
        );
        assertEquals(
                LocalTime.of(20, 0),
                lastSuggestion.getEndTime()
        );

        List<LocalTime> startTimes = result.getSuggestions()
                .stream()
                .map(TimeSlot::getStartTime)
                .toList();

        for (int index = 1; index < startTimes.size(); index++) {
            assertTrue(
                    startTimes.get(index - 1)
                            .isBefore(startTimes.get(index))
            );

            assertEquals(
                    SchedulePolicy.TIME_INCREMENT_MINUTES,
                    java.time.Duration.between(
                            startTimes.get(index - 1),
                            startTimes.get(index)
                    ).toMinutes()
            );
        }
    }

    @Test
    void maximumTwoHourDurationIsAccepted() {
        TimeSuggestionResult result = service.suggestAvailableTimes(
                1,
                TEST_DATE,
                120,
                List.of()
        );

        assertTrue(result.isSuccessful());
        assertFalse(result.getSuggestions().isEmpty());
        assertEquals(
                LocalTime.of(20, 0),
                result.getSuggestions()
                        .get(result.getSuggestions().size() - 1)
                        .getEndTime()
        );
    }

    @Test
    void overlappingAndBufferedCandidatesAreExcluded() {
        Reservation existingReservation = reservation(
                1,
                1,
                TEST_DATE,
                LocalTime.of(10, 0),
                LocalTime.of(11, 0)
        );

        TimeSuggestionResult result = service.suggestAvailableTimes(
                1,
                TEST_DATE,
                30,
                List.of(existingReservation)
        );

        List<LocalTime> startTimes = result.getSuggestions()
                .stream()
                .map(TimeSlot::getStartTime)
                .toList();

        assertTrue(startTimes.contains(LocalTime.of(9, 0)));
        assertFalse(startTimes.contains(LocalTime.of(9, 30)));
        assertFalse(startTimes.contains(LocalTime.of(10, 0)));
        assertFalse(startTimes.contains(LocalTime.of(10, 30)));
        assertFalse(startTimes.contains(LocalTime.of(11, 0)));
        assertTrue(startTimes.contains(LocalTime.of(11, 30)));
    }

    @Test
    void reservationsForDifferentSpaceOrDateDoNotBlockSuggestions() {
        Reservation differentSpaceReservation = reservation(
                1,
                2,
                TEST_DATE,
                LocalTime.of(10, 0),
                LocalTime.of(11, 0)
        );

        Reservation differentDateReservation = reservation(
                2,
                1,
                TEST_DATE.plusDays(1),
                LocalTime.of(10, 0),
                LocalTime.of(11, 0)
        );

        TimeSuggestionResult result = service.suggestAvailableTimes(
                1,
                TEST_DATE,
                30,
                List.of(
                        differentSpaceReservation,
                        differentDateReservation
                )
        );

        List<LocalTime> startTimes = result.getSuggestions()
                .stream()
                .map(TimeSlot::getStartTime)
                .toList();

        assertTrue(startTimes.contains(LocalTime.of(10, 0)));
        assertTrue(startTimes.contains(LocalTime.of(10, 30)));
    }

    @Test
    void fullyBlockedDayReturnsSuccessfulEmptyResult() {
        Reservation allDayReservation = reservation(
                1,
                1,
                TEST_DATE,
                SchedulePolicy.DAY_START_TIME,
                SchedulePolicy.DAY_END_TIME
        );

        TimeSuggestionResult result = service.suggestAvailableTimes(
                1,
                TEST_DATE,
                30,
                List.of(allDayReservation)
        );

        assertTrue(result.isSuccessful());
        assertTrue(result.getSuggestions().isEmpty());
        assertEquals(
                "No available times found",
                result.getMessage()
        );
    }

    @Test
    void nonpositiveSpaceIdIsRejected() {
        TimeSuggestionResult result = service.suggestAvailableTimes(
                0,
                TEST_DATE,
                30,
                List.of()
        );

        assertFalse(result.isSuccessful());
        assertEquals(
                TimeSuggestionResult.Status.INVALID_INPUT,
                result.getStatus()
        );
        assertEquals(
                "Space ID must be positive",
                result.getMessage()
        );
    }

    @Test
    void missingDateIsRejected() {
        TimeSuggestionResult result = service.suggestAvailableTimes(
                1,
                null,
                30,
                List.of()
        );

        assertFalse(result.isSuccessful());
        assertEquals(
                "Date cannot be null",
                result.getMessage()
        );
    }

    @Test
    void nonpositiveDurationIsRejected() {
        TimeSuggestionResult result = service.suggestAvailableTimes(
                1,
                TEST_DATE,
                0,
                List.of()
        );

        assertFalse(result.isSuccessful());
        assertEquals(
                "Duration must be greater than 0 minutes",
                result.getMessage()
        );
    }

    @Test
    void durationLongerThanTwoHoursIsRejected() {
        TimeSuggestionResult result = service.suggestAvailableTimes(
                1,
                TEST_DATE,
                121,
                List.of()
        );

        assertFalse(result.isSuccessful());
        assertEquals(
                "Duration cannot be longer than 120 minutes",
                result.getMessage()
        );
    }

    @Test
    void nullReservationsListIsRejected() {
        TimeSuggestionResult result = service.suggestAvailableTimes(
                1,
                TEST_DATE,
                30,
                null
        );

        assertFalse(result.isSuccessful());
        assertEquals(
                "Reservations cannot be null",
                result.getMessage()
        );
    }

    private Reservation reservation(
            int id,
            int spaceId,
            LocalDate date,
            LocalTime startTime,
            LocalTime endTime
    ) {
        return new Reservation(
                id,
                spaceId,
                "student",
                date,
                startTime,
                endTime
        );
    }

}

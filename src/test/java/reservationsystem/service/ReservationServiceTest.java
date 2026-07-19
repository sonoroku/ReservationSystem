package reservationsystem.service;

import org.junit.jupiter.api.Test;
import reservationsystem.model.Reservation;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ReservationServiceTest {

    @Test
    void validReservationIsAccepted() {
        ReservationService service = new ReservationService();

        Reservation newReservation = new Reservation(
                1,
                1,
                "user001",
                LocalDate.of(2026, 7, 8),
                LocalTime.of(10, 10),
                LocalTime.of(11, 10)
        );

        Reservation existingReservation = new Reservation(
                2,
                1,
                "user002",
                LocalDate.of(2026, 7, 8),
                LocalTime.of(9, 0),
                LocalTime.of(10, 0)
        );

        ReservationValidationResult result = service.validateReservation(
                newReservation,
                List.of(existingReservation)
        );

        assertTrue(result.isValid());
    }

    @Test
    void overlappingReservationIsRejected() {
        ReservationService service = new ReservationService();

        Reservation newReservation = new Reservation(
                1,
                1,
                "user001",
                LocalDate.of(2026, 7, 8),
                LocalTime.of(9, 30),
                LocalTime.of(10, 30)
        );

        Reservation existingReservation = new Reservation(
                2,
                1,
                "user002",
                LocalDate.of(2026, 7, 8),
                LocalTime.of(9, 0),
                LocalTime.of(10, 0)
        );

        ReservationValidationResult result = service.validateReservation(
                newReservation,
                List.of(existingReservation)
        );

        assertFalse(result.isValid());
        assertEquals("Reservation conflicts with an existing reservation", result.getMessage());
    }

    @Test
    void endTimeBeforeStartTimeIsRejected() {
        ReservationService service = new ReservationService();

        Reservation newReservation = new Reservation(
                1,
                1,
                "user001",
                LocalDate.of(2026, 7, 8),
                LocalTime.of(11, 0),
                LocalTime.of(10, 0)
        );

        ReservationValidationResult result = service.validateReservation(
                newReservation,
                List.of()
        );

        assertFalse(result.isValid());
        assertEquals("End time must be after start time", result.getMessage());
    }

    @Test
    void reservationLongerThanTwoHoursIsRejected() {
        ReservationService service = new ReservationService();

        Reservation newReservation = new Reservation(
                1,
                1,
                "user001",
                LocalDate.of(2026, 7, 8),
                LocalTime.of(9, 0),
                LocalTime.of(11, 1)
        );

        ReservationValidationResult result = service.validateReservation(
                newReservation,
                List.of()
        );

        assertFalse(result.isValid());
        assertEquals("Reservation cannot be longer than 2 hours", result.getMessage());
    }

    @Test
    void reservationLessThanTenMinutesAwayIsRejected() {
        ReservationService service = new ReservationService();

        Reservation newReservation = new Reservation(
                1,
                1,
                "user001",
                LocalDate.of(2026, 7, 8),
                LocalTime.of(10, 5),
                LocalTime.of(11, 0)
        );

        Reservation existingReservation = new Reservation(
                2,
                1,
                "user002",
                LocalDate.of(2026, 7, 8),
                LocalTime.of(9, 0),
                LocalTime.of(10, 0)
        );

        ReservationValidationResult result = service.validateReservation(
                newReservation,
                List.of(existingReservation)
        );

        assertFalse(result.isValid());
        assertEquals("Reservation must be at least 10 minutes away from another reservation", result.getMessage());
    }

    @Test
    void reservationExactlyTwoHoursIsAccepted() {
        ReservationService service = new ReservationService();

        Reservation newReservation = new Reservation(
                1,
                1,
                "user001",
                LocalDate.of(2026, 7, 8),
                LocalTime.of(9, 0),
                LocalTime.of(11, 0)
        );

        ReservationValidationResult result = service.validateReservation(
                newReservation,
                List.of()
        );

        assertTrue(result.isValid());
    }

    @Test
    void reservationExactlyTenMinutesAwayIsAccepted() {
        ReservationService service = new ReservationService();

        Reservation newReservation = new Reservation(
                1,
                1,
                "user001",
                LocalDate.of(2026, 7, 8),
                LocalTime.of(10, 10),
                LocalTime.of(11, 0)
        );

        Reservation existingReservation = new Reservation(
                2,
                1,
                "user002",
                LocalDate.of(2026, 7, 8),
                LocalTime.of(9, 0),
                LocalTime.of(10, 0)
        );

        ReservationValidationResult result = service.validateReservation(
                newReservation,
                List.of(existingReservation)
        );

        assertTrue(result.isValid());
    }

    @Test
    void reservationForDifferentSpaceDoesNotConflict() {
        ReservationService service = new ReservationService();

        Reservation newReservation = new Reservation(
                1,
                2,
                "user001",
                LocalDate.of(2026, 7, 8),
                LocalTime.of(9, 30),
                LocalTime.of(10, 30)
        );

        Reservation existingReservation = new Reservation(
                2,
                1,
                "user002",
                LocalDate.of(2026, 7, 8),
                LocalTime.of(9, 0),
                LocalTime.of(10, 0)
        );

        ReservationValidationResult result = service.validateReservation(
                newReservation,
                List.of(existingReservation)
        );

        assertTrue(result.isValid());
    }

    @Test
    void reservationForDifferentDateDoesNotConflict() {
        ReservationService service = new ReservationService();

        Reservation newReservation = new Reservation(
                1,
                1,
                "user001",
                LocalDate.of(2026, 7, 9),
                LocalTime.of(9, 30),
                LocalTime.of(10, 30)
        );

        Reservation existingReservation = new Reservation(
                2,
                1,
                "user002",
                LocalDate.of(2026, 7, 8),
                LocalTime.of(9, 0),
                LocalTime.of(10, 0)
        );

        ReservationValidationResult result = service.validateReservation(
                newReservation,
                List.of(existingReservation)
        );

        assertTrue(result.isValid());
    }

    @Test
    void nullReservationIsRejected() {
        ReservationService service = new ReservationService();

        ReservationValidationResult result = service.validateReservation(
                null,
                List.of()
        );

        assertFalse(result.isValid());
        assertEquals("Reservation cannot be null", result.getMessage());
    }

    @Test
    void nullExistingReservationsListIsRejected() {
        ReservationService service = new ReservationService();

        Reservation newReservation = new Reservation(
                1,
                1,
                "user001",
                LocalDate.of(2026, 7, 8),
                LocalTime.of(9, 0),
                LocalTime.of(10, 0)
        );

        ReservationValidationResult result = service.validateReservation(
                newReservation,
                null
        );

        assertFalse(result.isValid());
        assertEquals("Existing reservations cannot be null", result.getMessage());
    }

    @Test
    void unchangedReservationIsAcceptedWhenUpdatingItself() {
        ReservationService service = new ReservationService();
        Reservation reservation = new Reservation(
                7,
                1,
                "user001",
                LocalDate.of(2026, 7, 8),
                LocalTime.of(9, 0),
                LocalTime.of(10, 0)
        );

        ReservationValidationResult result = service.validateReservationUpdate(
                reservation,
                List.of(reservation)
        );

        assertTrue(result.isValid());
    }

    @Test
    void updateStillRejectsConflictWithAnotherReservation() {
        ReservationService service = new ReservationService();
        Reservation updatedReservation = new Reservation(
                7,
                1,
                "user001",
                LocalDate.of(2026, 7, 8),
                LocalTime.of(10, 30),
                LocalTime.of(11, 30)
        );
        Reservation otherReservation = new Reservation(
                8,
                1,
                "user002",
                LocalDate.of(2026, 7, 8),
                LocalTime.of(10, 0),
                LocalTime.of(11, 0)
        );

        ReservationValidationResult result = service.validateReservationUpdate(
                updatedReservation,
                List.of(updatedReservation, otherReservation)
        );

        assertFalse(result.isValid());
        assertEquals("Reservation conflicts with an existing reservation", result.getMessage());
    }

    @Test
    void updateStillRejectsEndTimeBeforeStartTime() {
        ReservationService service = new ReservationService();
        Reservation updatedReservation = new Reservation(
                7,
                1,
                "user001",
                LocalDate.of(2026, 7, 8),
                LocalTime.of(11, 0),
                LocalTime.of(10, 0)
        );

        ReservationValidationResult result = service.validateReservationUpdate(
                updatedReservation,
                List.of()
        );

        assertFalse(result.isValid());
        assertEquals("End time must be after start time", result.getMessage());
    }

    @Test
    void updateStillRejectsDurationLongerThanTwoHours() {
        ReservationService service = new ReservationService();
        Reservation updatedReservation = new Reservation(
                7,
                1,
                "user001",
                LocalDate.of(2026, 7, 8),
                LocalTime.of(9, 0),
                LocalTime.of(11, 1)
        );

        ReservationValidationResult result = service.validateReservationUpdate(
                updatedReservation,
                List.of()
        );

        assertFalse(result.isValid());
        assertEquals("Reservation cannot be longer than 2 hours", result.getMessage());
    }

    @Test
    void updateStillRejectsBufferViolationWithAnotherReservation() {
        ReservationService service = new ReservationService();
        Reservation updatedReservation = new Reservation(
                7,
                1,
                "user001",
                LocalDate.of(2026, 7, 8),
                LocalTime.of(10, 5),
                LocalTime.of(11, 0)
        );
        Reservation otherReservation = new Reservation(
                8,
                1,
                "user002",
                LocalDate.of(2026, 7, 8),
                LocalTime.of(9, 0),
                LocalTime.of(10, 0)
        );

        ReservationValidationResult result = service.validateReservationUpdate(
                updatedReservation,
                List.of(otherReservation)
        );

        assertFalse(result.isValid());
        assertEquals(
                "Reservation must be at least 10 minutes away from another reservation",
                result.getMessage()
        );
    }
}

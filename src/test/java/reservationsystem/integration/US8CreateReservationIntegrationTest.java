package reservationsystem.integration;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import reservationsystem.controller.ReservationController;
import reservationsystem.model.Reservation;
import reservationsystem.persistence.ReservationJsonRepository;
import reservationsystem.service.DefaultUserProvider;
import reservationsystem.service.ReservationService;
import reservationsystem.service.ReservationValidationResult;

import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class US8CreateReservationIntegrationTest {

    @TempDir
    Path tempDirectory;

    private ReservationJsonRepository createIsolatedRepository() {
        return new ReservationJsonRepository(
                tempDirectory.resolve("reservations.json")
        );
    }

    @Test
    void validReservationIsCreatedForDefaultUserAndSaved() {
        // US-8 Acceptance Test:
        // Given a user enters valid reservation information,
        // when the controller creates the reservation,
        // then the reservation is validated, saved, and returned as successful.

        ReservationJsonRepository repository = createIsolatedRepository();
        ReservationService reservationService = new ReservationService();
        ReservationController controller = new ReservationController(repository, reservationService);

        List<Reservation> originalReservations = repository.loadReservations();

        try {
            ReservationValidationResult result = controller.createReservation(
                    1,
                    LocalDate.of(2026, 7, 8),
                    LocalTime.of(16, 0),
                    LocalTime.of(17, 0)
            );

            assertTrue(result.isValid());

            List<Reservation> updatedReservations = repository.loadReservations();

            boolean reservationWasSaved = updatedReservations.stream()
                    .anyMatch(reservation ->
                            reservation.getSpaceId() == 1
                                    && reservation.getUserId().equals(DefaultUserProvider.DEFAULT_USER_ID)
                                    && reservation.getDate().equals(LocalDate.of(2026, 7, 8))
                                    && reservation.getStartTime().equals(LocalTime.of(16, 0))
                                    && reservation.getEndTime().equals(LocalTime.of(17, 0))
                    );

            assertTrue(reservationWasSaved);
        } finally {
            repository.saveReservations(originalReservations);
        }
    }

    @Test
    void invalidOverlappingReservationIsRejectedAndNotSaved() {
        // US-8 Acceptance Test:
        // Given a user enters reservation information that overlaps an existing reservation,
        // when the controller tries to create the reservation,
        // then the reservation is rejected and not saved.

        ReservationJsonRepository repository = createIsolatedRepository();
        ReservationService reservationService = new ReservationService();
        ReservationController controller = new ReservationController(repository, reservationService);

        List<Reservation> originalReservations = repository.loadReservations();

        try {
            ReservationValidationResult result = controller.createReservation(
                    1,
                    LocalDate.of(2026, 7, 8),
                    LocalTime.of(9, 30),
                    LocalTime.of(10, 30)
            );

            assertFalse(result.isValid());
            assertEquals("Reservation conflicts with an existing reservation", result.getMessage());

            List<Reservation> updatedReservations = repository.loadReservations();

            boolean reservationWasSaved = updatedReservations.stream()
                    .anyMatch(reservation ->
                            reservation.getSpaceId() == 1
                                    && reservation.getUserId().equals(DefaultUserProvider.DEFAULT_USER_ID)
                                    && reservation.getDate().equals(LocalDate.of(2026, 7, 8))
                                    && reservation.getStartTime().equals(LocalTime.of(9, 30))
                                    && reservation.getEndTime().equals(LocalTime.of(10, 30))
                    );

            assertFalse(reservationWasSaved);
            assertEquals(originalReservations.size(), updatedReservations.size());
        } finally {
            repository.saveReservations(originalReservations);
        }
    }

    @Test
    void reservationViolatingTenMinuteBufferIsRejectedAndNotSaved() {
        // US-8 Acceptance Test:
        // Given a user enters reservation information too close to an existing reservation,
        // when the controller tries to create the reservation,
        // then the reservation is rejected and not saved.

        ReservationJsonRepository repository = createIsolatedRepository();
        ReservationService reservationService = new ReservationService();
        ReservationController controller = new ReservationController(repository, reservationService);

        List<Reservation> originalReservations = repository.loadReservations();

        try {
            ReservationValidationResult result = controller.createReservation(
                    1,
                    LocalDate.of(2026, 7, 8),
                    LocalTime.of(10, 5),
                    LocalTime.of(11, 0)
            );

            assertFalse(result.isValid());
            assertEquals(
                    "Reservation must be at least 10 minutes away from another reservation",
                    result.getMessage()
            );

            List<Reservation> updatedReservations = repository.loadReservations();

            boolean reservationWasSaved = updatedReservations.stream()
                    .anyMatch(reservation ->
                            reservation.getSpaceId() == 1
                                    && reservation.getUserId().equals(DefaultUserProvider.DEFAULT_USER_ID)
                                    && reservation.getDate().equals(LocalDate.of(2026, 7, 8))
                                    && reservation.getStartTime().equals(LocalTime.of(10, 5))
                                    && reservation.getEndTime().equals(LocalTime.of(11, 0))
                    );

            assertFalse(reservationWasSaved);
            assertEquals(originalReservations.size(), updatedReservations.size());
        } finally {
            repository.saveReservations(originalReservations);
        }
    }
}

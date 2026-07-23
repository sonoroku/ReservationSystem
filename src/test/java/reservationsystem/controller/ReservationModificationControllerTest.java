package reservationsystem.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import reservationsystem.model.Reservation;
import reservationsystem.persistence.ReservationJsonRepository;
import reservationsystem.service.MyReservationsService;
import reservationsystem.service.ReservationModificationResult;
import reservationsystem.service.ReservationService;

import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ReservationModificationControllerTest {

    private static final String CURRENT_USER_ID = "student";

    @TempDir
    Path tempDirectory;

    @Test
    void modifiesOwnedReservationAndPreservesIdentity() {
        ReservationJsonRepository repository = createRepository("modified-reservation.json");
        repository.saveReservations(List.of(
                reservation(4, 1, CURRENT_USER_ID, 9, 10)
        ));

        ReservationModificationResult result = createController(repository).modifyReservation(
                4,
                2,
                LocalDate.of(2026, 7, 9),
                LocalTime.of(13, 0),
                LocalTime.of(14, 30)
        );

        assertTrue(result.isSuccessful());
        assertEquals(ReservationModificationResult.Status.SUCCESS, result.getStatus());

        Reservation savedReservation = repository.loadReservations().get(0);
        assertEquals(4, savedReservation.getId());
        assertEquals(CURRENT_USER_ID, savedReservation.getUserId());
        assertEquals(2, savedReservation.getSpaceId());
        assertEquals(LocalDate.of(2026, 7, 9), savedReservation.getDate());
        assertEquals(LocalTime.of(13, 0), savedReservation.getStartTime());
        assertEquals(LocalTime.of(14, 30), savedReservation.getEndTime());
    }

    @Test
    void unchangedOwnedReservationIsAccepted() {
        ReservationJsonRepository repository = createRepository("unchanged-reservation.json");
        repository.saveReservations(List.of(
                reservation(4, 1, CURRENT_USER_ID, 9, 10)
        ));

        ReservationModificationResult result = createController(repository).modifyReservation(
                4,
                1,
                LocalDate.of(2026, 7, 8),
                LocalTime.of(9, 0),
                LocalTime.of(10, 0)
        );

        assertTrue(result.isSuccessful());
    }

    @Test
    void modifiesOnlyTimesWhenOtherValuesAreUnchanged() {
        ReservationJsonRepository repository = createRepository("partial-modification.json");
        repository.saveReservations(List.of(
                reservation(4, 1, CURRENT_USER_ID, 9, 10)
        ));

        ReservationModificationResult result = createController(repository).modifyReservation(
                4,
                1,
                LocalDate.of(2026, 7, 8),
                LocalTime.of(13, 0),
                LocalTime.of(14, 0)
        );

        assertTrue(result.isSuccessful());

        Reservation savedReservation = repository.loadReservations().get(0);
        assertEquals(1, savedReservation.getSpaceId());
        assertEquals(LocalDate.of(2026, 7, 8), savedReservation.getDate());
        assertEquals(LocalTime.of(13, 0), savedReservation.getStartTime());
        assertEquals(LocalTime.of(14, 0), savedReservation.getEndTime());
    }

    @Test
    void missingReservationReturnsNotFoundWithoutChangingPersistence() {
        ReservationJsonRepository repository = createRepository("missing-reservation.json");
        repository.saveReservations(List.of(
                reservation(4, 1, CURRENT_USER_ID, 9, 10)
        ));

        ReservationModificationResult result = createController(repository).modifyReservation(
                99,
                2,
                LocalDate.of(2026, 7, 9),
                LocalTime.of(13, 0),
                LocalTime.of(14, 0)
        );

        assertFalse(result.isSuccessful());
        assertEquals(ReservationModificationResult.Status.NOT_FOUND, result.getStatus());
        assertEquals(1, repository.loadReservations().get(0).getSpaceId());
    }

    @Test
    void reservationOwnedByAnotherUserReturnsNotOwnedWithoutChangingPersistence() {
        ReservationJsonRepository repository = createRepository("not-owned-reservation.json");
        repository.saveReservations(List.of(
                reservation(4, 1, "admin", 9, 10)
        ));

        ReservationModificationResult result = createController(repository).modifyReservation(
                4,
                2,
                LocalDate.of(2026, 7, 9),
                LocalTime.of(13, 0),
                LocalTime.of(14, 0)
        );

        assertFalse(result.isSuccessful());
        assertEquals(ReservationModificationResult.Status.NOT_OWNED, result.getStatus());
        assertEquals(1, repository.loadReservations().get(0).getSpaceId());
    }

    @Test
    void invalidUpdateIsRejectedAtomically() {
        ReservationJsonRepository repository = createRepository("invalid-reservation.json");
        Reservation originalReservation = reservation(
                4,
                1,
                CURRENT_USER_ID,
                9,
                10
        );
        repository.saveReservations(List.of(originalReservation));

        ReservationModificationResult result = createController(repository).modifyReservation(
                4,
                2,
                LocalDate.of(2026, 7, 9),
                LocalTime.of(15, 0),
                LocalTime.of(14, 0)
        );

        assertFalse(result.isSuccessful());
        assertEquals(ReservationModificationResult.Status.VALIDATION_FAILED, result.getStatus());
        assertEquals("End time must be after start time", result.getMessage());

        Reservation savedReservation = repository.loadReservations().get(0);
        assertEquals(originalReservation.getSpaceId(), savedReservation.getSpaceId());
        assertEquals(originalReservation.getDate(), savedReservation.getDate());
        assertEquals(originalReservation.getStartTime(), savedReservation.getStartTime());
        assertEquals(originalReservation.getEndTime(), savedReservation.getEndTime());
    }

    @Test
    void conflictingUpdateIsRejectedAtomically() {
        ReservationJsonRepository repository = createRepository("conflicting-reservation.json");
        repository.saveReservations(List.of(
                reservation(4, 1, CURRENT_USER_ID, 9, 10),
                reservation(5, 1, "admin", 11, 12)
        ));

        ReservationModificationResult result = createController(repository).modifyReservation(
                4,
                1,
                LocalDate.of(2026, 7, 8),
                LocalTime.of(10, 30),
                LocalTime.of(11, 30)
        );

        assertFalse(result.isSuccessful());
        assertEquals(ReservationModificationResult.Status.VALIDATION_FAILED, result.getStatus());
        assertEquals(
                List.of(LocalTime.of(9, 0), LocalTime.of(11, 0)),
                repository.loadReservations().stream().map(Reservation::getStartTime).toList()
        );
    }

    private ReservationController createController(ReservationJsonRepository repository) {
        return new ReservationController(
                repository,
                new ReservationService(),
                () -> CURRENT_USER_ID,
                new MyReservationsService(),
                new SpaceController()
        );
    }

    private ReservationJsonRepository createRepository(String fileName) {
        return new ReservationJsonRepository(tempDirectory.resolve(fileName));
    }

    private Reservation reservation(
            int id,
            int spaceId,
            String userId,
            int startHour,
            int endHour
    ) {
        return new Reservation(
                id,
                spaceId,
                userId,
                LocalDate.of(2026, 7, 8),
                LocalTime.of(startHour, 0),
                LocalTime.of(endHour, 0)
        );
    }
}

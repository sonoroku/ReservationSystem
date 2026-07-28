package reservationsystem.integration;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import reservationsystem.controller.ReservationController;
import reservationsystem.model.Reservation;
import reservationsystem.persistence.ReservationJsonRepository;
import reservationsystem.service.ReservationCancellationResult;
import reservationsystem.service.ReservationModificationResult;
import reservationsystem.service.ReservationService;
import reservationsystem.service.ReservationValidationResult;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class US21ReservationPersistenceIntegrationTest {

    private static final String STARTER_RESERVATIONS_RESOURCE =
            "/data/reservations.json";

    private static final String CURRENT_USER_ID = "student";

    @TempDir
    Path temporaryDirectory;

    @Test
    void missingRuntimeFileInitializesFromStarterReservationsWithoutChangingResource()
            throws IOException {
        Path runtimeFile = temporaryDirectory.resolve("reservations.json");
        byte[] starterContentsBefore = readStarterReservationContents();

        ReservationJsonRepository repository =
                new ReservationJsonRepository(runtimeFile);
        List<Reservation> initializedReservations =
                repository.loadReservations();

        assertTrue(Files.exists(runtimeFile));
        assertEquals(3, initializedReservations.size());

        ReservationJsonRepository restartedRepository =
                new ReservationJsonRepository(runtimeFile);
        List<Reservation> reloadedReservations =
                restartedRepository.loadReservations();

        assertReservationsEqual(
                initializedReservations,
                reloadedReservations
        );
        assertArrayEquals(
                starterContentsBefore,
                readStarterReservationContents()
        );
    }

    @Test
    void createModifyAndCancelSurviveApplicationStyleReloads() {
        Path runtimeFile = temporaryDirectory.resolve(
                "reservation-lifecycle.json"
        );
        ReservationJsonRepository repository =
                new ReservationJsonRepository(runtimeFile);
        repository.saveReservations(List.of());

        ReservationController createController = controller(repository);
        ReservationValidationResult createResult =
                createController.createReservation(
                        1,
                        LocalDate.of(2026, 8, 10),
                        LocalTime.of(9, 15),
                        LocalTime.of(10, 45)
                );

        assertTrue(createResult.isValid());

        ReservationJsonRepository afterCreateRepository =
                new ReservationJsonRepository(runtimeFile);
        Reservation createdReservation =
                afterCreateRepository.loadReservations().get(0);
        assertReservation(
                createdReservation,
                1,
                1,
                CURRENT_USER_ID,
                LocalDate.of(2026, 8, 10),
                LocalTime.of(9, 15),
                LocalTime.of(10, 45)
        );

        ReservationController modifyController =
                controller(afterCreateRepository);
        ReservationModificationResult modifyResult =
                modifyController.modifyReservation(
                        createdReservation.getId(),
                        2,
                        LocalDate.of(2026, 8, 11),
                        LocalTime.of(13, 0),
                        LocalTime.of(14, 30)
                );

        assertTrue(modifyResult.isSuccessful());

        ReservationJsonRepository afterModifyRepository =
                new ReservationJsonRepository(runtimeFile);
        Reservation modifiedReservation =
                afterModifyRepository.loadReservations().get(0);
        assertReservation(
                modifiedReservation,
                1,
                2,
                CURRENT_USER_ID,
                LocalDate.of(2026, 8, 11),
                LocalTime.of(13, 0),
                LocalTime.of(14, 30)
        );

        ReservationController cancelController =
                controller(afterModifyRepository);
        ReservationCancellationResult cancelResult =
                cancelController.cancelReservation(
                        modifiedReservation.getId()
                );

        assertTrue(cancelResult.isSuccessful());

        ReservationJsonRepository afterCancelRepository =
                new ReservationJsonRepository(runtimeFile);
        assertTrue(afterCancelRepository.loadReservations().isEmpty());
    }

    @Test
    void intentionallyEmptyRuntimeListRemainsEmptyAfterRestart() {
        Path runtimeFile = temporaryDirectory.resolve(
                "empty-reservations.json"
        );
        ReservationJsonRepository repository =
                new ReservationJsonRepository(runtimeFile);

        repository.saveReservations(List.of());

        assertTrue(Files.exists(runtimeFile));

        ReservationJsonRepository restartedRepository =
                new ReservationJsonRepository(runtimeFile);
        assertTrue(restartedRepository.loadReservations().isEmpty());
    }

    private ReservationController controller(
            ReservationJsonRepository repository
    ) {
        return new ReservationController(
                repository,
                new ReservationService(),
                () -> CURRENT_USER_ID
        );
    }

    private byte[] readStarterReservationContents() throws IOException {
        try (InputStream inputStream =
                     ReservationJsonRepository.class.getResourceAsStream(
                             STARTER_RESERVATIONS_RESOURCE
                     )) {
            if (inputStream == null) {
                throw new IOException(
                        "Starter reservations resource was not found"
                );
            }

            return inputStream.readAllBytes();
        }
    }

    private void assertReservationsEqual(
            List<Reservation> expectedReservations,
            List<Reservation> actualReservations
    ) {
        assertEquals(
                expectedReservations.size(),
                actualReservations.size()
        );

        for (int index = 0;
             index < expectedReservations.size();
             index++) {
            Reservation expectedReservation =
                    expectedReservations.get(index);

            assertReservation(
                    actualReservations.get(index),
                    expectedReservation.getId(),
                    expectedReservation.getSpaceId(),
                    expectedReservation.getUserId(),
                    expectedReservation.getDate(),
                    expectedReservation.getStartTime(),
                    expectedReservation.getEndTime()
            );
        }
    }

    private void assertReservation(
            Reservation reservation,
            int expectedId,
            int expectedSpaceId,
            String expectedUserId,
            LocalDate expectedDate,
            LocalTime expectedStartTime,
            LocalTime expectedEndTime
    ) {
        assertEquals(expectedId, reservation.getId());
        assertEquals(expectedSpaceId, reservation.getSpaceId());
        assertEquals(expectedUserId, reservation.getUserId());
        assertEquals(expectedDate, reservation.getDate());
        assertEquals(expectedStartTime, reservation.getStartTime());
        assertEquals(expectedEndTime, reservation.getEndTime());
    }
}

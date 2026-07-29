package reservationsystem.integration;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import reservationsystem.controller.SpaceUsageReportController;
import reservationsystem.model.Reservation;
import reservationsystem.model.SpaceUsageReportRow;
import reservationsystem.model.User;
import reservationsystem.persistence.ReservationJsonRepository;
import reservationsystem.persistence.SpaceJsonRepository;
import reservationsystem.service.AuthorizationService;
import reservationsystem.service.SpaceUsageReportResult;
import reservationsystem.service.SpaceUsageReportService;

import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class US24SpaceUsageReportIntegrationTest {

    @TempDir
    Path temporaryDirectory;

    @Test
    void administratorReceivesEveryKnownSpaceWithPersistedCounts() {
        ReservationJsonRepository reservationRepository =
                new ReservationJsonRepository(
                        temporaryDirectory.resolve("reservations.json")
                );
        reservationRepository.saveReservations(List.of(
                reservation(1, 1),
                reservation(2, 1),
                reservation(3, 4),
                reservation(4, 99)
        ));

        SpaceUsageReportController controller =
                new SpaceUsageReportController(
                        new SpaceJsonRepository(),
                        reservationRepository,
                        new SpaceUsageReportService(),
                        new AuthorizationService(
                                () -> new User(
                                        "admin",
                                        "admin123",
                                        true
                                )
                        )
                );

        SpaceUsageReportResult result =
                controller.getSpaceUsageReport();

        assertTrue(result.isSuccessful());
        assertEquals(5, result.getRows().size());
        assertEquals(
                List.of(
                        "Nevins Hall Computer Lab",
                        "Odum Library Study Room",
                        "Student Union Conference Room 1",
                        "Student Union Multipurpose Room",
                        "University Center North Meeting Room"
                ),
                result.getRows().stream()
                        .map(row -> row.getSpace().getName())
                        .toList()
        );
        assertEquals(2, countForSpace(result, 1));
        assertEquals(1, countForSpace(result, 4));
        assertEquals(0, countForSpace(result, 2));
        assertEquals(0, countForSpace(result, 3));
        assertEquals(0, countForSpace(result, 5));
    }

    @Test
    void emptyPersistedReservationsGiveEverySpaceAZeroCount() {
        ReservationJsonRepository reservationRepository =
                new ReservationJsonRepository(
                        temporaryDirectory.resolve(
                                "empty-reservations.json"
                        )
                );
        reservationRepository.saveReservations(List.of());

        SpaceUsageReportResult result = controller(
                new User("admin", "admin123", true),
                reservationRepository
        ).getSpaceUsageReport();

        assertTrue(result.isSuccessful());
        assertEquals(5, result.getRows().size());
        assertTrue(result.getRows().stream().allMatch(
                row -> row.getReservationCount() == 0
        ));
    }

    @Test
    void regularUserCannotObtainPersistedUsageReport() {
        ReservationJsonRepository reservationRepository =
                new ReservationJsonRepository(
                        temporaryDirectory.resolve(
                                "denied-reservations.json"
                        )
                );
        reservationRepository.saveReservations(List.of(
                reservation(1, 1)
        ));

        SpaceUsageReportResult result = controller(
                new User("student", "student123", false),
                reservationRepository
        ).getSpaceUsageReport();

        assertEquals(
                SpaceUsageReportResult.Status.UNAUTHORIZED,
                result.getStatus()
        );
        assertEquals("Administrator access is required", result.getMessage());
        assertTrue(result.getRows().isEmpty());
    }

    private SpaceUsageReportController controller(
            User currentUser,
            ReservationJsonRepository reservationRepository
    ) {
        return new SpaceUsageReportController(
                new SpaceJsonRepository(),
                reservationRepository,
                new SpaceUsageReportService(),
                new AuthorizationService(() -> currentUser)
        );
    }

    private int countForSpace(
            SpaceUsageReportResult result,
            int spaceId
    ) {
        return result.getRows().stream()
                .filter(row -> row.getSpace().getId() == spaceId)
                .map(SpaceUsageReportRow::getReservationCount)
                .findFirst()
                .orElseThrow();
    }

    private Reservation reservation(int id, int spaceId) {
        return new Reservation(
                id,
                spaceId,
                "student",
                LocalDate.of(2026, 8, 15),
                LocalTime.of(9, 0),
                LocalTime.of(10, 0)
        );
    }
}

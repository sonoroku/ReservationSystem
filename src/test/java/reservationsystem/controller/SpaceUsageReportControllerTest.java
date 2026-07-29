package reservationsystem.controller;

import org.junit.jupiter.api.Test;
import reservationsystem.model.Reservation;
import reservationsystem.model.Space;
import reservationsystem.model.User;
import reservationsystem.persistence.ReservationJsonRepository;
import reservationsystem.persistence.SpaceJsonRepository;
import reservationsystem.service.AuthorizationService;
import reservationsystem.service.SpaceUsageReportResult;
import reservationsystem.service.SpaceUsageReportService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SpaceUsageReportControllerTest {

    @Test
    void administratorLoadsEachRepositoryOnce() {
        CountingSpaceRepository spaceRepository =
                new CountingSpaceRepository(List.of(
                        new Space(
                                1,
                                "Study Room",
                                "Library",
                                8,
                                List.of()
                        )
                ));
        CountingReservationRepository reservationRepository =
                new CountingReservationRepository(List.of());

        SpaceUsageReportResult result = controller(
                new User("admin", "admin123", true),
                spaceRepository,
                reservationRepository
        ).getSpaceUsageReport();

        assertTrue(result.isSuccessful());
        assertEquals(1, spaceRepository.getLoadCount());
        assertEquals(1, reservationRepository.getLoadCount());
    }

    @Test
    void regularUserDenialDoesNotLoadRepositories() {
        CountingSpaceRepository spaceRepository =
                new CountingSpaceRepository(List.of());
        CountingReservationRepository reservationRepository =
                new CountingReservationRepository(List.of());

        SpaceUsageReportResult result = controller(
                new User("student", "student123", false),
                spaceRepository,
                reservationRepository
        ).getSpaceUsageReport();

        assertEquals(
                SpaceUsageReportResult.Status.UNAUTHORIZED,
                result.getStatus()
        );
        assertEquals("Administrator access is required", result.getMessage());
        assertEquals(0, spaceRepository.getLoadCount());
        assertEquals(0, reservationRepository.getLoadCount());
    }

    @Test
    void loggedOutUserReceivesControlledDenial() {
        CountingSpaceRepository spaceRepository =
                new CountingSpaceRepository(List.of());
        CountingReservationRepository reservationRepository =
                new CountingReservationRepository(List.of());

        SpaceUsageReportResult result = controller(
                null,
                spaceRepository,
                reservationRepository
        ).getSpaceUsageReport();

        assertEquals(
                SpaceUsageReportResult.Status.UNAUTHORIZED,
                result.getStatus()
        );
        assertEquals(
                "You must be logged in to access this feature",
                result.getMessage()
        );
        assertTrue(result.getRows().isEmpty());
    }

    private SpaceUsageReportController controller(
            User currentUser,
            SpaceJsonRepository spaceRepository,
            ReservationJsonRepository reservationRepository
    ) {
        return new SpaceUsageReportController(
                spaceRepository,
                reservationRepository,
                new SpaceUsageReportService(),
                new AuthorizationService(() -> currentUser)
        );
    }

    private static class CountingSpaceRepository
            extends SpaceJsonRepository {

        private final List<Space> spaces;
        private int loadCount;

        CountingSpaceRepository(List<Space> spaces) {
            this.spaces = spaces;
        }

        @Override
        public List<Space> loadSpaces() {
            loadCount++;
            return spaces;
        }

        int getLoadCount() {
            return loadCount;
        }
    }

    private static class CountingReservationRepository
            extends ReservationJsonRepository {

        private final List<Reservation> reservations;
        private int loadCount;

        CountingReservationRepository(List<Reservation> reservations) {
            this.reservations = reservations;
        }

        @Override
        public List<Reservation> loadReservations() {
            loadCount++;
            return reservations;
        }

        int getLoadCount() {
            return loadCount;
        }
    }
}

package reservationsystem.integration;

import org.junit.jupiter.api.Test;
import reservationsystem.controller.ReservationReportController;
import reservationsystem.model.User;
import reservationsystem.service.ReservationReportEntry;
import reservationsystem.service.ReservationReportResult;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class US23ReservationReportingIntegrationTest {

    @Test
    void adminCanLoadAllReservationsFromJsonForReporting() {
        // US-23 Acceptance Test:
        // Given an authenticated administrator,
        // when the all-reservations report is requested,
        // then all reservations are loaded with owner and space information.

        User admin = new User("admin001", "password123", true);
        ReservationReportController controller = new ReservationReportController(() -> admin);

        ReservationReportResult result = controller.getAllReservationsReport();

        assertTrue(result.isSuccessful());
        assertFalse(result.getEntries().isEmpty());

        ReservationReportEntry firstEntry = result.getEntries().get(0);

        assertNotNull(firstEntry.getUserId());
        assertNotNull(firstEntry.getSpaceName());
        assertNotNull(firstEntry.getBuilding());
        assertNotNull(firstEntry.getDate());
        assertNotNull(firstEntry.getStartTime());
        assertNotNull(firstEntry.getEndTime());
    }

    @Test
    void reportLoadedFromJsonIsChronological() {
        // US-23 Acceptance Test:
        // Given reservation report data is loaded,
        // when the report entries are returned,
        // then they are sorted chronologically.

        User admin = new User("admin001", "password123", true);
        ReservationReportController controller = new ReservationReportController(() -> admin);

        ReservationReportResult result = controller.getAllReservationsReport();

        assertTrue(result.isSuccessful());

        List<ReservationReportEntry> entries = result.getEntries();

        for (int i = 0; i < entries.size() - 1; i++) {
            ReservationReportEntry current = entries.get(i);
            ReservationReportEntry next = entries.get(i + 1);

            int dateComparison = current.getDate().compareTo(next.getDate());

            if (dateComparison == 0) {
                assertTrue(
                        current.getStartTime().compareTo(next.getStartTime()) <= 0,
                        "Report entries should be sorted by date and start time"
                );
            } else {
                assertTrue(
                        dateComparison <= 0,
                        "Report entries should be sorted by date"
                );
            }
        }
    }

    @Test
    void regularUserCannotLoadAllReservationsReport() {
        // US-23 Acceptance Test:
        // Given a regular user is authenticated,
        // when the all-reservations report is requested,
        // then the report is rejected.

        User regularUser = new User("user001", "password123", false);
        ReservationReportController controller = new ReservationReportController(() -> regularUser);

        ReservationReportResult result = controller.getAllReservationsReport();

        assertFalse(result.isSuccessful());
        assertEquals("Administrator access is required", result.getMessage());
        assertTrue(result.getEntries().isEmpty());
    }
}

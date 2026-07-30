package reservationsystem.integration;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import reservationsystem.model.Reservation;
import reservationsystem.model.Space;
import reservationsystem.model.SpaceUsageReportRow;
import reservationsystem.model.User;
import reservationsystem.service.CsvExportResult;
import reservationsystem.service.ReportCsvExporter;
import reservationsystem.service.ReservationReportResult;
import reservationsystem.service.ReservationReportService;
import reservationsystem.service.SpaceUsageReportResult;
import reservationsystem.service.SpaceUsageReportService;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class US26CsvReportExportIntegrationTest {

    @TempDir
    Path temporaryDirectory;

    private final User admin = new User("admin", "admin123", true);
    private final List<Space> spaces = List.of(
            new Space(1, "Auditorium", "Main", 100, List.of()),
            new Space(2, "Study Room", "Library", 8, List.of())
    );
    private final List<Reservation> reservations = List.of(
            reservation(1, 1, 10),
            reservation(2, 2, 12)
    );
    private final ReportCsvExporter exporter = new ReportCsvExporter();

    @Test
    void filteredReservationDatasetIsExportedWithoutReloadingData()
            throws IOException {
        ReservationReportResult report = new ReservationReportService()
                .generateAllReservationsReport(
                        admin,
                        reservations,
                        spaces,
                        date(12),
                        date(12)
                );
        Path destination = temporaryDirectory.resolve("reservations.csv");

        CsvExportResult export = exporter.exportReservations(
                report.getEntries(),
                destination
        );

        String csv = Files.readString(destination, StandardCharsets.UTF_8);
        assertTrue(export.isSuccessful());
        assertTrue(csv.contains("2,2,Study Room"));
        assertFalse(csv.contains("1,1,Auditorium"));
    }

    @Test
    void filteredUsageCountsAreExportedExactlyAsDisplayed()
            throws IOException {
        SpaceUsageReportResult report = new SpaceUsageReportService()
                .createReport(
                        spaces,
                        reservations,
                        date(12),
                        date(12)
                );
        Path destination = temporaryDirectory.resolve("usage.csv");

        CsvExportResult export = exporter.exportSpaceUsage(
                report.getRows(),
                destination
        );

        String csv = Files.readString(destination, StandardCharsets.UTF_8);
        assertTrue(export.isSuccessful());
        assertTrue(csv.contains("1,Auditorium,Main,100,0"));
        assertTrue(csv.contains("2,Study Room,Library,8,1"));
        assertEquals(1, report.getRows().stream()
                .mapToInt(SpaceUsageReportRow::getReservationCount)
                .sum());
    }

    private LocalDate date(int day) {
        return LocalDate.of(2026, 8, day);
    }

    private Reservation reservation(int id, int spaceId, int day) {
        return new Reservation(
                id,
                spaceId,
                "student",
                date(day),
                LocalTime.of(9, 0),
                LocalTime.of(10, 0)
        );
    }
}

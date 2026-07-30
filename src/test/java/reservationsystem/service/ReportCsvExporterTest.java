package reservationsystem.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import reservationsystem.model.Space;
import reservationsystem.model.SpaceUsageReportRow;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ReportCsvExporterTest {

    @TempDir
    Path temporaryDirectory;

    private final ReportCsvExporter exporter = new ReportCsvExporter();

    @Test
    void reservationExportUsesFixedColumnsUtf8AndRfcQuoting()
            throws IOException {
        Path destination = temporaryDirectory.resolve("reservations.csv");
        ReservationReportEntry entry = new ReservationReportEntry(
                7,
                4,
                "Café, \"North\"\nRoom",
                "Science\r\nCenter",
                "student",
                LocalDate.of(2026, 8, 11),
                LocalTime.of(9, 0),
                LocalTime.of(10, 30)
        );

        CsvExportResult result = exporter.exportReservations(
                List.of(entry),
                destination
        );

        assertEquals(CsvExportResult.Status.SUCCESS, result.getStatus());
        String expected = "Reservation ID,Space ID,Space Name,Building,"
                + "User ID,Date,Start Time,End Time\r\n"
                + "7,4,\"Café, \"\"North\"\"\nRoom\","
                + "\"Science\r\nCenter\",student,2026-08-11,09:00,10:30\r\n";
        assertArrayEquals(
                expected.getBytes(StandardCharsets.UTF_8),
                Files.readAllBytes(destination)
        );
    }

    @Test
    void usageExportWritesOnlySuppliedRowsInReportOrder()
            throws IOException {
        Path destination = temporaryDirectory.resolve("usage.csv");
        Space included = new Space(
                2,
                "Study Room",
                "Library",
                8,
                List.of("Whiteboard")
        );

        CsvExportResult result = exporter.exportSpaceUsage(
                List.of(new SpaceUsageReportRow(included, 3)),
                destination
        );

        assertTrue(result.isSuccessful());
        assertEquals(
                "Space ID,Space Name,Building,Capacity,Reservation Count\r\n"
                        + "2,Study Room,Library,8,3\r\n",
                Files.readString(destination, StandardCharsets.UTF_8)
        );
    }

    @Test
    void emptyDatasetIsRejectedWithoutCreatingAFile() {
        Path destination = temporaryDirectory.resolve("empty.csv");

        CsvExportResult result = exporter.exportReservations(
                List.of(),
                destination
        );

        assertEquals(CsvExportResult.Status.EMPTY_DATA, result.getStatus());
        assertFalse(Files.exists(destination));
    }

    @Test
    void existingFileRequiresExplicitOverwriteConfirmation()
            throws IOException {
        Path destination = temporaryDirectory.resolve("existing.csv");
        Files.writeString(destination, "keep", StandardCharsets.UTF_8);
        Space space = new Space(1, "Room", "Main", 5, List.of());
        List<SpaceUsageReportRow> rows = List.of(
                new SpaceUsageReportRow(space, 1)
        );

        CsvExportResult refused = exporter.exportSpaceUsage(
                rows,
                destination
        );

        assertEquals(CsvExportResult.Status.FILE_EXISTS, refused.getStatus());
        assertEquals("keep", Files.readString(destination));

        CsvExportResult overwritten = exporter.exportSpaceUsage(
                rows,
                destination,
                true
        );

        assertTrue(overwritten.isSuccessful());
        assertTrue(Files.readString(destination).startsWith("Space ID,"));
    }

    @Test
    void writeFailureReturnsControlledResult() {
        Space space = new Space(1, "Room", "Main", 5, List.of());

        CsvExportResult result = exporter.exportSpaceUsage(
                List.of(new SpaceUsageReportRow(space, 1)),
                temporaryDirectory.resolve("missing").resolve("report.csv")
        );

        assertEquals(CsvExportResult.Status.WRITE_FAILED, result.getStatus());
        assertTrue(result.getMessage().startsWith(
                "Unable to export the report"
        ));
    }
}

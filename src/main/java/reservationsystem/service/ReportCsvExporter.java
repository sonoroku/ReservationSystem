package reservationsystem.service;

import reservationsystem.model.Space;
import reservationsystem.model.SpaceUsageReportRow;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;

public class ReportCsvExporter {

    private static final List<String> RESERVATION_HEADERS = List.of(
            "Reservation ID",
            "Space ID",
            "Space Name",
            "Building",
            "User ID",
            "Date",
            "Start Time",
            "End Time"
    );
    private static final List<String> SPACE_USAGE_HEADERS = List.of(
            "Space ID",
            "Space Name",
            "Building",
            "Capacity",
            "Reservation Count"
    );

    public CsvExportResult exportReservations(
            List<ReservationReportEntry> entries,
            Path destination
    ) {
        return exportReservations(entries, destination, false);
    }

    public CsvExportResult exportReservations(
            List<ReservationReportEntry> entries,
            Path destination,
            boolean overwriteConfirmed
    ) {
        requireArguments(entries, destination);
        if (entries.isEmpty()) {
            return CsvExportResult.emptyData(destination);
        }

        List<List<String>> rows = entries.stream()
                .map(entry -> List.of(
                        Integer.toString(entry.getReservationId()),
                        Integer.toString(entry.getSpaceId()),
                        safeText(entry.getSpaceName()),
                        safeText(entry.getBuilding()),
                        safeText(entry.getUserId()),
                        safeText(entry.getDate()),
                        safeText(entry.getStartTime()),
                        safeText(entry.getEndTime())
                ))
                .toList();

        return writeCsv(
                RESERVATION_HEADERS,
                rows,
                destination,
                overwriteConfirmed
        );
    }

    public CsvExportResult exportSpaceUsage(
            List<SpaceUsageReportRow> rows,
            Path destination
    ) {
        return exportSpaceUsage(rows, destination, false);
    }

    public CsvExportResult exportSpaceUsage(
            List<SpaceUsageReportRow> rows,
            Path destination,
            boolean overwriteConfirmed
    ) {
        requireArguments(rows, destination);
        if (rows.isEmpty()) {
            return CsvExportResult.emptyData(destination);
        }

        List<List<String>> csvRows = rows.stream()
                .map(row -> {
                    Space space = row.getSpace();
                    return List.of(
                            Integer.toString(space.getId()),
                            safeText(space.getName()),
                            safeText(space.getBuilding()),
                            Integer.toString(space.getCapacity()),
                            Integer.toString(row.getReservationCount())
                    );
                })
                .toList();

        return writeCsv(
                SPACE_USAGE_HEADERS,
                csvRows,
                destination,
                overwriteConfirmed
        );
    }

    private CsvExportResult writeCsv(
            List<String> headers,
            List<List<String>> rows,
            Path destination,
            boolean overwriteConfirmed
    ) {
        if (Files.exists(destination) && !overwriteConfirmed) {
            return CsvExportResult.fileExists(destination);
        }

        OpenOption[] options = overwriteConfirmed
                ? new OpenOption[]{
                        StandardOpenOption.CREATE,
                        StandardOpenOption.TRUNCATE_EXISTING,
                        StandardOpenOption.WRITE
                }
                : new OpenOption[]{
                        StandardOpenOption.CREATE_NEW,
                        StandardOpenOption.WRITE
                };

        try (BufferedWriter writer = Files.newBufferedWriter(
                destination,
                StandardCharsets.UTF_8,
                options
        )) {
            writeRow(writer, headers);
            for (List<String> row : rows) {
                writeRow(writer, row);
            }
            return CsvExportResult.success(destination);
        } catch (IOException exception) {
            return CsvExportResult.writeFailed(
                    destination,
                    exception.getMessage()
            );
        }
    }

    private void writeRow(
            BufferedWriter writer,
            List<String> fields
    ) throws IOException {
        for (int index = 0; index < fields.size(); index++) {
            if (index > 0) {
                writer.write(',');
            }
            writer.write(escape(fields.get(index)));
        }
        writer.write("\r\n");
    }

    private String escape(String field) {
        if (field.indexOf(',') < 0
                && field.indexOf('"') < 0
                && field.indexOf('\r') < 0
                && field.indexOf('\n') < 0) {
            return field;
        }
        return '"' + field.replace("\"", "\"\"") + '"';
    }

    private String safeText(Object value) {
        return value == null ? "" : value.toString();
    }

    private void requireArguments(List<?> rows, Path destination) {
        if (rows == null) {
            throw new IllegalArgumentException("Report data cannot be null");
        }
        if (destination == null) {
            throw new IllegalArgumentException(
                    "Export destination cannot be null"
            );
        }
    }
}

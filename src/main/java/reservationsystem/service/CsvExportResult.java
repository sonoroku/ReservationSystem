package reservationsystem.service;

import java.nio.file.Path;

public class CsvExportResult {

    public enum Status {
        SUCCESS,
        EMPTY_DATA,
        FILE_EXISTS,
        WRITE_FAILED
    }

    private final Status status;
    private final String message;
    private final Path destination;

    private CsvExportResult(
            Status status,
            String message,
            Path destination
    ) {
        this.status = status;
        this.message = message;
        this.destination = destination;
    }

    public static CsvExportResult success(Path destination) {
        return new CsvExportResult(
                Status.SUCCESS,
                "Report exported successfully",
                destination
        );
    }

    public static CsvExportResult emptyData(Path destination) {
        return new CsvExportResult(
                Status.EMPTY_DATA,
                "There is no report data to export",
                destination
        );
    }

    public static CsvExportResult fileExists(Path destination) {
        return new CsvExportResult(
                Status.FILE_EXISTS,
                "The selected file already exists",
                destination
        );
    }

    public static CsvExportResult writeFailed(
            Path destination,
            String detail
    ) {
        String message = "Unable to export the report";
        if (detail != null && !detail.isBlank()) {
            message += ": " + detail;
        }
        return new CsvExportResult(
                Status.WRITE_FAILED,
                message,
                destination
        );
    }

    public Status getStatus() {
        return status;
    }

    public boolean isSuccessful() {
        return status == Status.SUCCESS;
    }

    public String getMessage() {
        return message;
    }

    public Path getDestination() {
        return destination;
    }
}

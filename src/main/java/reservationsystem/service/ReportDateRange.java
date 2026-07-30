package reservationsystem.service;

import java.time.LocalDate;

public class ReportDateRange {

    private final LocalDate startDate;
    private final LocalDate endDate;

    ReportDateRange(LocalDate startDate, LocalDate endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public boolean includes(LocalDate date) {
        if (date == null) {
            return false;
        }

        return !date.isBefore(startDate) && !date.isAfter(endDate);
    }
}

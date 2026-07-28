package reservationsystem.service;

import reservationsystem.model.DailyReservationSummary;
import reservationsystem.model.Reservation;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class DailyReservationSummaryService {

    public DailyReservationSummaryResult summarize(
            List<Reservation> currentUserReservations
    ) {
        if (currentUserReservations == null) {
            throw new IllegalArgumentException(
                    "Reservations cannot be null"
            );
        }

        Map<LocalDate, List<Reservation>> reservationsByDate =
                new TreeMap<>();

        for (Reservation reservation : currentUserReservations) {
            reservationsByDate
                    .computeIfAbsent(
                            reservation.getDate(),
                            ignored -> new ArrayList<>()
                    )
                    .add(reservation);
        }

        Comparator<Reservation> chronologicalOrder =
                Comparator.comparing(Reservation::getStartTime)
                        .thenComparing(Reservation::getEndTime)
                        .thenComparingInt(Reservation::getId);

        List<DailyReservationSummary> summaries = new ArrayList<>();

        for (Map.Entry<LocalDate, List<Reservation>> entry
                : reservationsByDate.entrySet()) {
            List<Reservation> dailyReservations =
                    new ArrayList<>(entry.getValue());
            dailyReservations.sort(chronologicalOrder);

            summaries.add(new DailyReservationSummary(
                    entry.getKey(),
                    dailyReservations
            ));
        }

        return DailyReservationSummaryResult.from(summaries);
    }
}

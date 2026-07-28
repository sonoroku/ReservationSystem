package reservationsystem.service;

import reservationsystem.model.Reservation;
import reservationsystem.model.Space;
import reservationsystem.model.User;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReservationReportService {

    public ReservationReportResult generateAllReservationsReport(
            User currentUser,
            List<Reservation> reservations,
            List<Space> spaces
    ) {
        if (currentUser == null) {
            return ReservationReportResult.unauthorized("An authenticated administrator is required");
        }

        if (!currentUser.isAdmin()) {
            return ReservationReportResult.unauthorized("Only administrators can view all reservations");
        }

        if (reservations == null) {
            throw new IllegalArgumentException("Reservations cannot be null");
        }

        if (spaces == null) {
            throw new IllegalArgumentException("Spaces cannot be null");
        }

        Map<Integer, Space> spacesById = new HashMap<>();

        for (Space space : spaces) {
            spacesById.put(space.getId(), space);
        }

        List<ReservationReportEntry> entries = new ArrayList<>();

        for (Reservation reservation : reservations) {
            Space space = spacesById.get(reservation.getSpaceId());

            String spaceName = space == null ? "Unknown Space" : space.getName();
            String building = space == null ? "Unknown Building" : space.getBuilding();

            entries.add(new ReservationReportEntry(
                    reservation.getId(),
                    reservation.getSpaceId(),
                    spaceName,
                    building,
                    reservation.getUserId(),
                    reservation.getDate(),
                    reservation.getStartTime(),
                    reservation.getEndTime()
            ));
        }

        entries.sort(
                Comparator.comparing(ReservationReportEntry::getDate)
                        .thenComparing(ReservationReportEntry::getStartTime)
                        .thenComparing(ReservationReportEntry::getEndTime)
                        .thenComparing(ReservationReportEntry::getSpaceName)
                        .thenComparing(ReservationReportEntry::getReservationId)
        );

        return ReservationReportResult.success(entries);
    }
}

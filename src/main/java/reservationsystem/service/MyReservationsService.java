package reservationsystem.service;

import reservationsystem.model.Reservation;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class MyReservationsService {

    public List<Reservation> getReservationsForUser(String userId, List<Reservation> reservations) {
        if (userId == null || userId.isBlank()) {
            throw new IllegalArgumentException("User ID is required");
        }

        if (reservations == null) {
            throw new IllegalArgumentException("Reservations cannot be null");
        }

        List<Reservation> userReservations = new ArrayList<>();

        for (Reservation reservation : reservations) {
            if (reservation.getUserId().equals(userId)) {
                userReservations.add(reservation);
            }
        }

        userReservations.sort(
                Comparator.comparing(Reservation::getDate)
                        .thenComparing(Reservation::getStartTime)
                        .thenComparingInt(Reservation::getId)
        );

        return userReservations;
    }
}

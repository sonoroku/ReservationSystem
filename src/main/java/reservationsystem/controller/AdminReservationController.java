package reservationsystem.controller;

import reservationsystem.model.Reservation;
import reservationsystem.model.User;
import reservationsystem.persistence.ReservationJsonRepository;
import reservationsystem.persistence.UserJsonRepository;
import reservationsystem.service.AdminReservationCreationResult;
import reservationsystem.service.AuthenticatedUserProvider;
import reservationsystem.service.AuthorizationResult;
import reservationsystem.service.AuthorizationService;
import reservationsystem.service.ReservationService;
import reservationsystem.service.ReservationValidationResult;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class AdminReservationController {

	private final ReservationJsonRepository reservationJsonRepository;
    private final UserJsonRepository userJsonRepository;
    private final ReservationService reservationService;
    private final AuthorizationService authorizationService;

    public AdminReservationController(
            AuthenticatedUserProvider authenticatedUserProvider
    ) {
        this(
                new ReservationJsonRepository(),
                new UserJsonRepository(),
                new ReservationService(),
                new AuthorizationService(authenticatedUserProvider)
        );
    }

    public AdminReservationController(
            ReservationJsonRepository reservationJsonRepository,
            UserJsonRepository userJsonRepository,
            ReservationService reservationService,
            AuthorizationService authorizationService
    ) {
        if (reservationJsonRepository == null) {
            throw new IllegalArgumentException(
                    "Reservation repository cannot be null"
            );
        }

        if (userJsonRepository == null) {
            throw new IllegalArgumentException(
                    "User repository cannot be null"
            );
        }

        if (reservationService == null) {
            throw new IllegalArgumentException(
                    "Reservation service cannot be null"
            );
        }

        if (authorizationService == null) {
            throw new IllegalArgumentException(
                    "Authorization service cannot be null"
            );
        }

        this.reservationJsonRepository = reservationJsonRepository;
        this.userJsonRepository = userJsonRepository;
        this.reservationService = reservationService;
        this.authorizationService = authorizationService;
    }

    public List<String> getAvailableUserIds() {
        AuthorizationResult authorizationResult =
                authorizationService.checkAdminAccess();

        if (!authorizationResult.isAuthorized()) {
            throw new IllegalStateException(
                    authorizationResult.getMessage()
            );
        }

        return userJsonRepository.loadUsers()
                .stream()
                .map(User::getUsername)
                .sorted(String.CASE_INSENSITIVE_ORDER)
                .toList();
    }

    public AdminReservationCreationResult createReservationForUser(
            String targetUserId,
            int spaceId,
            LocalDate date,
            LocalTime startTime,
            LocalTime endTime
    ) {
        AuthorizationResult authorizationResult =
                authorizationService.checkAdminAccess();

        if (!authorizationResult.isAuthorized()) {
            return AdminReservationCreationResult.unauthorized(
                    authorizationResult.getMessage()
            );
        }

        if (date == null) {
            return AdminReservationCreationResult.validationFailed(
                    "Reservation date is required"
            );
        }

        User targetUser = findUser(targetUserId);

        if (targetUser == null) {
            return AdminReservationCreationResult.invalidUser();
        }

        List<Reservation> existingReservations = new ArrayList<>(
                reservationJsonRepository.loadReservations()
        );

        Reservation newReservation = new Reservation(
                getNextReservationId(existingReservations),
                spaceId,
                targetUser.getUsername(),
                date,
                startTime,
                endTime
        );

        ReservationValidationResult validationResult =
                reservationService.validateReservation(
                        newReservation,
                        existingReservations
                );

        if (!validationResult.isValid()) {
            return AdminReservationCreationResult.validationFailed(
                    validationResult.getMessage()
            );
        }

        existingReservations.add(newReservation);
        reservationJsonRepository.saveReservations(existingReservations);

        return AdminReservationCreationResult.success(newReservation);
    }

    private User findUser(String targetUserId) {
        if (targetUserId == null || targetUserId.isBlank()) {
            return null;
        }

        String normalizedUserId = targetUserId.trim();

        return userJsonRepository.loadUsers()
                .stream()
                .filter(user -> user.getUsername().equalsIgnoreCase(
                        normalizedUserId
                ))
                .findFirst()
                .orElse(null);
    }

    private int getNextReservationId(
            List<Reservation> reservations
    ) {
        return reservations.stream()
                .max(Comparator.comparingInt(Reservation::getId))
                .map(reservation -> reservation.getId() + 1)
                .orElse(1);
    }

}

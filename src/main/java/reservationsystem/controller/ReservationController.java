package reservationsystem.controller;

import reservationsystem.model.Reservation;
import reservationsystem.model.Space;
import reservationsystem.persistence.ReservationJsonRepository;
import reservationsystem.service.CurrentUserProvider;
import reservationsystem.service.MyReservationsService;
import reservationsystem.service.ReservationService;
import reservationsystem.service.ReservationValidationResult;
import reservationsystem.service.ReservationCancellationResult;
import reservationsystem.service.ReservationModificationResult;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class ReservationController {

    private final ReservationJsonRepository reservationJsonRepository;
    private final ReservationService reservationService;
    private final CurrentUserProvider currentUserProvider;
    private final MyReservationsService myReservationsService;
    private final SpaceController spaceController;

    public ReservationController(CurrentUserProvider currentUserProvider) {
        this(
                new ReservationJsonRepository(),
                new ReservationService(),
                currentUserProvider,
                new MyReservationsService(),
                new SpaceController()
        );
    }

    public ReservationController(
            ReservationJsonRepository reservationJsonRepository,
            ReservationService reservationService,
            CurrentUserProvider currentUserProvider
    ) {
        this(
                reservationJsonRepository,
                reservationService,
                currentUserProvider,
                new MyReservationsService(),
                new SpaceController()
        );
    }

    public ReservationController(
            ReservationJsonRepository reservationJsonRepository,
            ReservationService reservationService,
            CurrentUserProvider currentUserProvider,
            MyReservationsService myReservationsService,
            SpaceController spaceController
    ) {
        if (currentUserProvider == null) {
            throw new IllegalArgumentException(
                    "Current user provider cannot be null"
            );
        }

        this.reservationJsonRepository = reservationJsonRepository;
        this.reservationService = reservationService;
        this.currentUserProvider = currentUserProvider;
        this.myReservationsService = myReservationsService;
        this.spaceController = spaceController;
    }

    public ReservationValidationResult createReservation(
            int spaceId,
            LocalDate date,
            LocalTime startTime,
            LocalTime endTime
    ) {
        List<Reservation> existingReservations = reservationJsonRepository.loadReservations();

        int newReservationId = getNextReservationId(existingReservations);

        Reservation newReservation = new Reservation(
                newReservationId,
                spaceId,
                currentUserProvider.getCurrentUserId(),
                date,
                startTime,
                endTime
        );

        ReservationValidationResult validationResult =
                reservationService.validateReservation(newReservation, existingReservations);

        if (!validationResult.isValid()) {
            return validationResult;
        }

        existingReservations.add(newReservation);
        reservationJsonRepository.saveReservations(existingReservations);

        return ReservationValidationResult.valid();
    }

    public List<Reservation> getMyReservations() {
        List<Reservation> reservations = reservationJsonRepository.loadReservations();
        return myReservationsService.getReservationsForUser(
                currentUserProvider.getCurrentUserId(),
                reservations
        );
    }

    public Optional<Space> getSpaceForReservation(Reservation reservation) {
        if (reservation == null) {
            return Optional.empty();
        }

        return spaceController.getSpaceById(reservation.getSpaceId());
    }
    
    public ReservationCancellationResult cancelReservation(int reservationId) {
        List<Reservation> existingReservations = reservationJsonRepository.loadReservations();

        Reservation reservationToCancel = existingReservations.stream()
                .filter(reservation -> reservation.getId() == reservationId)
                .findFirst()
                .orElse(null);

        if (reservationToCancel == null) {
            return ReservationCancellationResult.notFound();
        }

        if (!currentUserProvider.getCurrentUserId().equals(reservationToCancel.getUserId())) {
            return ReservationCancellationResult.notOwned();
        }

        existingReservations.remove(reservationToCancel);
        reservationJsonRepository.saveReservations(existingReservations);

        return ReservationCancellationResult.success();
    }

    public ReservationModificationResult modifyReservation(
            int reservationId,
            int spaceId,
            LocalDate date,
            LocalTime startTime,
            LocalTime endTime
    ) {
        List<Reservation> existingReservations = reservationJsonRepository.loadReservations();

        int reservationIndex = -1;
        for (int index = 0; index < existingReservations.size(); index++) {
            if (existingReservations.get(index).getId() == reservationId) {
                reservationIndex = index;
                break;
            }
        }

        if (reservationIndex < 0) {
            return ReservationModificationResult.notFound();
        }

        Reservation existingReservation = existingReservations.get(reservationIndex);
        if (!currentUserProvider.getCurrentUserId().equals(existingReservation.getUserId())) {
            return ReservationModificationResult.notOwned();
        }

        Reservation updatedReservation = new Reservation(
                existingReservation.getId(),
                spaceId,
                existingReservation.getUserId(),
                date,
                startTime,
                endTime
        );

        ReservationValidationResult validationResult =
                reservationService.validateReservationUpdate(
                        updatedReservation,
                        existingReservations
                );

        if (!validationResult.isValid()) {
            return ReservationModificationResult.validationFailed(validationResult.getMessage());
        }

        existingReservations.set(reservationIndex, updatedReservation);
        reservationJsonRepository.saveReservations(existingReservations);

        return ReservationModificationResult.success();
    }

    private int getNextReservationId(List<Reservation> reservations) {
        return reservations.stream()
                .max(Comparator.comparingInt(Reservation::getId))
                .map(reservation -> reservation.getId() + 1)
                .orElse(1);
    }
}

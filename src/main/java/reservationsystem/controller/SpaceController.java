package reservationsystem.controller;

import reservationsystem.model.Space;
import reservationsystem.persistence.SpaceJsonRepository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class SpaceController {

    private static final int MINIMUM_CAPACITY = 1;
    private static final int MAXIMUM_CAPACITY = 500;

    private final SpaceJsonRepository spaceJsonRepository;

    public SpaceController() {
        this(new SpaceJsonRepository());
    }

    public SpaceController(SpaceJsonRepository spaceJsonRepository) {
        this.spaceJsonRepository = spaceJsonRepository;
    }

    public List<Space> getAllSpaces() {
        List<Space> spaces = new ArrayList<>(spaceJsonRepository.loadSpaces());

        spaces.sort(Comparator.comparing(Space::getName));

        return spaces;
    }

    public boolean hasSpaces() {
        return !getAllSpaces().isEmpty();
    }

    public SpaceFilterResult filterByMinimumCapacity(String minimumCapacityInput) {
        if (minimumCapacityInput == null || minimumCapacityInput.isBlank()) {
            return SpaceFilterResult.invalid("Minimum capacity is required.");
        }

        final int minimumCapacity;
        try {
            minimumCapacity = Integer.parseInt(minimumCapacityInput.trim());
        } catch (NumberFormatException exception) {
            return SpaceFilterResult.invalid("Minimum capacity must be a whole number from 1 to 500.");
        }

        if (minimumCapacity < MINIMUM_CAPACITY || minimumCapacity > MAXIMUM_CAPACITY) {
            return SpaceFilterResult.invalid("Minimum capacity must be between 1 and 500.");
        }

        List<Space> matchingSpaces = getAllSpaces().stream()
                .filter(space -> space.getCapacity() >= minimumCapacity)
                .toList();

        return SpaceFilterResult.success(matchingSpaces);
    }

    public Optional<Space> getSelectedSpaceDetails(Space selectedSpace) {
        return Optional.ofNullable(selectedSpace);
    }

    public Optional<Space> getSpaceById(int spaceId) {
        return getAllSpaces().stream()
                .filter(space -> space.getId() == spaceId)
                .findFirst();
    }
}

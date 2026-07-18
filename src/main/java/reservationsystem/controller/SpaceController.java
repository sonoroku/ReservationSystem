package reservationsystem.controller;

import reservationsystem.model.Space;
import reservationsystem.persistence.SpaceJsonRepository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class SpaceController {

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

    public Optional<Space> getSelectedSpaceDetails(Space selectedSpace) {
        return Optional.ofNullable(selectedSpace);
    }

    public Optional<Space> getSpaceById(int spaceId) {
        return getAllSpaces().stream()
                .filter(space -> space.getId() == spaceId)
                .findFirst();
    }
}

package reservationsystem.controller;

import reservationsystem.model.Space;
import reservationsystem.persistence.SpaceJsonRepository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

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
}
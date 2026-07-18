package reservationsystem.controller;

import reservationsystem.model.Space;

import java.util.List;

public record SpaceFilterResult(Status status, List<Space> spaces, String message) {

    public enum Status {
        SUCCESS,
        EMPTY,
        INVALID_INPUT
    }

    public SpaceFilterResult {
        spaces = List.copyOf(spaces);
    }

    public static SpaceFilterResult success(List<Space> spaces) {
        Status status = spaces.isEmpty() ? Status.EMPTY : Status.SUCCESS;
        String message = spaces.isEmpty() ? "No spaces match the filter." : "";
        return new SpaceFilterResult(status, spaces, message);
    }

    public static SpaceFilterResult invalid(String message) {
        return new SpaceFilterResult(Status.INVALID_INPUT, List.of(), message);
    }

    public boolean isValid() {
        return status != Status.INVALID_INPUT;
    }
}

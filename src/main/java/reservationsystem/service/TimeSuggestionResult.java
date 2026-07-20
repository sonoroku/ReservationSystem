package reservationsystem.service;

import reservationsystem.model.TimeSlot;

import java.util.List;

public class TimeSuggestionResult {
	
	public enum Status {
        SUCCESS,
        INVALID_INPUT
    }

    private final Status status;
    private final String message;
    private final List<TimeSlot> suggestions;

    private TimeSuggestionResult(
            Status status,
            String message,
            List<TimeSlot> suggestions
    ) {
        this.status = status;
        this.message = message;
        this.suggestions = List.copyOf(suggestions);
    }

    public static TimeSuggestionResult success(List<TimeSlot> suggestions) {
        if (suggestions == null) {
            throw new IllegalArgumentException(
                    "Suggestions cannot be null"
            );
        }

        String message = suggestions.isEmpty()
                ? "No available times found"
                : "Available times found";

        return new TimeSuggestionResult(
                Status.SUCCESS,
                message,
                suggestions
        );
    }

    public static TimeSuggestionResult invalid(String message) {
        return new TimeSuggestionResult(
                Status.INVALID_INPUT,
                message,
                List.of()
        );
    }

    public Status getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public List<TimeSlot> getSuggestions() {
        return suggestions;
    }

    public boolean isSuccessful() {
        return status == Status.SUCCESS;
    }

}

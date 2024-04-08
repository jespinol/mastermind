package org.jmel.mastermind.core.feedback;

public record OriginalMastermindFeedback(int wellPlaced, int misplaced) implements Feedback {
    @Override
    public String toString() {
        return String.format("%d correctly placed, %d misplaced", wellPlaced, misplaced);
    }
}

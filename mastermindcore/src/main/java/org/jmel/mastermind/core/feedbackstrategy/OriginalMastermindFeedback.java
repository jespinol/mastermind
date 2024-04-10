package org.jmel.mastermind.core.feedbackstrategy;

/**
 * An implementation of the Feedback interface based on the feedback found in many versions of the Mastermind game.
 */
public record OriginalMastermindFeedback(int wellPlaced, int misplaced) implements Feedback {
    @Override
    public String toString() {
        return "%d correctly placed, %d misplaced".formatted(wellPlaced, misplaced);
    }
}

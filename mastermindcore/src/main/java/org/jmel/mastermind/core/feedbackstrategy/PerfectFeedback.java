package org.jmel.mastermind.core.feedbackstrategy;

/**
 * An implementation of the Feedback interface. It is a variation of the OriginalMastermindFeedback, but only the number
 * of well-placed pegs is reported.
 */
public record PerfectFeedback(int wellPlaced) implements Feedback {
    @Override
    public String toString() {
        return "%d correctly placed".formatted(wellPlaced);
    }
}

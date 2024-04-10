package org.jmel.mastermind.core.feedbackstrategy;

/**
 * An implementation of the Feedback interface. It is a variation of the OriginalMastermindFeedback, but only the number
 * of well-placed pegs is reported.
 *
 * @param wellPlaced the number of correctly placed numbers in the guess
 */
public record PerfectFeedback(int wellPlaced) implements Feedback {
    /**
     * {@inheritDoc}
     * <p>
     * Reports the number of correctly placed numbers. Correctly placed refers to numbers that are in the secret code
     * and are in the correct position.
     */
    @Override
    public String toString() {
        return "%d correctly placed".formatted(wellPlaced);
    }
}

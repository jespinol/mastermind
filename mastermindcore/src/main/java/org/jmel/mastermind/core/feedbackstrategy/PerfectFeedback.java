package org.jmel.mastermind.core.feedbackstrategy;

/**
 * An implementation of the Feedback interface. It is a variation of the OriginalMastermindFeedback, but only the number
 * of well-placed pegs is reported.
 * <p></p>
 * For example, secret code [1,2,3,4] and guess [1,4,3,5] will yield "2 correctly placed"
 *
 * @param wellPlaced the number of correctly placed numbers in the guess
 */
public record PerfectFeedback(int wellPlaced) implements Feedback {
    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "%d correctly placed".formatted(wellPlaced);
    }
}

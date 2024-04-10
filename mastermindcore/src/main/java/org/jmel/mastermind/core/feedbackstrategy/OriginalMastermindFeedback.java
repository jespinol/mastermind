package org.jmel.mastermind.core.feedbackstrategy;

/**
 * An implementation of the Feedback interface based on the feedback found in the original Mastermind game.
 * <p></p>
 * Reports the number of correctly placed, and correct but misplaced elements. For example, secret code [1,2,3,4] and
 * guess [1,4,3,5] will yield "2 correctly placed, 1 misplaced"
 *
 * @param wellPlaced the number of correctly placed numbers in the guess
 * @param misplaced  the number of misplaced numbers in the guess
 */
public record OriginalMastermindFeedback(int wellPlaced, int misplaced) implements Feedback {
    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "%d correctly placed, %d misplaced".formatted(wellPlaced, misplaced);
    }
}

package org.jmel.mastermind.core.feedbackstrategy;

/**
 * An implementation of the Feedback interface based on the feedback found in many versions of the Mastermind game.
 *
 * @param wellPlaced the number of correctly placed numbers in the guess
 * @param misplaced  the number of misplaced numbers in the guess
 */
public record OriginalMastermindFeedback(int wellPlaced, int misplaced) implements Feedback {
    /**
     * {@inheritDoc}
     * <p>
     * Reports the number of correctly placed and misplaced numbers. Correctly placed refers to numbers that are in the
     * secret code and are in the correct position. Misplaced refers to numbers that are in the secret code but only to
     * those that are not in the correct position.
     */
    @Override
    public String toString() {
        return "%d correctly placed, %d misplaced".formatted(wellPlaced, misplaced);
    }
}

package org.jmel.mastermind.core.feedbackstrategy;

/**
 * An implementation of the Feedback interface based on the feedback found in the REACH Mastermind Challenge
 * instructions.
 *
 * @param correctPos the number of correctly placed numbers in the guess
 * @param correctNum the number of guess numbers that are in the code, regardless of position
 */
public record DefaultFeedback(int correctPos, int correctNum) implements Feedback {
    /**
     * {@inheritDoc}
     * <p>
     * Reports the number of correct numbers and correctly placed numbers. Correct numbers are numbers that are in the
     * secret code, regardless of their position. Correctly placed refers to numbers that are in the secret code and are
     * in the correct position.
     */
    @Override
    public String toString() {
        return String.format("%d correct numbers, %d correctly placed", correctNum, correctPos);
    }
}

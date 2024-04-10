package org.jmel.mastermind.core.feedbackstrategy;

/**
 * An implementation of the Feedback interface based on the feedback found in the REACH Mastermind Challenge
 * instructions.
 * <p></p>
 * * For example, secret code [1,2,3,4] and guess [1,4,3,5] will yield "3 correct numbers, 2 correctly placed"
 *
 * @param correctPos the number of correctly placed elements in the guess
 * @param correctNum the number of elements of the guess present in both the secret code and the guess, regardless of position

 */
public record DefaultFeedback(int correctPos, int correctNum) implements Feedback {
    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return String.format("%d correct numbers, %d correctly placed", correctNum, correctPos);
    }
}

package org.jmel.mastermind.core.feedback;

/**
 * An implementation of the Feedback interface based on the feedback found in the REACH Mastermind Challenge instructions.
 */
public record DefaultFeedback(int correctPos, int correctNum) implements Feedback {
    @Override
    public String toString() {
        return String.format("%d correct numbers, %d correctly placed", correctNum, correctPos);
    }
}

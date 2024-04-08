package org.jmel.mastermind.core.feedback;

public record DefaultFeedback(int correctPos, int correctNum) implements Feedback {
    @Override
    public String toString() {
        return String.format("%d correct numbers, %d correctly placed", correctNum, correctPos);
    }
}

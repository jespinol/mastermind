package org.jmel.mastermind.core.feedback;

public record PerfectFeedback(int wellPlaced) implements Feedback {
    @Override
    public String toString() {
        return String.format("%d correctly placed", wellPlaced);
    }
}

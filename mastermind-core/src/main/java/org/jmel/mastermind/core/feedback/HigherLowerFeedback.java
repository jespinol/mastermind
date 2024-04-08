package org.jmel.mastermind.core.feedback;

import java.util.List;

public record HigherLowerFeedback(List<Integer> scores) implements Feedback {
    public HigherLowerFeedback(List<Integer> scores) {
        this.scores = List.copyOf(scores);
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("> ");
        for (int score : scores) {
            if (score > 0) {
                result.append("+ ");
            } else if (score < 0) {
                result.append("- ");
            } else {
                result.append("= ");
            }
        }

        return result.toString();
    }
}

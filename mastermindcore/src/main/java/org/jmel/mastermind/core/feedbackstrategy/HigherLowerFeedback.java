package org.jmel.mastermind.core.feedbackstrategy;

import java.util.List;

/**
 * An implementation of the Feedback interface that provides higher, lower, or equal information for each position in the guess.
 */
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

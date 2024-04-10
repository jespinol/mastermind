package org.jmel.mastermind.core.feedbackstrategy;

import java.util.List;

/**
 * An implementation of the Feedback interface that provides higher, lower, or equal information for each position in
 * the guess. For example, with codes of length 1, the feedback '+' means that the guess was too high
 *
 * @param scores a list of integers representing where each element represents a feedback for that position in the guess
 */
public record HigherLowerFeedback(List<Integer> scores) implements Feedback {
    /**
     * Constructs a new HigherLowerFeedback object with the given scores by creating an immutable list of the scores.
     */
    public HigherLowerFeedback(List<Integer> scores) {
        this.scores = List.copyOf(scores);
    }

    /**
     * {@inheritDoc}
     * <p>
     * Reports whether each number in the guess is higher, lower, or equal to the corresponding number in the secret
     * code.
     */
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

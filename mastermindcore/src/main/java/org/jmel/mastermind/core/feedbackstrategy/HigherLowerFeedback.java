package org.jmel.mastermind.core.feedbackstrategy;

import java.util.List;

/**
 * An implementation of the Feedback interface that provides a {@code List<Integer>} of ones, zeros, and negative ones,
 * where:
 * <ul>
 *     <li>1 means that the guess element was higher than,</li>
 *     <li>0 means that the guess element was equal to, and</li>
 *     <li>-1 means that the guess element was lower than,</li>
 * </ul>
 * the corresponding secret code element.
 * <p></p>
 * For example, with codes of length 1, the feedback {@code -1} means that the guess was too high
 *
 * @param values a list of integers where each element represents the feedback for a position in the guess
 */
public record HigherLowerFeedback(List<Integer> values) implements Feedback {
    /**
     * Constructs a new HigherLowerFeedback object with the given values by creating an immutable list of the values.
     */
    public HigherLowerFeedback(List<Integer> values) {
        this.values = List.copyOf(values);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("> ");
        for (int value : values) {
            if (value > 0) {
                result.append("+ ");
            } else if (value < 0) {
                result.append("- ");
            } else {
                result.append("= ");
            }
        }

        return result.toString();
    }
}

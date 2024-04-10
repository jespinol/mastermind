package org.jmel.mastermind.core.feedbackstrategy;

/**
 * An interface to specify the output format of feedback provided by a {@link FeedbackStrategyImpl}.
 */
public interface Feedback {
    /**
     * Returns a string representation of a feedback.
     *
     * @return a string representing the feedback after making a valid guess
     */
    String toString();
}

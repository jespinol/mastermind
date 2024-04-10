package org.jmel.mastermind.core.feedbackstrategy;

import org.jmel.mastermind.core.Code;

/**
 * Represents an algorithm for computing feedback for a guess of a secret code.
 */
public interface FeedbackStrategy {
    /**
     * Returns a feedback based on the secret code and the guess.
     *
     * @param secretCode the secret code
     * @param guess      the guess
     * @return a feedback based on the secret code and the guess
     */
    Feedback get(Code secretCode, Code guess);
}

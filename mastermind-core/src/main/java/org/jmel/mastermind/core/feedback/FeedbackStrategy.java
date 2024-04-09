package org.jmel.mastermind.core.feedback;

import org.jmel.mastermind.core.Code;

/**
 * Interface for a strategy to provide feedback for a guess.
 * Implementations provide a feedback based on the secret code and the guess.
 */
public interface FeedbackStrategy {
    Feedback get(Code secretCode, Code guess);
}

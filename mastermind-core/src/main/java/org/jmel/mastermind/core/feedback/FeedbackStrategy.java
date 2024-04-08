package org.jmel.mastermind.core.feedback;

import org.jmel.mastermind.core.Code;

public interface FeedbackStrategy {
    Feedback get(Code secretCode, Code guess);
}

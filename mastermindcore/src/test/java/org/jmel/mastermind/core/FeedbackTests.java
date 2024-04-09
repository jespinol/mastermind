package org.jmel.mastermind.core;

import org.jmel.mastermind.core.feedbackstrategy.*;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.jmel.mastermind.core.feedbackstrategy.FeedbackStrategyImpl.*;
import static org.junit.jupiter.api.Assertions.*;

public class FeedbackTests {
    @Nested
    public class FeedbackStrategyTests {
        private final static Code secretCode = Code.from(List.of(1, 2, 3, 4), 4, 8);
        private final static Code correctGuess = Code.from(List.of(1, 2, 3, 4), 4, 8);

        @Test
        void defaultFeedbackIsInstantiated() {
            Feedback feedback = DEFAULT.get(secretCode, correctGuess);
            assertInstanceOf(DefaultFeedback.class, feedback);
        }

        @Test
        void originalFeedbackIsInstantiated() {
            Feedback feedback = ORIGINAL_MASTERMIND.get(secretCode, correctGuess);
            assertInstanceOf(OriginalMastermindFeedback.class, feedback);
        }

        @Test
        void highLowFeedbackIsInstantiated() {
            Feedback feedback = HIGHER_LOWER.get(secretCode, correctGuess);
            assertInstanceOf(HigherLowerFeedback.class, feedback);
        }

        @Test
        void perfectFeedbackIsInstantiated() {
            Feedback feedback = PERFECT.get(secretCode, correctGuess);
            assertInstanceOf(PerfectFeedback.class, feedback);
        }
    }

    @Nested
    public class FeedbackComputationTests {
        private final static Code secretCode = Code.from(List.of(0, 1, 3, 5), 4, 8);
        private final static Code guess1 = Code.from(List.of(2, 2, 4, 6), 4, 8);
        private final static Code guess2 = Code.from(List.of(0, 2, 4, 6), 4, 8);
        private final static Code guess3 = Code.from(List.of(2, 2, 1, 1), 4, 8);
        private final static Code guess4 = Code.from(List.of(0, 1, 5, 6), 4, 8);
        private final static Code guess5 = Code.from(List.of(0, 1, 3, 5), 4, 8);

        @Test
        void defaultFeedbackComputesCorrectly() {
            DefaultFeedback f1 = (DefaultFeedback) DEFAULT.get(secretCode, guess1);
            DefaultFeedback e1 = new DefaultFeedback(0, 0);

            DefaultFeedback f2 = (DefaultFeedback) DEFAULT.get(secretCode, guess2);
            DefaultFeedback e2 = new DefaultFeedback(1, 1);

            DefaultFeedback f3 = (DefaultFeedback) DEFAULT.get(secretCode, guess3);
            DefaultFeedback e3 = new DefaultFeedback(0, 1);

            DefaultFeedback f4 = (DefaultFeedback) DEFAULT.get(secretCode, guess4);
            DefaultFeedback e4 = new DefaultFeedback(2, 3);

            DefaultFeedback f5 = (DefaultFeedback) DEFAULT.get(secretCode, guess5);
            DefaultFeedback e5 = new DefaultFeedback(4, 4);

            assertAll(
                    () -> assertEquals(f1, e1),
                    () -> assertEquals(f2, e2),
                    () -> assertEquals(f3, e3),
                    () -> assertEquals(f4, e4),
                    () -> assertEquals(f5, e5)
            );
        }

        @Test
        void originalFeedbackComputesCorrectly() {
            OriginalMastermindFeedback f1 = (OriginalMastermindFeedback) ORIGINAL_MASTERMIND.get(secretCode, guess1);
            OriginalMastermindFeedback e1 = new OriginalMastermindFeedback(0, 0);

            OriginalMastermindFeedback f2 = (OriginalMastermindFeedback) ORIGINAL_MASTERMIND.get(secretCode, guess2);
            OriginalMastermindFeedback e2 = new OriginalMastermindFeedback(1, 0);

            OriginalMastermindFeedback f3 = (OriginalMastermindFeedback) ORIGINAL_MASTERMIND.get(secretCode, guess3);
            OriginalMastermindFeedback e3 = new OriginalMastermindFeedback(0, 1);

            OriginalMastermindFeedback f4 = (OriginalMastermindFeedback) ORIGINAL_MASTERMIND.get(secretCode, guess4);
            OriginalMastermindFeedback e4 = new OriginalMastermindFeedback(2, 1);

            OriginalMastermindFeedback f5 = (OriginalMastermindFeedback) ORIGINAL_MASTERMIND.get(secretCode, guess5);
            OriginalMastermindFeedback e5 = new OriginalMastermindFeedback(4, 0);

            assertAll(
                    () -> assertEquals(f1, e1),
                    () -> assertEquals(f2, e2),
                    () -> assertEquals(f3, e3),
                    () -> assertEquals(f4, e4),
                    () -> assertEquals(f5, e5)
            );
        }

        @Test
        void highLowFeedbackComputesCorrectly() {
            HigherLowerFeedback f1 = (HigherLowerFeedback) HIGHER_LOWER.get(secretCode, guess1);
            HigherLowerFeedback e1 = new HigherLowerFeedback(List.of(1, 1, 1, 1));

            HigherLowerFeedback f2 = (HigherLowerFeedback) HIGHER_LOWER.get(secretCode, guess2);
            HigherLowerFeedback e2 = new HigherLowerFeedback(List.of(0, 1, 1, 1));

            HigherLowerFeedback f3 = (HigherLowerFeedback) HIGHER_LOWER.get(secretCode, guess3);
            HigherLowerFeedback e3 = new HigherLowerFeedback(List.of(1, 1, -1, -1));

            HigherLowerFeedback f4 = (HigherLowerFeedback) HIGHER_LOWER.get(secretCode, guess4);
            HigherLowerFeedback e4 = new HigherLowerFeedback(List.of(0, 0, 1, 1));

            HigherLowerFeedback f5 = (HigherLowerFeedback) HIGHER_LOWER.get(secretCode, guess5);
            HigherLowerFeedback e5 = new HigherLowerFeedback(List.of(0, 0, 0, 0));

            assertAll(
                    () -> assertEquals(f1, e1),
                    () -> assertEquals(f2, e2),
                    () -> assertEquals(f3, e3),
                    () -> assertEquals(f4, e4),
                    () -> assertEquals(f5, e5)
            );
        }

        @Test
        void highLowFeedbackIsImmutable() {
            List<Integer> scoreList = new ArrayList<>(List.of(1, 1, 1, 1));
            HigherLowerFeedback f1 = new HigherLowerFeedback(scoreList);

            scoreList.set(0, 0);
            HigherLowerFeedback f2 = new HigherLowerFeedback(scoreList);

            assertNotEquals(f1, f2);
        }

        @Test
        void perfectFeedbackComputesCorrectly() {
            PerfectFeedback f1 = (PerfectFeedback) PERFECT.get(secretCode, guess1);
            PerfectFeedback e1 = new PerfectFeedback(0);

            PerfectFeedback f2 = (PerfectFeedback) PERFECT.get(secretCode, guess2);
            PerfectFeedback e2 = new PerfectFeedback(1);

            PerfectFeedback f3 = (PerfectFeedback) PERFECT.get(secretCode, guess3);
            PerfectFeedback e3 = new PerfectFeedback(0);

            PerfectFeedback f4 = (PerfectFeedback) PERFECT.get(secretCode, guess4);
            PerfectFeedback e4 = new PerfectFeedback(2);

            PerfectFeedback f5 = (PerfectFeedback) PERFECT.get(secretCode, guess5);
            PerfectFeedback e5 = new PerfectFeedback(4);

            assertAll(
                    () -> assertEquals(f1, e1),
                    () -> assertEquals(f2, e2),
                    () -> assertEquals(f3, e3),
                    () -> assertEquals(f4, e4),
                    () -> assertEquals(f5, e5)
            );
        }
    }
}

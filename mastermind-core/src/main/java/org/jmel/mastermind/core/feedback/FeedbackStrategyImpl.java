package org.jmel.mastermind.core.feedback;

import org.jmel.mastermind.core.Code;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum FeedbackStrategyImpl implements FeedbackStrategy {
    DEFAULT {
        @Override
        public DefaultFeedback get(Code secretCode, Code guess) {
            int correctPos = calculatePerfectMatches(secretCode, guess);
            int correctNum = calculateColorMatches(secretCode, guess);

            return new DefaultFeedback(correctPos, correctNum);
        }
    },
    ORIGINAL_MASTERMIND {
        @Override
        public OriginalMastermindFeedback get(Code secretCode, Code guess) {
            int wellPlaced = calculatePerfectMatches(secretCode, guess);
            int misplaced = calculateColorMatches(secretCode, guess) - wellPlaced;

            return new OriginalMastermindFeedback(wellPlaced, misplaced);
        }
    },
    HIGHER_LOWER {
        @Override
        public HigherLowerFeedback get(Code secretCode, Code guess) {
            List<Integer> scores = new ArrayList<>();

            for (int i = 0; i < secretCode.value().size(); i++) {
                int diff = secretCode.value().get(i) - guess.value().get(i);
                if (diff > 0) {
                    scores.add(-1);
                } else if (diff < 0) {
                    scores.add(1);
                } else {
                    scores.add(0);
                }
            }

            return new HigherLowerFeedback(scores);
        }
    },
    PERFECT {
        @Override
        public PerfectFeedback get(Code secretCode, Code guess) {
            int perfectMatches = calculatePerfectMatches(secretCode, guess);

            return new PerfectFeedback(perfectMatches);
        }
    };

    private static int calculatePerfectMatches(Code c1, Code c2) {
        int perfectMatches = 0;
        for (int i = 0; i < c1.value().size(); i++) {
            if (c1.value().get(i).equals(c2.value().get(i))) {
                perfectMatches++;
            }
        }

        return perfectMatches;
    }

    private static int calculateColorMatches(Code c1, Code c2) {
        Map<Integer, Integer> c1Freq = createFrequencyMap(c1);
        Map<Integer, Integer> c2Freq = createFrequencyMap(c2);

        int allMatches = 0;
        for (int digit : c2Freq.keySet()) {
            int timesInC1 = c2Freq.get(digit);
            int timesInC2 = c1Freq.getOrDefault(digit, 0);
            allMatches += Math.min(timesInC1, timesInC2);
        }

        return allMatches;
    }

    private static Map<Integer, Integer> createFrequencyMap(Code c) {
        Map<Integer, Integer> freq = new HashMap<>();
        for (int i = 0; i < c.value().size(); i++) {
            int c1Digit = c.value().get(i);
            freq.put(c1Digit, freq.getOrDefault(c1Digit, 0) + 1);
        }

        return freq;
    }
}

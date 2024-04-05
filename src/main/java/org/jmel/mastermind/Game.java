package org.jmel.mastermind;

import org.jmel.mastermind.enums.CodeGenerationPreference;
import org.jmel.mastermind.secret_code_suppliers.ApiCodeSupplier;
import org.jmel.mastermind.secret_code_suppliers.LocalRandomCodeSupplier;
import org.jmel.mastermind.secret_code_suppliers.UserDefinedCodeSupplier;

import java.util.*;
import java.util.function.Supplier;

import static org.jmel.mastermind.enums.CodeGenerationPreference.RANDOM_ORG_API;
import static org.jmel.mastermind.enums.CodeGenerationPreference.USER_DEFINED;

public class Game {
    private final int codeLength;
    private final int numColors;
    private final int maxAttempts;
    private final Code secretCode;
    private final List<Code> guessHistory = new ArrayList<>();

    private Game(Builder builder) {
        this.codeLength = builder.codeLength;
        this.numColors = builder.numColors;
        this.maxAttempts = builder.maxAttempts;
        this.secretCode = builder.codeSupplier.get();
    }

    public static class Builder {
        private int codeLength = 4;
        private int numColors = 8;
        private int maxAttempts = 10;
        private CodeGenerationPreference codeGenerationPreference = RANDOM_ORG_API;
        private List<Integer> secretCodeInput; // Required for user defined secret code
        private Supplier<Code> codeSupplier;

        public Builder codeLength(int codeLength) {
            if (codeLength < 1) throw new IllegalArgumentException("Invalid code length");
            this.codeLength = codeLength;
            return this;
        }

        public Builder numColors(int numColors) {
            if (numColors < 1) throw new IllegalArgumentException("Invalid number of colors");
            this.numColors = numColors;
            return this;
        }

        public Builder maxAttempts(int maxAttempts) {
            if (maxAttempts < 1) throw new IllegalArgumentException("Invalid number of attempts");
            this.maxAttempts = maxAttempts;
            return this;
        }

        public Builder strategy(CodeGenerationPreference strategy) {
            if (Objects.isNull(strategy)) throw new IllegalArgumentException("Invalid strategy");
            this.codeGenerationPreference = strategy;
            return this;
        }

        public Builder secretCode(List<Integer> secretCode) {
            if (Objects.isNull(secretCode)) throw new IllegalArgumentException("Invalid secret code");
            this.secretCodeInput = secretCode;
            this.codeGenerationPreference = USER_DEFINED;
            this.codeLength = secretCode.size();
            return this;
        }

        public Game build() {
            setCodeSupplier();

            return new Game(this);
        }

        private void setCodeSupplier() {
            // They provided a secretCode, but they also specified they wanted a code generated for them
            if (!codeGenerationPreference.equals(USER_DEFINED) && Objects.nonNull(secretCodeInput)) {
                throw new IllegalArgumentException("Cannot specify a code with selected strategy");
            }

            // They intended to provide their own secret code, but didn't provide it, or provided it but violated configured values
            if (codeGenerationPreference.equals(USER_DEFINED)) {
                if (Objects.isNull(secretCodeInput))
                    throw new IllegalArgumentException("Null secret code"); // TODO all code validation should be consolidated
                if (secretCodeInput.size() != codeLength)
                    throw new IllegalArgumentException("Invalid secret code length");
                if (secretCodeInput.stream().anyMatch(i -> i < 0 || i >= numColors))
                    throw new IllegalArgumentException("Invalid secret code colors");
            }

            this.codeSupplier = switch (codeGenerationPreference) {
                case RANDOM_ORG_API -> new ApiCodeSupplier(codeLength, numColors);
                case LOCAL_RANDOM -> new LocalRandomCodeSupplier(codeLength, numColors);
                case USER_DEFINED -> new UserDefinedCodeSupplier(secretCodeInput);
                default -> throw new IllegalArgumentException("Unimplemented CodeSupplier strategy");
            };
        }
    }

    public Feedback processGuess(List<Integer> guessInput) {
        if (isGameOver()) {
            throw new IllegalStateException("Max attempts reached"); // TODO custom exception
        }

        Code guess = new Code(guessInput);
        guessHistory.add(guess);
        return computeFeedback(this.secretCode, guess);
    }

    public int getMovesLeft() {
        return maxAttempts - guessHistory.size();
    }

    private Feedback computeFeedback(Code secretCode, Code guess) {
        int wellPlaced = 0;
        Map<Integer, Integer> secretFreq = new HashMap<>();
        Map<Integer, Integer> guessFreq = new HashMap<>();

        for (int i = 0; i < secretCode.getValue().size(); i++) {
            int secretDigit = secretCode.getValue().get(i);
            int guessDigit = guess.getValue().get(i);
            if (secretDigit == guessDigit) {
                wellPlaced++;
            } else {
                secretFreq.put(secretDigit, secretFreq.getOrDefault(secretDigit, 0) + 1);
                guessFreq.put(guessDigit, guessFreq.getOrDefault(guessDigit, 0) + 1);
            }
        }

        int misplaced = 0;
        for (int digit : guessFreq.keySet()) {
            int timesInGuess = guessFreq.get(digit);
            int timesInSecret = secretFreq.getOrDefault(digit, 0);
            misplaced += Math.min(timesInGuess, timesInSecret);
        }

        return new Feedback(wellPlaced, misplaced);
    }

    public boolean isGameOver() {
        boolean wonGame = !guessHistory.isEmpty() && Objects.equals(guessHistory.get(guessHistory.size() - 1), secretCode);
        return wonGame || getMovesLeft() == 0; // TODO ensure movesLeft can't be negative
    }
}

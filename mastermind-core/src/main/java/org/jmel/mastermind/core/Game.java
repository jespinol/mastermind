package org.jmel.mastermind.core;


import org.jmel.mastermind.core.secret_code_suppliers.*;

import java.io.IOException;
import java.util.*;

import static org.jmel.mastermind.core.secret_code_suppliers.CodeGenerationPreference.*;

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
        this.secretCode = builder.secretCode;
    }

    public Feedback processGuess(List<Integer> guessInput) {
        if (isGameWon())
            throw new IllegalStateException("Game already won!");

        if (movesCompleted() == maxAttempts)
            throw new IllegalStateException("Game is over. Cannot make more guesses.");

        Code guess = Code.from(guessInput, this.codeLength, this.numColors);
        guessHistory.add(guess);

        return computeFeedback(this.secretCode, guess);
    }

    private Feedback computeFeedback(Code secretCode, Code guess) {
        int wellPlaced = 0;
        Map<Integer, Integer> secretFreq = new HashMap<>();
        Map<Integer, Integer> guessFreq = new HashMap<>();

        for (int i = 0; i < secretCode.value().size(); i++) {
            int secretDigit = secretCode.value().get(i);
            int guessDigit = guess.value().get(i);
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

    public int movesCompleted() {
        if (guessHistory.size() > maxAttempts) throw new IllegalStateException("Game in illegal state -- more moves than attempts");

        return guessHistory.size();
    }

    public boolean isGameWon() {
        return !guessHistory.isEmpty() && Objects.equals(guessHistory.get(guessHistory.size() - 1), secretCode);
    }

    public int codeLength() {
        return this.codeLength;
    }

    public int numColors() {
        return this.numColors;
    }

    public int maxAttempts() {
        return this.maxAttempts;
    }

    public static class Builder {
        private int codeLength = 4;
        private int numColors = 8;
        private int maxAttempts = 10;
        private CodeGenerationPreference codeGenerationPreference = RANDOM_ORG_API;
        private List<Integer> secretCodeInput; // Required for user defined secret code
        private Code secretCode;

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

        public Builder codeGenerationPreference(CodeGenerationPreference preference) {
            if (Objects.isNull(preference)) throw new IllegalArgumentException("Invalid code generation preference");
            this.codeGenerationPreference = preference;

            return this;
        }

        public Builder secretCode(List<Integer> secretCode) {
            if (Objects.isNull(secretCode)) throw new IllegalArgumentException("Invalid secret code");
            this.secretCodeInput = secretCode;
            this.codeGenerationPreference = USER_DEFINED;
            this.codeLength = secretCode.size();

            return this;
        }

        public int getCodeLength() {
            return codeLength;
        }

        public int getNumColors() {
            return numColors;
        }

        public int getMaxAttempts() {
            return maxAttempts;
        }

        public Game build() throws IOException {
            if (!codeGenerationPreference.equals(USER_DEFINED) && Objects.nonNull(secretCodeInput)) {
                throw new IllegalArgumentException("Cannot specify a code with selected strategy");
            }

            CodeSupplier codeSupplier = switch (this.codeGenerationPreference) {
                case RANDOM_ORG_API -> new ApiCodeSupplier(codeLength, numColors);
                case LOCAL_RANDOM -> new LocalRandomCodeSupplier(codeLength, numColors);
                case USER_DEFINED -> new UserDefinedCodeSupplier(Code.from(secretCodeInput, codeLength, numColors));
                default -> throw new IllegalArgumentException("Unimplemented CodeSupplier strategy");
            };

            try {
                this.secretCode = codeSupplier.get();
                return new Game(this);

            } catch (IOException e) {
                throw new IOException("Failed to supply code with " + codeGenerationPreference, e);
            }
        }
    }
}

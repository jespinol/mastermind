package org.jmel.mastermind.core;

import org.jmel.mastermind.core.feedback.Feedback;
import org.jmel.mastermind.core.feedback.FeedbackStrategy;
import org.jmel.mastermind.core.feedback.FeedbackStrategyImpl;
import org.jmel.mastermind.core.secret_code_suppliers.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.jmel.mastermind.core.secret_code_suppliers.CodeGenerationPreference.RANDOM_ORG_API;
import static org.jmel.mastermind.core.secret_code_suppliers.CodeGenerationPreference.USER_DEFINED;

public class Game {
    private final int codeLength;
    private final int numColors;
    private final int maxAttempts;
    private final Code secretCode;
    private final List<Code> guessHistory = new ArrayList<>();
    private final FeedbackStrategy feedbackStrategy;

    private Game(Builder builder) {
        this.codeLength = builder.codeLength;
        this.numColors = builder.numColors;
        this.maxAttempts = builder.maxAttempts;
        this.secretCode = builder.secretCode;
        this.feedbackStrategy = builder.feedbackStrategy;
    }

    public Feedback processGuess(List<Integer> guessInput) {
        if (isGameWon())
            throw new IllegalStateException("Game already won!");

        if (movesCompleted() == maxAttempts)
            throw new IllegalStateException("Game is over. Cannot make more guesses.");

        Code guess = Code.from(guessInput, this.codeLength, this.numColors);
        guessHistory.add(guess);


        return feedbackStrategy.get(secretCode, guess);
    }

    public int movesCompleted() {
        if (guessHistory.size() > maxAttempts)
            throw new IllegalStateException("Game in illegal state -- more moves than attempts");

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
        private CodeGenerationPreference codeSupplierPreference = RANDOM_ORG_API;
        private List<Integer> secretCodeInput; // Required for user defined secret code
        private FeedbackStrategy feedbackStrategy = FeedbackStrategyImpl.DEFAULT;
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

        public Builder codeGenerationPreference(CodeGenerationPreference supplier) {
            if (Objects.isNull(supplier)) throw new IllegalArgumentException("Invalid code generation preference");
            this.codeSupplierPreference = supplier;

            return this;
        }

        public Builder secretCode(List<Integer> secretCode) {
            if (Objects.isNull(secretCode)) throw new IllegalArgumentException("Invalid secret code");
            this.secretCodeInput = secretCode;
            this.codeSupplierPreference = USER_DEFINED;
            this.codeLength = secretCode.size();

            return this;
        }

        public Builder feedbackStrategy(FeedbackStrategy strategy) {
            if (Objects.isNull(strategy)) throw new IllegalArgumentException("Invalid feedback strategy preference");
            this.feedbackStrategy = strategy;

            return this;
        }

        public int getCodeLength() {
            return codeLength;
        }

        public int getNumColors() {
            return numColors;
        }

        public Game build() throws IOException {
            if (!codeSupplierPreference.equals(USER_DEFINED) && Objects.nonNull(secretCodeInput)) {
                throw new IllegalArgumentException("Cannot specify a code with selected strategy");
            }

            CodeSupplier codeSupplier = switch (this.codeSupplierPreference) {
                case RANDOM_ORG_API -> new ApiCodeSupplier(codeLength, numColors);
                case LOCAL_RANDOM -> new LocalRandomCodeSupplier(codeLength, numColors);
                case USER_DEFINED -> new UserDefinedCodeSupplier(Code.from(secretCodeInput, codeLength, numColors));
                default -> throw new IllegalArgumentException("Unimplemented CodeSupplier strategy");
            };

            try {
                this.secretCode = codeSupplier.get();
                return new Game(this);

            } catch (IOException e) {
                throw new IOException("Failed to supply code with " + codeSupplierPreference, e);
            }
        }
    }
}

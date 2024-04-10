package org.jmel.mastermind.core;

import org.jmel.mastermind.core.feedbackstrategy.Feedback;
import org.jmel.mastermind.core.feedbackstrategy.FeedbackStrategy;
import org.jmel.mastermind.core.feedbackstrategy.FeedbackStrategyImpl;
import org.jmel.mastermind.core.secretcodesupplier.ApiCodeSupplier;
import org.jmel.mastermind.core.secretcodesupplier.CodeSupplier;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Represents a Mastermind game.
 */
public class Game {
    private final int codeLength;
    private final int numColors;
    private final int maxAttempts;
    private final Code secretCode;
    private final List<Code> guessHistory = new ArrayList<>();
    private final FeedbackStrategy feedbackStrategy;

    /**
     * Game constructor that initializes a game with a given configuration.
     *
     * @param builder a Game.Builder object that contains a game's configuration
     */
    private Game(Builder builder) {
        this.codeLength = builder.codeLength;
        this.numColors = builder.numColors;
        this.maxAttempts = builder.maxAttempts;
        this.secretCode = builder.secretCode;
        this.feedbackStrategy = builder.feedbackStrategy;
    }

    /**
     * Processes a guess against the secret code. It adds a valid guess to the guess history. A guess to be processed is
     * considered invalid if the game is won or the maximum number of attempts is reached.
     *
     * @param guessInput list of integers representing a guess that conforms to the game's code length and number of
     *                   colors
     * @return Feedback object representing the result of the guess against the secret code based on a feedback strategy
     * @throws IllegalStateException if the game is already over
     */
    public Feedback processGuess(List<Integer> guessInput) {
        if (isGameWon())
            throw new IllegalStateException("Game already won!");

        if (movesCompleted() == maxAttempts)
            throw new IllegalStateException("Game is over. Cannot make more guesses.");

        Code guess = Code.from(guessInput, this.codeLength, this.numColors);
        guessHistory.add(guess);


        return feedbackStrategy.get(secretCode, guess);
    }

    /**
     * Returns the number of moves completed in the game. A move is considered completed if the input guess is valid and
     * is added to the guess history.
     *
     * @return an integer representing the number of moves completed in the game
     */
    public int movesCompleted() {
        if (guessHistory.size() > maxAttempts)
            throw new IllegalStateException("Game in illegal state -- more moves than attempts");

        return guessHistory.size();
    }

    /**
     * Returns true if the game is won. A game is won if the last guess in the guess history is equal to the secret
     * code.
     *
     * @return a boolean representing whether the game is won
     */
    public boolean isGameWon() {
        return !guessHistory.isEmpty() && Objects.equals(guessHistory.get(guessHistory.size() - 1), secretCode);
    }

    /**
     * Returns the length of the secret code. This can be used to validate the length of a guess input.
     *
     * @return an integer representing the length of the secret code
     */
    public int codeLength() {
        return this.codeLength;
    }

    /**
     * Returns the number of colors that can form the secret code. This can be used to validate the composition of a
     * guess input.
     *
     * @return an integer representing the number of colors that can form the secret code
     */
    public int numColors() {
        return this.numColors;
    }

    /**
     * Returns the number of attempts allowed in the game. This can be used to determine if a new guess can be
     * processed.
     *
     * @return an integer representing the maximum number of attempts allowed in the game
     */
    public int maxAttempts() {
        return this.maxAttempts;
    }

    /**
     * Builder for a Game object. Different game parameters are set to default values corresponding to a standard
     * Mastermind game. Custom values can be set using the builder's methods and will throw exceptions if invalid values
     * are provided.
     */
    public static class Builder {
        private int codeLength = 4;
        private int numColors = 8;
        private int maxAttempts = 10;
        private CodeSupplier codeSupplier;
        private FeedbackStrategy feedbackStrategy = FeedbackStrategyImpl.DEFAULT;
        private Code secretCode;

        public Builder codeLength(int codeLength) {
            if (codeLength < 1) throw new IllegalArgumentException("Invalid code length");
            this.codeLength = codeLength;

            return this;
        }

        public Builder numColors(int numColors) {
            if (numColors <= 1) throw new IllegalArgumentException("Invalid number of colors");
            this.numColors = numColors;

            return this;
        }

        public Builder maxAttempts(int maxAttempts) {
            if (maxAttempts < 1) throw new IllegalArgumentException("Invalid number of attempts");
            this.maxAttempts = maxAttempts;

            return this;
        }

        public Builder codeSupplier(CodeSupplier supplier) throws IOException {
            if (Objects.isNull(supplier)) throw new IllegalArgumentException("Invalid secret code supplier");
            this.codeSupplier = supplier;

            return this;
        }

        public Builder feedbackStrategy(FeedbackStrategy strategy) {
            if (Objects.isNull(strategy)) throw new IllegalArgumentException("Invalid feedback strategy preference");
            this.feedbackStrategy = strategy;

            return this;
        }

        public int codeLength() {
            return codeLength;
        }

        public int numColors() {
            return numColors;
        }

        /**
         * Builds a Game object with the specified configuration.
         *
         * @return a Game object with the specified configuration
         * @throws IllegalArgumentException if the client specified a user-defined code supplier but no code, or if the
         *                                  code supplier strategy is unimplemented
         * @throws IOException              if the code supplier fails to supply a code
         */
        public Game build() throws IOException {
            if (codeSupplier == null) {
                codeSupplier(ApiCodeSupplier.of(codeLength, numColors));
            }

            try {
                this.secretCode = Code.from(codeSupplier.get(), this.codeLength, this.numColors);
                return new Game(this);
            } catch (IOException e) {
                throw new IOException("Failed to get code from code supplier", e);
            }
        }
    }
}

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
 * <p>
 * A game is played by making guesses against a secret code. The game is won when a guess matches the secret code or if
 * the maximum number of attempts is reached. A game can be configured with a custom code length, number of colors,
 * maximum number of attempts, secret code supplier, and feedback strategy.
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
     * Processes a guess against the secret code.
     * <p>
     * A guess to be processed is considered invalid if the game is won, the maximum number of attempts was reached, or
     * a Code object could not be instantiated. If a guess is valid, it will be added to the guess history.
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
     * Returns the number of moves completed in the game.
     * <p>
     * A move is considered completed if the input guess is valid and is added to the guess history.
     *
     * @return an integer representing the number of moves completed in the game
     */
    public int movesCompleted() {
        if (guessHistory.size() > maxAttempts)
            throw new IllegalStateException("Game in illegal state -- more moves than attempts");

        return guessHistory.size();
    }

    /**
     * Returns true if the game is won.
     * <p>
     * A game is won if the last guess in the guess history is equal to the secret code.
     *
     * @return a boolean representing whether the game is won
     */
    public boolean isGameWon() {
        return !guessHistory.isEmpty() && Objects.equals(guessHistory.get(guessHistory.size() - 1), secretCode);
    }

    /**
     * Returns the length of the secret code.
     * <p>
     * This can be used to validate the length of a guess input.
     *
     * @return an integer representing the length of the secret code
     */
    public int codeLength() {
        return this.codeLength;
    }

    /**
     * Returns the number of colors that can form the secret code.
     * <p>
     * This can be used to validate the composition of a guess input.
     *
     * @return an integer representing the number of colors that can form the secret code
     */
    public int numColors() {
        return this.numColors;
    }

    /**
     * Returns the number of attempts allowed in the game.
     * <p>
     * This can be used to determine if a new guess can be processed.
     *
     * @return an integer representing the maximum number of attempts allowed in the game
     */
    public int maxAttempts() {
        return this.maxAttempts;
    }

    /**
     * Builder for a Game object.
     * <p>
     * Different game parameters are set to default values corresponding to a standard Mastermind game. Custom values
     * can be set using the builder's methods and will throw exceptions if invalid values are provided.
     */
    public static class Builder {
        private int codeLength = 4;
        private int numColors = 8;
        private int maxAttempts = 10;
        private CodeSupplier codeSupplier;
        private FeedbackStrategy feedbackStrategy = FeedbackStrategyImpl.DEFAULT;
        private Code secretCode;

        /**
         * Sets the length of codes to be used in a game instance.
         * <p>
         * The length of the secret code must be greater than 0.
         *
         * @param codeLength an integer representing the length of a code
         * @return the current builder object
         * @throws IllegalArgumentException if the code length is less than 1
         */
        public Builder codeLength(int codeLength) {
            if (codeLength < 1) throw new IllegalArgumentException("Invalid code length");
            this.codeLength = codeLength;

            return this;
        }

        /**
         * Sets the number of colors that can be used in the secret code.
         * <p>
         * The number of colors must be greater than 1.
         *
         * @param numColors an integer representing the number of colors that can be used in a code
         * @return the current builder object
         * @throws IllegalArgumentException if the number of colors is less than or equal to 1
         */
        public Builder numColors(int numColors) {
            if (numColors <= 1) throw new IllegalArgumentException("Invalid number of colors");
            this.numColors = numColors;

            return this;
        }

        /**
         * Sets the maximum number of attempts allowed in a game instance.
         * <p>
         * The maximum number of attempts must be greater than 0.
         *
         * @param maxAttempts an integer representing the maximum number of attempts allowed in a game
         * @return the current builder object
         * @throws IllegalArgumentException if the maximum number of attempts is less than 1
         */
        public Builder maxAttempts(int maxAttempts) {
            if (maxAttempts < 1) throw new IllegalArgumentException("Invalid number of attempts");
            this.maxAttempts = maxAttempts;

            return this;
        }

        /**
         * Sets the secret code supplier for a game instance.
         * <p>
         * If the code supplier is not set, a default code supplier will be used.
         * See {@link org.jmel.mastermind.core.secretcodesupplier.CodeSupplier}
         *
         * @param supplier a CodeSupplier object that supplies a secret code
         * @return the current builder object
         * @throws IllegalArgumentException if the code supplier is invalid
         * @throws IOException if the code supplier fails to supply a code
         */
        public Builder codeSupplier(CodeSupplier supplier) throws IOException {
            if (Objects.isNull(supplier)) throw new IllegalArgumentException("Invalid secret code supplier");
            this.codeSupplier = supplier;

            return this;
        }

        /**
         * Sets the feedback strategy for a game instance.
         * <p>
         * If the feedback strategy is not set, a default feedback strategy will be used.
         * See {@link org.jmel.mastermind.core.feedbackstrategy.FeedbackStrategyImpl}
         *
         * @param strategy a FeedbackStrategy object that determines the feedback for a guess
         * @return the current builder object
         * @throws IllegalArgumentException if the feedback strategy is invalid
         */

        public Builder feedbackStrategy(FeedbackStrategy strategy) {
            if (Objects.isNull(strategy)) throw new IllegalArgumentException("Invalid feedback strategy preference");
            this.feedbackStrategy = strategy;

            return this;
        }

        /**
         * Returns the current length of the secret code in the builder.
         *
         * @return the length of the secret code
         */
        public int codeLength() {
            return codeLength;
        }

        /**
         * Returns the current number of colors in the builder.
         *
         * @return the number of colors
         */
        public int numColors() {
            return numColors;
        }

        /**
         * Builds a Game object with the specified configuration.
         *
         * @return a Game object with the specified configuration
         * @throws IllegalArgumentException if the client specified an invalid game parameter or combination of
         *                                  parameters, if the code supplier or feedback strategies are unimplemented,
         *                                  or if the code supplier fails to supply a code
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

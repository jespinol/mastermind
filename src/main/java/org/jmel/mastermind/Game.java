package org.jmel.mastermind;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Game {

    private final Code secretCode;
    private final int maxAttempts;
    private final List<Code> guessHistory;

    public Game() {
        secretCode = new Code(generateSecretCode());
        maxAttempts = 2;
        guessHistory = new ArrayList<>();
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
        // TODO implement algorithm here
        return new Feedback(0, 0);
    }

    public boolean isGameOver() {
        boolean wonGame = !guessHistory.isEmpty() && Objects.equals(guessHistory.get(guessHistory.size() - 1), secretCode);
        return wonGame || getMovesLeft() == 0; // TODO ensure movesLeft can't be negative
    }

    private List<Integer> generateSecretCode() {
        return  List.of(1, 2, 3, 4); // TODO api call here
    }
}

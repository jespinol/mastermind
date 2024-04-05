package org.jmel.mastermind;

import org.jmel.mastermind.secret_code_suppliers.ApiCodeSupplier;
import org.jmel.mastermind.secret_code_suppliers.UserDefinedCodeSupplier;

import java.util.*;

public class Game {
    private final int codeLength = 4;
    private final int numColors = 8;
    private final int maxAttempts = 10;
    private final Code secretCode;
    private final List<Code> guessHistory = new ArrayList<>();

    private Game() {
        this.secretCode = new ApiCodeSupplier(codeLength, numColors).get();
    }

    private Game(List<Integer> secretCodeValue) {
        this.secretCode = new UserDefinedCodeSupplier(secretCodeValue).get();
    }

    // get instance with defaults
    public static Game createGame() {
        return new Game();
    }

    // get instance with custom values (any of them)
    public static Game createGameWithCode(List<Integer> secretCodeValue) {
        return new Game(secretCodeValue);
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

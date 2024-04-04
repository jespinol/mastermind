package org.jmel.mastermind;

import java.util.*;

public class Game {
    private final Code secretCode;
    private final int maxAttempts;
    private final List<Code> guessHistory;

    public Game() {
        secretCode = new Code(generateSecretCode());
        maxAttempts = 10;
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

    private List<Integer> generateSecretCode() {
        return List.of(1, 2, 3, 4); // TODO api call here
    }
}

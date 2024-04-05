package org.jmel.mastermind;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GameTests {
    private final static int NUM_COLORS = 8;
    private final static int CODE_LENGTH = 4;
    private final static int MAX_ATTEMPTS = 10;
    private final static List<Integer> correctGuess = List.of(1, 2, 3, 4);
    private final static List<Integer> incorrectGuess = List.of(1, 2, 3, 5);

    private Game game;

    @BeforeEach
    public void setUp() {
        game = new Game.Builder()
                .numColors(NUM_COLORS)
                .codeLength(CODE_LENGTH)
                .secretCode(correctGuess)
                .maxAttempts(MAX_ATTEMPTS)
                .build();
    }

    @DisplayName("Game can be played with correct guess. Game.isGameOver() returns true after exactly one guess.")
    @Test
    void winGameInOneMove() {
        game.processGuess(correctGuess);

        assertAll(
                () -> assertEquals(game.getMovesLeft(), MAX_ATTEMPTS - 1),
                () -> assertTrue(game.isGameOver())
        );
    }

    @DisplayName("Game can be played with wrong and correct guesses. Game is not over after an incorrect guess, but is over after a correct guess.")
    @Test
    void gameEndsCorrectly() {
        game.processGuess(incorrectGuess);
        assertFalse(game.isGameOver());

        game.processGuess(correctGuess);
        assertTrue(game.isGameOver());
    }

    @DisplayName("Player can make 10 incorrect guesses and no more.")
    @Test
    void loseGame() {
        for (int i = 0; i < MAX_ATTEMPTS; i++) {
            game.processGuess(incorrectGuess);
        }

        assertAll(
                () -> assertThrows(Exception.class, () -> game.processGuess(incorrectGuess)),
                () -> assertEquals(game.getMovesLeft(), 0),
                () -> assertTrue(game.isGameOver()));
    }

    @DisplayName("Feedback is computed correctly for a correct guess")
    @Test
    void feedbackFromCorrectGuess() {
        Feedback feedbackCorrect = game.processGuess(correctGuess);

        assertAll(
                () -> assertEquals(feedbackCorrect.getWellPlaced(), 4),
                () -> assertEquals(feedbackCorrect.getMisplaced(), 0));
    }

    @DisplayName("Feedback is computed correctly for an incorrect guess")
    @Test
    void feedbackFromIncorrectGuess() {
        Feedback feedbackIncorrect = game.processGuess(incorrectGuess);

        assertAll(
                () -> assertEquals(feedbackIncorrect.getWellPlaced(), 3),
                () -> assertEquals(feedbackIncorrect.getMisplaced(), 0));
    }
}

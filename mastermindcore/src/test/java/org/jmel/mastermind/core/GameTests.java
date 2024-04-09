package org.jmel.mastermind.core;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
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
    public void setUp() throws IOException {
        game = new Game.Builder()
                .numColors(NUM_COLORS)
                .codeLength(CODE_LENGTH)
                .secretCode(correctGuess)
                .maxAttempts(MAX_ATTEMPTS)
                .build();
    }

    @DisplayName("Game can be played with correct guess. Game.isGameWon() returns true after exactly one guess.")
    @Test
    void winGameInOneMove() {
        game.processGuess(correctGuess);

        assertAll(
                () -> assertEquals(game.movesCompleted(), 1),
                () -> assertTrue(game.isGameWon())
        );
    }

    @DisplayName("Game can be played with wrong and correct guesses. Game is not won after an incorrect guess, but is won after a correct guess.")
    @Test
    void gameEndsCorrectly() {
        game.processGuess(incorrectGuess);
        assertFalse(game.isGameWon());

        game.processGuess(correctGuess);
        assertTrue(game.isGameWon());
    }

    @DisplayName("Player can make 10 incorrect guesses and no more.")
    @Test
    void loseGame() {
        for (int i = 0; i < game.maxAttempts(); i++) {
            game.processGuess(incorrectGuess);
        }

        assertAll(
                () -> assertThrows(IllegalStateException.class, () -> game.processGuess(incorrectGuess)),
                () -> assertEquals(game.movesCompleted(), game.maxAttempts()),
                () -> assertFalse(game.isGameWon()));
    }
}

package org.jmel.mastermind;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class CodeTests {
    // Assumes that the Code constructor expects a list of integers of length 4
    private final Game game = new Game();

    @DisplayName("A too-short guess throws an exception.")
    @Test
    void codeWithShortGuess() {
        List<Integer> shortGuess = Collections.nCopies(3, 1);

        assertThrows(IllegalArgumentException.class, () -> game.processGuess(shortGuess));
    }

    @DisplayName("A too-long guess throws an exception.")
    @Test
    void codeWithLongGuess() {
        List<Integer> longGuess = Collections.nCopies(5, 1);

        assertThrows(IllegalArgumentException.class, () -> game.processGuess(longGuess));
    }

    @DisplayName("A null guess throws an exception.")
    @Test
    void codeWithNullGuess() {

        assertThrows(IllegalArgumentException.class, () -> game.processGuess(null));
    }

    @DisplayName("An empty guess throws an exception.")
    @Test
    void codeWithEmptyGuess() {

        assertThrows(IllegalArgumentException.class, () -> game.processGuess(Collections.emptyList()));
    }
}

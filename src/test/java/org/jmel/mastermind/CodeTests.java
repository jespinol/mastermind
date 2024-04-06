package org.jmel.mastermind;

import org.jmel.mastermind.secret_code_suppliers.ApiCodeSupplier;
import org.jmel.mastermind.secret_code_suppliers.CodeSupplier;
import org.jmel.mastermind.secret_code_suppliers.LocalRandomCodeSupplier;
import org.jmel.mastermind.secret_code_suppliers.UserDefinedCodeSupplier;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CodeTests {
    private final static Game game = new Game.Builder().build();

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

    @DisplayName("API code supplier successfully generates a Code object.")
    @Test
    void getCodeFromApi() { // TODO: test with a mock response instead of actually sending to random.org
        CodeSupplier apiCodeSupplier = new ApiCodeSupplier(4, 8);

        assertTrue(apiCodeSupplier.get().isPresent());
    }

    @DisplayName("User defined code supplier successfully generates a Code object.")
    @Test
    void getCodeFromUserDefined() {
        List<Integer> codeValue = List.of(1, 2, 3, 4);
        Code secretCode = Code.from(codeValue, 4, 8);
        CodeSupplier userDefinedCodeSupplier = new UserDefinedCodeSupplier(secretCode);

        assertTrue(userDefinedCodeSupplier.get().isPresent());
    }

    @DisplayName("Locally random code supplier successfully returns a Code object.")
    @Test
    void getCodeFromLocalRandom() {
        CodeSupplier localRandomCodeSupplier = new LocalRandomCodeSupplier(4, 8);

        assertTrue(localRandomCodeSupplier.get().isPresent());
    }
}

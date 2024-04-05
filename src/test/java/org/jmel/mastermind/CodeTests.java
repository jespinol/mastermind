package org.jmel.mastermind;

import org.jmel.mastermind.secret_code_suppliers.ApiCodeSupplier;
import org.jmel.mastermind.secret_code_suppliers.LocalRandomCodeSupplier;
import org.jmel.mastermind.secret_code_suppliers.UserDefinedCodeSupplier;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class CodeTests {
    private final Game game = Game.createGame();

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
    void getCodeFromApi() { // TODO: test with a mock response
        Supplier<Code> apiCodeSupplier = new ApiCodeSupplier(4, 8);
        apiCodeSupplier.get();
    }

    @DisplayName("User defined code supplier successfully generates a Code object.")
    @Test
    void getCodeFromUserDefined() {
        List<Integer> codeValue = List.of(1, 2, 3, 4);
        Supplier<Code> userDefinedCodeSupplier = new UserDefinedCodeSupplier(codeValue);
        userDefinedCodeSupplier.get();
    }

    @DisplayName("Locally random code supplier successfully returns a Code object.")
    @Test
    void getCodeFromLocalRandom() {
        Supplier<Code> localRandomCodeSupplier = new LocalRandomCodeSupplier(4, 8);
        localRandomCodeSupplier.get();
    }
}

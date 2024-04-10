package org.jmel.mastermind.core;

import org.jmel.mastermind.core.secretcodesupplier.ApiCodeSupplier;
import org.jmel.mastermind.core.secretcodesupplier.CodeSupplier;
import org.jmel.mastermind.core.secretcodesupplier.LocalRandomCodeSupplier;
import org.jmel.mastermind.core.secretcodesupplier.UserDefinedCodeSupplier;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CodeTests {
    @Nested
    public class GuessTests {
        private final static Game game;

        static {
            try {
                game = new Game.Builder()
                        .codeSupplier(LocalRandomCodeSupplier.of(4, 8))
                        .build();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        @DisplayName("A too-short guess throws an exception.")
        @Test
        void guessWithTooShortCodeFails() {
            List<Integer> shortGuess = Collections.nCopies(3, 1);

            assertThrows(IllegalArgumentException.class, () -> game.processGuess(shortGuess));
        }

        @DisplayName("A too-long guess throws an exception.")
        @Test
        void guessWithTooLongCodeFails() {
            List<Integer> longGuess = Collections.nCopies(5, 1);

            assertThrows(IllegalArgumentException.class, () -> game.processGuess(longGuess));
        }

        @DisplayName("A null guess throws an exception.")
        @Test
        void guessWithNullCodeFails() {

            assertThrows(IllegalArgumentException.class, () -> game.processGuess(null));
        }

        @DisplayName("An empty guess throws an exception.")
        @Test
        void guessWithEmptyCodeFails() {

            assertThrows(IllegalArgumentException.class, () -> game.processGuess(Collections.emptyList()));
        }

        @Test
        void clientCannotAddToCode() {
            List<Integer> codeValue = List.of(1, 2, 3, 4);
            Code secretCode = Code.from(codeValue, 4, 8);

            assertThrows(UnsupportedOperationException.class, () -> secretCode.value().add(5));
        }

        @Test
        void clientCannotModifyDigitInCode() {
            List<Integer> codeValue = new ArrayList<>(List.of(1, 2, 3, 4));
            Code secretCode = Code.from(codeValue, 4, 8);

            assertThrows(UnsupportedOperationException.class, () -> secretCode.value().set(0, 5));
        }

        @Test
        void modifyingOriginalListDoesntChangeCode() {
            List<Integer> codeValue = new ArrayList<>(List.of(1, 2, 3, 4)); // codeValue is mutable
            Code secretCode = Code.from(codeValue, 4, 8);

            List<Integer> oldValue = secretCode.value();
            codeValue.set(0, 5);
            List<Integer> newValue = secretCode.value();

            assertEquals(newValue, oldValue);
        }
    }

    @Nested
    public class CodeSupplierTests {
        @DisplayName("User defined code supplier successfully generates a Code object.")
        @Test
        void getCodeFromUserDefined() throws IOException {
            List<Integer> codeValue = List.of(1, 2, 3, 4);
//            Code secretCode = Code.from(codeValue, 4, 8);
            CodeSupplier userDefinedCodeSupplier = UserDefinedCodeSupplier.of(codeValue);

            Code.from(userDefinedCodeSupplier.get(), 4, 8);
        }

        @DisplayName("Locally random code supplier successfully returns a Code object.")
        @Test
        void getCodeFromLocalRandom() throws IOException {
            CodeSupplier localRandomCodeSupplier = LocalRandomCodeSupplier.of(4, 8);

            localRandomCodeSupplier.get();
        }

        @DisplayName("API code supplier successfully generates a Code object.")
        @Test
        void getCodeFromApi() throws IOException { // TODO: test with a mock response instead of actually sending to random.org
            CodeSupplier apiCodeSupplier = ApiCodeSupplier.of(4, 8);

            apiCodeSupplier.get();
        }
    }
}

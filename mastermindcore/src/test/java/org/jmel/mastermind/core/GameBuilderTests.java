package org.jmel.mastermind.core;

import org.jmel.mastermind.core.secretcodesupplier.CodeSupplier;
import org.jmel.mastermind.core.secretcodesupplier.UserDefinedCodeSupplier;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class GameBuilderTests {
    private static final CodeSupplier userDefinedSupplier = UserDefinedCodeSupplier.of(List.of(1, 2, 3, 4));

    @Nested
    public class CorrectBuilderParamTests {
        private Game.Builder gameBuilder;

        @BeforeEach
        void setUp() {
            gameBuilder = new Game.Builder();
        }

        @AfterEach
        void build() throws IOException {
            gameBuilder.build();
        }

        @Test
        void canBuildAGameWithDefaultValues() {
        }

        @Test
        void canBuildAGameWithCustomValues() {
            gameBuilder
                    .numColors(9)
                    .codeLength(5)
                    .maxAttempts(11);
        }

        @Test
        void canBuildAGameWithSecretCodeOnly() throws IOException {
            gameBuilder
                    .codeSupplier(userDefinedSupplier);
        }
    }

    @Nested
    public class IncorrectBuilderParamTests {
        private Game.Builder gameBuilder;

        @BeforeEach
        void setUp() {
            gameBuilder = new Game.Builder();
        }

        @Test
        void buildGameWithInvalidNumColors() {
            assertThrows(IllegalArgumentException.class, () -> gameBuilder
                    .numColors(1)
                    .build());
        }

        @Test
        void buildGameWithInvalidCodeLength() {
            assertThrows(IllegalArgumentException.class, () -> gameBuilder
                    .codeLength(0)
                    .build());
        }

        @Test
        void buildGameWithInvalidMaxAttempts() {
            assertThrows(IllegalArgumentException.class, () -> gameBuilder
                    .maxAttempts(0)
                    .build());
        }

        @Test
        void buildGameWithNullCodeSupplier() {
            assertThrows(IllegalArgumentException.class, () -> gameBuilder
                    .codeSupplier(null)
                    .build());
        }

        @Test
        void buildGameWithTooHighColorInCode() {
            CodeSupplier supplier = UserDefinedCodeSupplier.of(List.of(2, 2, 2, 2));
            assertThrows(IllegalArgumentException.class, () -> gameBuilder
                    .codeSupplier(supplier)
                    .numColors(2)
                    .build());
        }

        @Test
        void buildGameWithTooLowColorInCode() {
            CodeSupplier supplier = UserDefinedCodeSupplier.of(List.of(-1, -1, -1, -1));
            assertThrows(IllegalArgumentException.class, () -> gameBuilder
                    .codeSupplier(supplier)
                    .numColors(2)
                    .build());
        }

        @Test
        void buildGameWithTooShortCode() {
            CodeSupplier supplier = UserDefinedCodeSupplier.of(List.of(1));
            assertThrows(IllegalArgumentException.class, () -> gameBuilder
                    .codeSupplier(supplier)
                    .codeLength(2)
                    .build());
        }

        @Test
        void buildGameWithTooLongCode() {
            CodeSupplier supplier = UserDefinedCodeSupplier.of(List.of(1, 2, 3, 4, 5));
            assertThrows(IllegalArgumentException.class, () -> gameBuilder
                    .codeSupplier(supplier)
                    .codeLength(4)
                    .build());
        }

        @Test
        void buildGameWithNullFeedbackStrategy() {
            assertThrows(IllegalArgumentException.class, () -> gameBuilder
                    .feedbackStrategy(null)
                    .build());
        }
    }
}

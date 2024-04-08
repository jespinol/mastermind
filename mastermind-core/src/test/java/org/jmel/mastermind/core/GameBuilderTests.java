package org.jmel.mastermind.core;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static org.jmel.mastermind.core.secret_code_suppliers.CodeGenerationPreference.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class GameBuilderTests {

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
        void canBuildAGameWithCustomValues() {
            gameBuilder
                    .numColors(9)
                    .codeLength(5)
                    .maxAttempts(11);
        }

        @Test
        void canBuildAGameWithDefaultValues() {
        }

        @Test
        void canBuildAGameWithSecretCodeOnly() {
            gameBuilder
                    .secretCode(List.of(1, 1, 1, 1));
        }

        @Test
        void canBuildAGameWithLongerSecretCodeOnly() {
            gameBuilder
                    .secretCode(Collections.nCopies(5, 1));
        }

        @Test
        void canOverrideCodeLength() {
            gameBuilder
                    .codeLength(5)
                    .secretCode(List.of(1, 1, 1, 1));
        }

        @Test
        void canOverrideCodeSupplierByProvidingCode() {
            gameBuilder
                    .codeGenerationPreference(LOCAL_RANDOM)
                    .secretCode(List.of(1, 1, 1, 1));
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
        void buildGameWithUserDefinedStrategyButNoSecretCode() {
            assertThrows(IllegalArgumentException.class, () -> gameBuilder
                    .codeGenerationPreference(USER_DEFINED)
                    .build());
        }

        @Test
        void buildGameWithRandomStrategyAndSecretCode() {
            assertThrows(IllegalArgumentException.class, () -> gameBuilder
                    .secretCode(List.of(1, 1, 1, 1))
                    .codeGenerationPreference(RANDOM_ORG_API)
                    .build());
        }

        @Test
        void buildGameWithInvalidNumColors() {
            assertThrows(IllegalArgumentException.class, () -> gameBuilder
                    .numColors(0)
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
        void buildGameWithInvalidStrategy() {
            assertThrows(IllegalArgumentException.class, () -> gameBuilder
                    .codeGenerationPreference(null)
                    .build());
        }

        @Test
        void buildGameWithTooHighColorInCode() {
            assertThrows(IllegalArgumentException.class, () -> gameBuilder
                    .secretCode(List.of(1))
                    .numColors(1)
                    .build());
        }

        @Test
        void buildGameWithTooLowColorInCode() {
            assertThrows(IllegalArgumentException.class, () -> gameBuilder
                    .secretCode(List.of(-1))
                    .numColors(1)
                    .build());
        }

        @Test
        void buildGameWithTooShortCode() {
            assertThrows(IllegalArgumentException.class, () -> gameBuilder
                    .secretCode(List.of(0))
                    .codeLength(2)
                    .build());
        }

        @Test
        void buildGameWithTooLongCode() {
            assertThrows(IllegalArgumentException.class, () -> gameBuilder
                    .secretCode(List.of(1, 2, 3, 4, 5))
                    .codeLength(4)
                    .build());
        }

        @Test
        void buildGameWithInvalidFeedbackStrategy() {
            assertThrows(IllegalArgumentException.class, () -> gameBuilder
                    .feedbackStrategy(null)
                    .build());
        }
    }
}

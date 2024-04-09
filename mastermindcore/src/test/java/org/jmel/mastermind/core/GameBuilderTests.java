package org.jmel.mastermind.core;

import org.junit.jupiter.api.*;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static org.jmel.mastermind.core.secretcodesupplier.CodeSupplierPreference.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
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
                    .codeSupplierPreference(LOCAL_RANDOM)
                    .secretCode(List.of(1, 1, 1, 1));
            assertEquals(USER_DEFINED, gameBuilder.codeSupplierPreference());
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
                    .codeSupplierPreference(USER_DEFINED)
                    .build());
        }

        @Test
        void buildGameWithRandomStrategyAndSecretCode() {
            assertThrows(IllegalArgumentException.class, () -> gameBuilder
                    .secretCode(List.of(1, 1, 1, 1))
                    .codeSupplierPreference(RANDOM_ORG_API)
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
        void buildGameWithNullStrategy() {
            assertThrows(IllegalArgumentException.class, () -> gameBuilder
                    .codeSupplierPreference(null)
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
        @Disabled
        void buildGameWithOneColor() throws IOException {
            gameBuilder
                    .numColors(1)
                    .build(); // TODO: This throws an exception because Random.org API is used and the user specified only one color. Could check in builder
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
        void buildGameWithNullFeedbackStrategy() {
            assertThrows(IllegalArgumentException.class, () -> gameBuilder
                    .feedbackStrategy(null)
                    .build());
        }
    }
}

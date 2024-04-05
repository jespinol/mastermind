package org.jmel.mastermind;

import org.jmel.mastermind.enums.CodeSecretGenerationStrategy;
import org.junit.jupiter.api.*;

import java.util.Collections;
import java.util.List;

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
        void build() {
            gameBuilder.build();
        }

        @Test
        @Disabled("This test is disabled because code lengths other than 4 is not implemented yet.")
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
        @Disabled("This test is disabled because code lengths other than 4 is not implemented yet.")
        void canBuildAGameWithLongerSecretCodeOnly() {
            gameBuilder
                    .secretCode(Collections.nCopies(5, 1));
        }

        @Test
        @Disabled
        void canOverrideCodeLength() {
            gameBuilder
                    .codeLength(5)
                    .secretCode(List.of(1, 1, 1, 1));
        }

        @Test
        void canOverrideCodeSupplierByProvidingCode() {
            gameBuilder
                    .strategy(CodeSecretGenerationStrategy.LOCAL_RANDOM)
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
            assertThrows(Exception.class, () -> gameBuilder
                    .strategy(CodeSecretGenerationStrategy.USER_DEFINED)
                    .build());
        }

        @Test
        void buildGameWithRandomStrategyAndSecretCode() {
            assertThrows(Exception.class, () -> gameBuilder
                    .secretCode(List.of(1, 1, 1, 1))
                    .strategy(CodeSecretGenerationStrategy.RANDOM_ORG_API)
                    .build());
        }

        @Test
        void buildGameWithInvalidNumColors() {
            assertThrows(Exception.class, () -> gameBuilder
                    .numColors(0)
                    .build());
        }

        @Test
        void buildGameWithInvalidCodeLength() {
            assertThrows(Exception.class, () -> gameBuilder
                    .codeLength(0)
                    .build());
        }

        @Test
        void buildGameWithInvalidMaxAttempts() {
            assertThrows(Exception.class, () -> gameBuilder
                    .maxAttempts(0)
                    .build());
        }

        @Test
        void buildGameWithInvalidStrategy() {
            assertThrows(Exception.class, () -> gameBuilder
                    .strategy(null)
                    .build());
        }

        @Test
        void buildGameWithInvalidSecretCodeColors() {
            assertThrows(Exception.class, () -> gameBuilder
                    .secretCode(List.of(0, 0, 0, 1))
                    .numColors(1)
                    .build());
        }
    }
}

package org.jmel.mastermind.cli;

import org.jmel.mastermind.core.Game;
import org.jmel.mastermind.core.feedbackstrategy.Feedback;
import org.jmel.mastermind.core.feedbackstrategy.FeedbackStrategy;
import org.jmel.mastermind.core.secretcodesupplier.ApiCodeSupplier;
import org.jmel.mastermind.core.secretcodesupplier.CodeSupplierPreference;
import org.jmel.mastermind.core.secretcodesupplier.LocalRandomCodeSupplier;
import org.jmel.mastermind.core.secretcodesupplier.UserDefinedCodeSupplier;

import java.io.IOException;
import java.util.List;

import static org.jmel.mastermind.cli.CliUtils.*;
import static org.jmel.mastermind.cli.Menu.*;
import static org.jmel.mastermind.core.secretcodesupplier.CodeSupplierPreference.*;

public class MastermindCliApplication {
    private static Menu currentMenu = MAIN_MENU;
    private static CodeSupplierPreference currentPreference = RANDOM_ORG_API;
    private static final Game.Builder builder = new Game.Builder();

    public static void main(String[] args) throws IOException {
        Game game = setUpGame();

        play(game);
    }

    public static Game setUpGame() throws IOException {
        while (currentMenu != EXIT && currentMenu != PLAY) {
            if (currentMenu != GAME_SETUP_FAILED) clearScreen();
            System.out.println(currentMenu);
            currentMenu = getNextMenu();
        }

        try {
            if (currentPreference == RANDOM_ORG_API) {
                builder.codeSupplier(ApiCodeSupplier.of(builder.codeLength(), builder.numColors()));
            } else if (currentPreference == LOCAL_RANDOM) {
                builder.codeSupplier(LocalRandomCodeSupplier.of(builder.codeLength(), builder.numColors()));
            }

            if (currentMenu == EXIT) System.exit(0);
            return builder.build();
        } catch (IOException | IllegalArgumentException e) {
            System.out.println("Error creating game: " + e.getMessage());
            currentMenu = GAME_SETUP_FAILED;
            return setUpGame();
        }
    }

    private static Menu getNextMenu() throws IOException {
        if (navigationalMenu.containsKey(currentMenu)) {
            int choice = getIntegerChoiceFromSet(navigationalMenu.get(currentMenu).keySet());
            return navigationalMenu.get(currentMenu).get(choice);
        }

        if (!inputCollectionMenu.containsKey(currentMenu)) {
            throw new IllegalStateException("Menu not found");
        }

        Menu nextMenu = inputCollectionMenu.get(currentMenu);
        switch (currentMenu) {
            case SETTINGS_MAX_ATTEMPTS -> builder.maxAttempts(getIntegerMatchingCondition(i -> i > 0));
            case SETTINGS_CODE_LENGTH -> builder.codeLength(getIntegerMatchingCondition(i -> i > 0));
            case SETTINGS_NUM_COLORS -> builder.numColors(getIntegerMatchingCondition(i -> i > 1));
            case SETTINGS_FEEDBACK_PREFERENCE -> {
                FeedbackStrategy selected = feedbackStrategyMap.get(getIntegerChoiceFromSet(feedbackStrategyMap.keySet()));
                builder.feedbackStrategy(selected);
            }
            case SETTINGS_CODE_GENERATION_PREFERENCE -> {
                currentPreference = codeGenerationMap.get(getIntegerChoiceFromSet(codeGenerationMap.keySet()));
                if (currentPreference == USER_DEFINED) nextMenu = SETTINGS_CODE_USER_DEFINED;
            }
            case SETTINGS_CODE_USER_DEFINED -> {
                List<Integer> secretCode = CliUtils.getCode();
                builder.codeSupplier(UserDefinedCodeSupplier.of(secretCode));
            }
            default -> throw new IllegalStateException("Unexpected value: " + currentMenu);
        }

        return nextMenu;
    }

    private static void play(Game game) {
        System.out.printf("""
                Start!
                The secret code is %d digits long from 0 to %d
                Enter your guess in one line, space separated
                """.formatted(game.codeLength(), game.numColors() - 1));

        while (!game.isGameWon() && (game.movesCompleted() < game.maxAttempts())) {
            System.out.printf("%nRound %d of %d %n".formatted(game.movesCompleted() + 1, game.maxAttempts()));

            List<Integer> guess = CliUtils.getCode(game.codeLength(), game.numColors());
            Feedback feedback = game.processGuess(guess);
            System.out.println(feedback);
        }

        printEndOfGameMessage(game);
    }

    private static void printEndOfGameMessage(Game game) {
        if (game.isGameWon()) {
            System.out.println("\nCongratulations! You won!\n");
        } else {
            System.out.println("\nGame over! Try again\n");
        }
    }
}

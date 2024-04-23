package org.jmel.mastermind.cli;

import org.jmel.mastermind.core.Game;
import org.jmel.mastermind.core.feedbackstrategy.Feedback;
import org.jmel.mastermind.core.feedbackstrategy.FeedbackStrategy;
import org.jmel.mastermind.core.secretcodesupplier.ApiCodeSupplier;
import org.jmel.mastermind.core.secretcodesupplier.LocalRandomCodeSupplier;
import org.jmel.mastermind.core.secretcodesupplier.UserDefinedCodeSupplier;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.jmel.mastermind.cli.CliUtils.*;
import static org.jmel.mastermind.cli.CodeSupplierPreference.*;
import static org.jmel.mastermind.cli.Menu.*;

public class MastermindCliApplication {
    private static Menu currentMenu = MAIN_MENU;
    private static CodeSupplierPreference currentPreference = RANDOM_ORG_API;
    private static final Game.Builder builder = new Game.Builder();
    private static final List<Game> games = new ArrayList<>();
    private static int gameNum = 1;

    public static void main(String[] args) throws IOException {
        setUpGame();

        if (gameNum == 1) {
            play(games.get(0));
        } else {
            playMultiplayer();
        }
    }

    public static void setUpGame() throws IOException {
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

            for (int i = 0; i < gameNum; i++) games.add(builder.build());
        } catch (IOException | IllegalArgumentException e) {
            System.out.println("Error creating game: " + e.getMessage());
            currentMenu = GAME_SETUP_FAILED;
            setUpGame();
        }
    }

    private static Menu getNextMenu() {
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
            case MULTIPLAYER_SETUP -> gameNum = getIntegerMatchingCondition(i -> i > 1);
            default -> throw new IllegalStateException("Unexpected value: " + currentMenu);
        }

        return nextMenu;
    }

    private static void playMultiplayer() {
        System.out.printf("""
                
                Starting game with %d players!
                The secret code is %d digits long from 0 to %d
                Enter your guess in one line, space separated
                """.formatted(games.size(), games.get(0).codeLength(), games.get(0).numColors() - 1));

        int round = 1;
        while (games.stream().anyMatch(game -> !game.isGameWon() && game.movesCompleted() < game.maxAttempts())) {
            System.out.printf("%n---------------------------------------------%n");
            System.out.printf("Round %d".formatted(round));
            System.out.printf("%n---------------------------------------------%n");
            for (int i = 0; i < games.size(); i++) {
                System.out.printf("%nPlayer %d%n", i+1);

                Game game = games.get(i);
                if (game.isGameWon() ) {
                    System.out.println("Already won!");
                    continue;
                }

                List<Integer> guess = CliUtils.getCode(game.codeLength(), game.numColors());
                Feedback feedback = game.processGuess(guess);
                System.out.println(feedback);
            }
            round++;
        }

        printEndOfMultiplayerGameMessage();
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

        System.out.println();
        printEndOfGameMessage(game);
    }

    private static void printEndOfMultiplayerGameMessage() {
        System.out.printf("%n---------------------------------------------%n");
        System.out.print("Final results");
        System.out.printf("%n---------------------------------------------%n");
        for (int i = 0; i < games.size(); i++) {
            System.out.printf("Player %d: ".formatted(i + 1));
            printEndOfGameMessage(games.get(i));
        }
    }

    private static void printEndOfGameMessage(Game game) {
        if (game.isGameWon()) {
            System.out.println("Congratulations! You won!");
        } else {
            System.out.println("Game over! Try again");
        }
    }
}

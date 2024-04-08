package org.jmel.mastermind.cli;

import org.jmel.mastermind.core.Feedback;
import org.jmel.mastermind.core.Game;
import org.jmel.mastermind.core.secret_code_suppliers.CodeGenerationPreference;

import java.io.IOException;
import java.util.List;

import static org.jmel.mastermind.cli.CliUtils.*;
import static org.jmel.mastermind.cli.Menu.*;
import static org.jmel.mastermind.core.secret_code_suppliers.CodeGenerationPreference.USER_DEFINED;

public class MastermindCliApplication {
    private static Menu currentMenu = MAIN_MENU;
    private static final Game.Builder builder = new Game.Builder();

    public static void main(String[] args) throws IOException {
        while (currentMenu != EXIT && currentMenu != PLAY) {
            System.out.println(currentMenu);
            currentMenu = getNextMenu();
        }

        System.out.println(currentMenu);
        if (currentMenu == EXIT) System.exit(0);

        Game game = builder.build();
        play(game);
    }

    private static void play(Game game) {
        System.out.printf("""
                The secret code is %d digits long from 0 to %d
                Enter your guess in one line, space separated
                """, game.codeLength(), game.numColors() - 1);

        while (!game.isGameWon() && (game.movesCompleted() < game.maxAttempts())) {
            System.out.printf("\nRound %d%n", game.movesCompleted() + 1);
            List<Integer> guess = getNextCode(game.codeLength(), game.numColors());
            Feedback feedback = game.processGuess(guess);
            System.out.println(feedback);
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
            case SETTINGS_MAX_ATTEMPTS -> builder.maxAttempts(getIntegerValue());
            case SETTINGS_CODE_LENGTH -> builder.codeLength(getIntegerValue());
            case SETTINGS_NUM_COLORS -> builder.numColors(getIntegerValue());
            case SETTINGS_FEEDBACK_PREFERENCE ->
                    throw new UnsupportedOperationException("Feedback strategy not implemented yet"); // TODO: implement in Game builder
            case SETTINGS_CODE_GENERATION_PREFERENCE -> {
                CodeGenerationPreference preference = codeGenerationMap.get(getIntegerChoiceFromSet(codeGenerationMap.keySet()));
                builder.codeGenerationPreference(preference);
                if (preference == USER_DEFINED) nextMenu = SETTINGS_CODE_USER_DEFINED;
            }
            case SETTINGS_CODE_USER_DEFINED -> {
                List<Integer> secretCode = getNextCode(builder.getCodeLength(), builder.getNumColors());
                builder.secretCode(secretCode);
            }
            default -> throw new IllegalStateException("Unexpected value: " + currentMenu);
        }

        return nextMenu;
    }
}

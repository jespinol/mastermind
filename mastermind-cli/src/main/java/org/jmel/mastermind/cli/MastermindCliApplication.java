package org.jmel.mastermind.cli;


import org.jmel.mastermind.core.Feedback;
import org.jmel.mastermind.core.Game;
import org.jmel.mastermind.core.secret_code_suppliers.CodeGenerationPreference;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Stream;

import static org.jmel.mastermind.cli.Menu.*;
import static org.jmel.mastermind.core.secret_code_suppliers.CodeGenerationPreference.*;

public class MastermindCliApplication {
    private static final Scanner scanner = new Scanner(System.in);
    private static Menu currentMenu = MAIN_MENU;
    private static Game.Builder builder;
    private static int codeLength;
    private static int numColors;
    public static void main(String[] args) throws IOException {
        builder = new Game.Builder();

        while (currentMenu != EXIT && currentMenu != PLAY) {
            renderMenu(currentMenu);
            currentMenu = getNextMenu();
        }

        if (currentMenu == EXIT) {
            System.exit(0);
        }

        codeLength = builder.getCodeLength();
        numColors = builder.getNumColors();
        Game game = builder.build();

        System.out.printf("""
                The secret code is %d digits long from 0 to %d
                Enter your guess in one line, space separated
                """, codeLength, numColors - 1);

        while (!game.isGameWon() && game.getMovesCompleted() < builder.getMaxAttempts()) {
            System.out.printf("\nRound %d%n", game.getMovesCompleted() + 1);
            Feedback feedback = game.processGuess(getNextCode());
            System.out.println(feedback);
        }
    }

    private static Menu getNextMenu() {
        /* if (navigationalMenu)
            int choice = getMenuChoice(currentMenu.numChoices()); // getMenuChoice uses containsKey?
            return navigationalMenuMap.get(currentMenu).get(choice);

            else (if inputCollectionMenu)
                collect the input (type varies), possibly translate input to Code/Feddback enum, perform the action
                    this would have a big switch switching on the current Menu
        */

        int choice = getMenuChoice(currentMenu.numChoices());
        Menu nextMenu = menuMap.get(currentMenu).get(choice);

        if (isIntegerEntryMenu(nextMenu)) {
            renderMenu(nextMenu);
            int value = getPositiveValue();
            switch (choice) {
                case 1 -> builder.maxAttempts(value);
                case 2 -> builder.codeLength(value);
                case 3 -> builder.numColors(value);
            }

            return SETTINGS_MAIN;
        } else if (nextMenu == SETTINGS_CODE) {
            CodeGenerationPreference preference = codeGenerationMap.get(getMenuChoice(nextMenu.numChoices()));
            builder.codeGenerationPreference(preference);
            if (preference == USER_DEFINED) {
                List<Integer> secretCode = getNextCode();
                builder.secretCode(secretCode);
            }

            return SETTINGS_MAIN;
        } else if (nextMenu == SETTINGS_FEEDBACK) {
//            FeedbackStrategyPreference preference = feedbackStrategyMap.get(getMenuChoice(nextMenu.numChoices()));
//            builder.feedbackStrategyPreference(preference);
            System.out.println("Setting feedback strategy not implemented yet.");

            return SETTINGS_MAIN;
        }

        return nextMenu;
    }

    private static void renderMenu(Menu menu) {
        System.out.println(menu.view());
    }

    private static int getMenuChoice(int maxChoice) {
        int choice = -1;
        do {
            System.out.print("> ");
            String trimmedLine = scanner.nextLine().trim();
            try {
                choice = Integer.parseInt(trimmedLine);
            } catch (NumberFormatException e) {
                System.out.println("Invalid selection. Please select one from above.");
            }
        } while (choice < 0 || choice >= maxChoice);

        return choice;
    }

    private static int getPositiveValue() {
        int value = 0;
        do {
            System.out.print("> ");
            String trimmedLine = scanner.nextLine().trim();
            try {
                value = Integer.parseInt(trimmedLine);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number greater than 0.");
            }
        } while (value < 1);

        return value;
    }

    private static List<Integer> getNextCode() {
        do {
            System.out.print("> ");
            String trimmedLine = scanner.nextLine().trim();
            try {
                List<Integer> possibleCode = Stream.of(trimmedLine.split("\\s+")).map(Integer::parseInt).toList();
                if (isCodeValid(possibleCode)) {
                    return possibleCode;
                } else {
                    System.out.printf("Invalid code. Please enter %d digits from 0 to %d, space separated%n", codeLength, numColors - 1);
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid code. Please enter a numeric guess.");
            }
        } while (true);
    }

    private static boolean isCodeValid(List<Integer> possibleCode) {
        return possibleCode.size() == codeLength && possibleCode.stream().allMatch(i -> i >= 0 && i < numColors);
    }
}

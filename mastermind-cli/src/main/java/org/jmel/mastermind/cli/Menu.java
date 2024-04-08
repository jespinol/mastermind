package org.jmel.mastermind.cli;

import org.jmel.mastermind.core.feedback_strategy.FeedbackStrategyPreference;
import org.jmel.mastermind.core.secret_code_suppliers.CodeGenerationPreference;

import java.util.HashMap;
import java.util.Map;

public enum Menu {
    MAIN_MENU("""
            #############################
            ##                         ##
            ##   M A S T E R M I N D   ##
            ##                         ##
            #############################
            Choose one:
            1. Play
            2. Settings
            0. Quit""", 3),
    SETTINGS_MAIN("""
            ************************
            **      Settings      **
            ************************
            Choose any to modify:
            1. Max attempts
            2. Code length
            3. Number of colors
            4. Secret code supplier
            5. Feedback strategy
            0. Back""", 6),
    SETTINGS_ATTEMPTS("Enter max attempts: ", 0),
    SETTINGS_LENGTH("Enter code length: ", 0),
    SETTINGS_COLORS("Enter number of colors: ", 0),
    SETTINGS_FEEDBACK("""
            ************************
            **  Feedback Strategy **
            ************************
            Choose one:
            1. Standard Mastermind (default)
            2. Only exact matches
            3. Accuracy
            4. Permutation
            0. Back""", 5),
    SETTINGS_CODE("""
            ************************
            **   Code Provider    **
            ************************
            Choose one:
            1. Random.org API (default)
            2. Local random
            3. User defined
            0. Back""", 4),
    SETTINGS_CODE_USER("Enter secret code: ", 0),
    EXIT("\nGoodBye!", 0),
    PLAY("\nStart!", 0);

    private final String view;
    private final int numChoices;

    Menu(String view, int numChoices) {
        this.view = view;
        this.numChoices = numChoices;
    }

    /* TODO: refactor to use these maps, maybe use:
    public static final Map<Menu, Map<Integer,Menu>> navigationalMenu; // contains Main, Setting; integer that isn't a key isn't a valid choice for that menu; these also require no invisible side effects
    public static final Map<Menu, Menu> inputCollectionMenu; // contains all others; (remove "Back" from SETTINGS_FEEDBACK, SETTINGS_CODE to emphasize these are _not_ navigational
        // these ones all require some action (or actions) to be taken after collecting user input
        // e.g., from userCode menu, set user defined supplier then secretCode
        // e.g. from numColors menu, set numColors
     */

    public static final Map<Menu, Map<Integer, Menu>> menuMap = new HashMap<>();

    static {
        menuMap.put(MAIN_MENU, Map.of(0, EXIT, 1, PLAY, 2, SETTINGS_MAIN));
        menuMap.put(SETTINGS_MAIN, Map.of(0, MAIN_MENU, 1, SETTINGS_ATTEMPTS, 2, SETTINGS_LENGTH, 3, SETTINGS_COLORS, 4, SETTINGS_CODE, 5, SETTINGS_FEEDBACK));
    }

    public static final Map<Menu, Menu> menuSelectionMapping = new HashMap<>();

    static {
        menuSelectionMapping.put(SETTINGS_CODE_USER, SETTINGS_MAIN);
        menuSelectionMapping.put(SETTINGS_LENGTH, SETTINGS_MAIN);
        menuSelectionMapping.put(SETTINGS_COLORS, SETTINGS_MAIN);
        menuSelectionMapping.put(SETTINGS_ATTEMPTS, SETTINGS_MAIN);
        menuSelectionMapping.put(SETTINGS_FEEDBACK, SETTINGS_MAIN);
    }

    public static final Map<Integer, CodeGenerationPreference> codeGenerationMap = new HashMap<>();
    static {
        codeGenerationMap.put(1, CodeGenerationPreference.RANDOM_ORG_API);
        codeGenerationMap.put(2, CodeGenerationPreference.LOCAL_RANDOM);
        codeGenerationMap.put(3, CodeGenerationPreference.USER_DEFINED);
    }

    public static final Map<Integer, FeedbackStrategyPreference> feedbackStrategyMap = new HashMap<>();
    static {
        feedbackStrategyMap.put(1, FeedbackStrategyPreference.STANDARD);
    }

    public static boolean isIntegerEntryMenu(Menu menu) {
        return menu == SETTINGS_LENGTH || menu == SETTINGS_COLORS || menu == SETTINGS_ATTEMPTS;
    }

    public String view() {
        return view;
    }

    public int numChoices() {
        return numChoices;
    }

}

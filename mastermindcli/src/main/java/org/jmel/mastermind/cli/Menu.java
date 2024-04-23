package org.jmel.mastermind.cli;

import org.jmel.mastermind.core.feedbackstrategy.FeedbackStrategy;
import org.jmel.mastermind.core.feedbackstrategy.FeedbackStrategyImpl;

import java.util.HashMap;
import java.util.Map;

public enum Menu {
    EXIT("\nGoodBye!"),
    PLAY("\nStart!"),
    MAIN_MENU("""
            #############################
            ##                         ##
            ##   M A S T E R M I N D   ##
            ##                         ##
            #############################
            Choose one:
            1. Play
            2. Multiplayer
            3. Settings
            0. Quit"""), // changes here must be reflected in navigationalMenu
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
            0. Back"""), // changes here must be reflected in navigationalMenu
    SETTINGS_FEEDBACK_PREFERENCE("""
            ************************
            **  Feedback Strategy **
            ************************
            Choose one:
            1. Default
            2. Original Mastermind
            3. Higher/Lower
            4. Perfect"""), // changes here must be reflected in feedbackStrategyMap
    SETTINGS_CODE_GENERATION_PREFERENCE("""
            ************************
            **   Code Supplier    **
            ************************
            Choose one:
            1. Random.org API (default)
            2. Local random
            3. User defined"""), // changes here must be reflected in codeGenerationMap
    SETTINGS_CODE_USER_DEFINED("Enter secret code: "),
    SETTINGS_MAX_ATTEMPTS("Enter max attempts: "),
    SETTINGS_CODE_LENGTH("Enter code length: "),
    SETTINGS_NUM_COLORS("Enter number of colors: "),
    GAME_SETUP_FAILED("Press 1 to revise settings or 0 to quit"),
    MULTIPLAYER_SETUP("How many players?");

    // Every menu must be either a navigational menu or an input collection menu, but not both
    public static final Map<Menu, Map<Integer, Menu>> navigationalMenu = new HashMap<>();
    public static final Map<Menu, Menu> inputCollectionMenu = new HashMap<>();
    public static final Map<Integer, CodeSupplierPreference> codeGenerationMap = new HashMap<>();
    public static final Map<Integer, FeedbackStrategy> feedbackStrategyMap = new HashMap<>();

    static {
        navigationalMenu.put(MAIN_MENU, Map.of(0, EXIT, 1, PLAY, 2, MULTIPLAYER_SETUP, 3, SETTINGS_MAIN));
        navigationalMenu.put(SETTINGS_MAIN, Map.of(0, MAIN_MENU, 1, SETTINGS_MAX_ATTEMPTS, 2, SETTINGS_CODE_LENGTH, 3, SETTINGS_NUM_COLORS, 4, SETTINGS_CODE_GENERATION_PREFERENCE, 5, SETTINGS_FEEDBACK_PREFERENCE));
        navigationalMenu.put(GAME_SETUP_FAILED, Map.of(0, EXIT, 1, SETTINGS_MAIN));
    }

    static {
        inputCollectionMenu.put(SETTINGS_CODE_LENGTH, SETTINGS_MAIN);
        inputCollectionMenu.put(SETTINGS_NUM_COLORS, SETTINGS_MAIN);
        inputCollectionMenu.put(SETTINGS_MAX_ATTEMPTS, SETTINGS_MAIN);
        inputCollectionMenu.put(SETTINGS_CODE_GENERATION_PREFERENCE, SETTINGS_MAIN);
        inputCollectionMenu.put(SETTINGS_CODE_USER_DEFINED, SETTINGS_MAIN);
        inputCollectionMenu.put(SETTINGS_FEEDBACK_PREFERENCE, SETTINGS_MAIN);
        inputCollectionMenu.put(MULTIPLAYER_SETUP, PLAY);
    }

    static {
        codeGenerationMap.put(1, CodeSupplierPreference.RANDOM_ORG_API);
        codeGenerationMap.put(2, CodeSupplierPreference.LOCAL_RANDOM);
        codeGenerationMap.put(3, CodeSupplierPreference.USER_DEFINED);
    }

    static {
        feedbackStrategyMap.put(1, FeedbackStrategyImpl.DEFAULT);
        feedbackStrategyMap.put(2, FeedbackStrategyImpl.ORIGINAL_MASTERMIND);
        feedbackStrategyMap.put(3, FeedbackStrategyImpl.HIGHER_LOWER);
        feedbackStrategyMap.put(4, FeedbackStrategyImpl.PERFECT);
    }

    private final String displayString;

    Menu(String displayString) {
        this.displayString = displayString;
    }

    @Override
    public String toString() {
        return displayString;
    }
}

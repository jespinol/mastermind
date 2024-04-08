package org.jmel.mastermind.cli;

import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class CliUtils {
    private static final Scanner scanner = new Scanner(System.in);

    public static int getIntegerChoiceFromSet(Set<Integer> allowedChoices) {
        return getIntegerMatchingCondition(allowedChoices::contains);
    }

    public static int getIntegerValue() {
        return getIntegerMatchingCondition(i -> i > 0);
    }

    private static int getIntegerMatchingCondition(Predicate<Integer> p) {
        while (true) {
            System.out.print("> ");
            String trimmedLine = scanner.nextLine().trim();
            int choice;
            try {
                choice = Integer.parseInt(trimmedLine);
                if (p.test(choice)) {
                    return choice;
                } else {
                    System.out.println("Invalid number chosen.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid selection. Please enter a number.");
            }
        }
    }

    public static List<Integer> getNextCode(int codeLength, int numColors) {
        do {
            System.out.print("> ");
            String trimmedLine = scanner.nextLine().trim();
            try {
                List<Integer> possibleCode = Stream.of(trimmedLine.split("\\s+")).map(Integer::parseInt).toList();
                if (isCodeValid(possibleCode, codeLength, numColors)) {
                    return possibleCode;
                } else {
                    System.out.printf("Invalid code. Please enter %d digits from 0 to %d, space separated%n", codeLength, numColors - 1);
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid code. Please enter a numeric guess.");
            }
        } while (true);
    }

    public static boolean isCodeValid(List<Integer> possibleCode, int codeLength, int numColors) {
        return possibleCode.size() == codeLength && possibleCode.stream().allMatch(i -> i >= 0 && i < numColors);
    }
}

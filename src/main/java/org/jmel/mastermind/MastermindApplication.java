package org.jmel.mastermind;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Stream;

@SpringBootApplication
public class MastermindApplication implements ApplicationRunner {
    private static final Logger logger = LoggerFactory.getLogger(MastermindApplication.class);
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        SpringApplication.run(MastermindApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        logger.info("Application started with command-line arguments: {}", Arrays.toString(args.getSourceArgs()));
        logger.info("NonOptionArgs: {}", args.getNonOptionArgs());
        logger.info("OptionNames: {}", args.getOptionNames());

        for (String name : args.getOptionNames()) {
            logger.info("arg-" + name + "=" + args.getOptionValues(name));
        }

        boolean containsOption = args.containsOption("person.name");
        logger.info("Contains person.name: " + containsOption);

        Game game = new Game.Builder().build();

        while (!game.isGameOver()) {
            List<Integer> guess = getGuess();
            Feedback feedback = game.processGuess(guess);
            System.out.println(feedback);
        }
    }

    private static List<Integer> getGuess() {
        System.out.print("Enter your guess in one line, space separated like 1 2 3 4 :  ");
        String guess = scanner.nextLine();
        return Stream.of(guess.trim().split("\\s+"))
                .map(Integer::parseInt)
                .toList();
    }

    //    private static List<Integer> getSecretCode(Configuration config) {
//        System.out.print("Enter the secret code in one line, space separated like 1 2 3 4 :  ");
//
//        String secretCode = scanner.nextLine();
//        boolean isWellFormatted = false;
//        while (!isWellFormatted) {
//            secretCode = scanner.nextLine();
//            isWellFormatted = secretCode.length() == config.codeLength();
//            isWellFormatted &= Stream.of(secretCode.trim().split("\\s+"))
//                    .allMatch(s -> s.matches("\\d+"));
//            isWellFormatted &= Stream.of(secretCode.trim().split("\\s+"))
//                    .map(Integer::parseInt)
//                    .allMatch(i -> i >= 0 && i < config.numColors());
//        }
//
//        return Stream.of(secretCode.trim().split("\\s+"))
//                .map(Integer::parseInt)
//                .toList();
//    }
}

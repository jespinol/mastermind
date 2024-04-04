package org.jmel.mastermind;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;
import java.util.Scanner;
import java.util.stream.Stream;

@SpringBootApplication
public class MastermindApplication {

	public static void main(String[] args) {
		SpringApplication.run(MastermindApplication.class, args);
		Game game = new Game();

		while (!game.isGameOver()) {
			List<Integer> guess = getGuess();
			Feedback feedback = game.processGuess(guess);
			System.out.println(feedback);
		}
	}
	private static final Scanner scanner = new Scanner(System.in);

	private static List<Integer> getGuess() {
		System.out.print("Enter your guess in one line, space separated like 1 2 3 4 :  ");
		String guess = scanner.nextLine();
		return Stream.of(guess.trim().split("\\s+"))
				.map(Integer::parseInt)
				.toList();
	}

}

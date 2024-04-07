package org.jmel.mastermind.cli;


import org.jmel.mastermind.core.Feedback;
import org.jmel.mastermind.core.Game;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Stream;
public class MastermindCliApplication {
	private static final Scanner scanner = new Scanner(System.in);

	public static void main(String[] args) throws IOException {
		System.out.println("Welcome to Mastermind!");
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

}

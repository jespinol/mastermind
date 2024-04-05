package org.jmel.mastermind;

import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

import java.util.*;
import java.util.stream.Collectors;

public class Game {
    private final int codeLength = 4;
    private final int maxAttempts = 10;
    private final int numColors = 8;
    private final Code secretCode;
    private final List<Code> guessHistory = new ArrayList<>();

    private Game() {
        this.secretCode = new Code(generateSecretCode());
    }

    private Game(Code secretCode) {
        this.secretCode = secretCode;
    }

    // get instance with defaults
    public static Game createGame() {
        return new Game();
    }

    // get instance with custom values (any of them)
    public static Game createGameWithCode(Code secretCode) {
        return new Game(secretCode);
    }

    public Feedback processGuess(List<Integer> guessInput) {
        if (isGameOver()) {
            throw new IllegalStateException("Max attempts reached"); // TODO custom exception
        }

        Code guess = new Code(guessInput);
        guessHistory.add(guess);
        return computeFeedback(this.secretCode, guess);
    }

    public int getMovesLeft() {
        return maxAttempts - guessHistory.size();
    }

    private Feedback computeFeedback(Code secretCode, Code guess) {
        int wellPlaced = 0;
        Map<Integer, Integer> secretFreq = new HashMap<>();
        Map<Integer, Integer> guessFreq = new HashMap<>();

        for (int i = 0; i < secretCode.getValue().size(); i++) {
            int secretDigit = secretCode.getValue().get(i);
            int guessDigit = guess.getValue().get(i);
            if (secretDigit == guessDigit) {
                wellPlaced++;
            } else {
                secretFreq.put(secretDigit, secretFreq.getOrDefault(secretDigit, 0) + 1);
                guessFreq.put(guessDigit, guessFreq.getOrDefault(guessDigit, 0) + 1);
            }
        }

        int misplaced = 0;
        for (int digit : guessFreq.keySet()) {
            int timesInGuess = guessFreq.get(digit);
            int timesInSecret = secretFreq.getOrDefault(digit, 0);
            misplaced += Math.min(timesInGuess, timesInSecret);
        }

        return new Feedback(wellPlaced, misplaced);
    }

    public boolean isGameOver() {
        boolean wonGame = !guessHistory.isEmpty() && Objects.equals(guessHistory.get(guessHistory.size() - 1), secretCode);
        return wonGame || getMovesLeft() == 0; // TODO ensure movesLeft can't be negative
    }

    private List<Integer> generateSecretCode() {
        // TODO handle connection errors
        JdkClientHttpRequestFactory clientHttpRequestFactory = new JdkClientHttpRequestFactory();
        clientHttpRequestFactory.setReadTimeout(1000);

        RestClient restClient = RestClient
                .builder()
                .baseUrl("https://www.random.org")
                .requestFactory(clientHttpRequestFactory)
                .build();

        // TODO use quota check. A request for 4 integers from 0 to 7 costs 12 bits
//        String quota = restClient
//                .get()
//                .uri("/quota/?format=plain")
//                .retrieve()
//                .body(String.class);

        // TODO handle failed requests
        String result = restClient
                .get()
                .uri(String.format("/integers/?num=%d&min=%d&max=%d&col=1&base=10&format=plain&rnd=new", codeLength, 0, numColors - 1))
                .retrieve()
                .body(String.class);

        return Arrays.stream(result.split("\n"))
                .map(Integer::parseInt)
                .collect(Collectors.toList());
    }
}

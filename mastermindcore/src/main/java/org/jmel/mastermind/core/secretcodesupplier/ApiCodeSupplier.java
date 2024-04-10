package org.jmel.mastermind.core.secretcodesupplier;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A code supplier that uses the random.org API to generate a random code of a given length where each code element is
 * one of the allowed colors. It uses the free tier API which allows up to 1,000,000 bits per day per IP address.
 */
public class ApiCodeSupplier implements CodeSupplier {
    private static final String QUOTA_URI = "https://www.random.org/quota/?format=plain";
    private static final String INTS_URI = "https://www.random.org/integers/?num=%d&min=%d&max=%d&col=1&base=10&format=plain&rnd=new";
    private final int codeLength;
    private final int numColors;

    private ApiCodeSupplier(int codeLength, int numColors) {
        this.codeLength = codeLength;
        this.numColors = numColors;
    }

    public static ApiCodeSupplier of(int codeLength, int numColors) {
        return new ApiCodeSupplier(codeLength, numColors);
    }

    /**
     * Gets a random code from the random.org API.
     *
     * @return a Code object
     * @throws IOException if the API request fails or the quota is exceeded
     */
    @Override
    public List<Integer> get() throws IOException {
        try {
            HttpClient httpClient = buildHttpClient();
            if (checkQuota(httpClient)) {

                return List.copyOf(getCodeFromApi(httpClient));
            } else {
                throw new IOException("Random.org quota exceeded");
            }
        } catch (RuntimeException e) {
            // Catches API exceptions (not including quota exceeded exception above) and rethrows them as IOExceptions
            throw new IOException("Failed to get secret code from API", e);
        }
    }

    private HttpClient buildHttpClient() {
        return HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(2))
                .build();
    }

    private boolean checkQuota(HttpClient client) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(QUOTA_URI))
                    .timeout(Duration.ofSeconds(2))
                    .build();

            String quota = client.send(request, HttpResponse.BodyHandlers.ofString())
                    .body()
                    .trim();

            int bitsNeeded = this.codeLength * (int) (Math.log(this.numColors - 1) / Math.log(2) + 1);

            return Integer.parseInt(quota) >= bitsNeeded * 10;
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private List<Integer> getCodeFromApi(HttpClient client) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(generateIntegerRequestPath()))
                    .timeout(Duration.ofSeconds(1))
                    .build();

            String result = client.send(request, HttpResponse.BodyHandlers.ofString())
                    .body()
                    .trim();

            return Arrays.stream(result.split("\n"))
                    .map(Integer::parseInt)
                    .collect(Collectors.toList());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private String generateIntegerRequestPath() {
        return String.format(INTS_URI, this.codeLength, 0, this.numColors - 1);
    }
}

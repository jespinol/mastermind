package org.jmel.mastermind.core.secret_code_suppliers;

import org.jmel.mastermind.core.Code;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ApiCodeSupplier implements CodeSupplier {
    private static final String QUOTA_URI = "https://www.random.org/quota/?format=plain";
    private static final String INTS_URI = "https://www.random.org/integers/?num=%d&min=%d&max=%d&col=1&base=10&format=plain&rnd=new";
    private final int codeLength;
    private final int numColors;

    public ApiCodeSupplier(int codeLength, int numColors) {
        this.codeLength = codeLength;
        this.numColors = numColors;
    }

    @Override
    public Code get() throws IOException {
        try {
            HttpClient httpClient = buildHttpClient();
            if (checkQuota(httpClient)) {
                List<Integer> codeValue = getCodeFromApi(httpClient);

                return Code.from(codeValue, codeLength, numColors);
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
                .connectTimeout(Duration.ofSeconds(1))
                .build();
    }

    private boolean checkQuota(HttpClient client) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(QUOTA_URI))
                    .timeout(Duration.ofSeconds(1))
                    .build();

            String quota = client.send(request, HttpResponse.BodyHandlers.ofString())
                    .body()
                    .trim();

            // TODO: fix use the number of bits using base 2 log. e.g., a request for 4 integers from 0 to 7 costs 4*3 = 12 bits.
            return Integer.parseInt(quota) >= this.codeLength * 4;
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

package org.jmel.mastermind.secret_code_suppliers;

import org.jmel.mastermind.Code;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ApiCodeSupplier implements CodeSupplier {
    private static final String BASE_URL = "https://www.random.org";
    private static final String QUOTA_URI = "/quota/?format=plain";
    private static final String INTS_URI = "/integers/?num=%d&min=%d&max=%d&col=1&base=10&format=plain&rnd=new";
    private final int codeLength;
    private final int numColors;

    public ApiCodeSupplier(int codeLength, int numColors) {
        this.codeLength = codeLength;
        this.numColors = numColors;
    }

    @Override
    public Code get() throws IOException {
        RestClient restClient = buildRestClient();
        // TODO test below with mock failures
        try {
            if (checkQuota(restClient)) {
                List<Integer> codeValue = getCodeFromApi(restClient);
                return Code.from(codeValue, codeLength, numColors);
            } else {
                throw new IOException("Quota exceeded");
            }
        } catch (RuntimeException e) {
            // Catches API exceptions (not including quota exceeded exception above) and rethrows them as IOExceptions
            throw new IOException("Failed to get secret code from API", e);
        }
    }

    private RestClient buildRestClient() {
        // TODO: handle connection errors
        JdkClientHttpRequestFactory clientHttpRequestFactory = new JdkClientHttpRequestFactory();
        clientHttpRequestFactory.setReadTimeout(1000);

        return RestClient
                .builder()
                .baseUrl(BASE_URL)
                .requestFactory(clientHttpRequestFactory)
                .build();
    }

    private boolean checkQuota(RestClient restClient) {
        // TODO: use quota check. E.g., a request for 4 integers from 0 to 7 costs 4*3 = 12 bits.
        String quota = Objects.requireNonNull(restClient
                .get()
                .uri(QUOTA_URI)
                .retrieve()
                .body(String.class)).trim(); // Will throw a RuntimeException on 503

        return Integer.parseInt(quota) >= this.codeLength * 4; // TODO fix use the number of bits using base 2 log.
    }

    private List<Integer> getCodeFromApi(RestClient restClient) {
        String result = Objects.requireNonNull(restClient
                .get()
                .uri(generateRequestPath())
                .retrieve()
                .body(String.class)); // Will throw a RuntimeException on 503

        System.out.println(BASE_URL + generateRequestPath());

        return Arrays.stream(result.split("\n"))
                .map(Integer::parseInt)
                .collect(Collectors.toList());
    }

    private String generateRequestPath() {
        return String.format(INTS_URI, this.codeLength, 0, this.numColors - 1);
    }
}

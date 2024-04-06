package org.jmel.mastermind.secret_code_suppliers;

import org.jmel.mastermind.Code;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
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
    public Optional<Code> get() { // TODO: this doesn't return an empty optional if api calls fail -- can throw runtime exceptions
        RestClient restClient = buildRestClient();

        if (checkQuota(restClient)) {
            List<Integer> codeValue = getCodeFromApi(restClient);
            return Optional.of(Code.from(codeValue, codeLength, numColors));
        }

        return Optional.empty();
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
        // TODO: use quota check. A request for 4 integers from 0 to 7 costs 12 bits.
        //  Check for response errors and handle them.
        String quota = Objects.requireNonNull(restClient
                .get()
                .uri(QUOTA_URI)
                .retrieve()
                .body(String.class)).trim();

        return Integer.parseInt(quota) >= this.codeLength * 4;
    }

    private List<Integer> getCodeFromApi(RestClient restClient) {
        // TODO handle failed requests
        String result = Objects.requireNonNull(restClient
                .get()
                .uri(generateRequestPath())
                .retrieve()
                .body(String.class));

        return Arrays.stream(result.split("\n"))
                .map(Integer::parseInt)
                .collect(Collectors.toList());
    }

    private String generateRequestPath() {
        return String.format(INTS_URI, this.codeLength, 0, this.numColors - 1);
    }
}

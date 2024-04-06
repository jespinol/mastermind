package org.jmel.mastermind.secret_code_suppliers;

import org.jmel.mastermind.Code;
import org.jmel.mastermind.custom_exceptions.ApiQuotaExceededException;
import org.jmel.mastermind.custom_exceptions.ApiResponseException;
import org.jmel.mastermind.custom_exceptions.InvalidCodeException;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

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
    public Code get() throws InvalidCodeException, ApiQuotaExceededException {
        RestClient restClient = buildRestClient();

        if (!checkQuota(restClient)) {
            throw new ApiQuotaExceededException("Quota exceeded");
        }

        List<Integer> codeValue = getCodeFromApi(restClient);
        return Code.from(codeValue, codeLength, numColors);
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

    private boolean checkQuota(RestClient restClient) throws ApiResponseException {
        // TODO: use quota check. A request for 4 integers from 0 to 7 costs 12 bits.
        try {
            String quota = Objects.requireNonNull(
                            restClient
                                    .get()
                                    .uri(QUOTA_URI)
                                    .retrieve()
                                    .body(String.class))
                    .trim();
            return Integer.parseInt(quota) >= this.codeLength * 4;
        } catch (RuntimeException e) {
            throw new ApiResponseException("API error while checking quota");
        }

    }

    private List<Integer> getCodeFromApi(RestClient restClient) throws ApiResponseException {
        try {
            String result = restClient
                    .get()
                    .uri(generateRequestPath())
                    .retrieve()
                    .body(String.class);

            return Arrays.stream(Objects.requireNonNull(result)
                            .split("\n"))
                    .map(Integer::parseInt)
                    .collect(Collectors.toList());
        } catch (RuntimeException e) {
            throw new ApiResponseException("API error while getting code");
        }
    }

    private String generateRequestPath() {
        return String.format(INTS_URI, this.codeLength, 0, this.numColors - 1);
    }
}

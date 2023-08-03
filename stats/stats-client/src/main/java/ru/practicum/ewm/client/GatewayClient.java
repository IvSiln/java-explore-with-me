package ru.practicum.ewm.client;

import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

public class GatewayClient {
    private final RestTemplate restTemplate;

    public GatewayClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    protected ResponseEntity<Object> get(String path, Map<String, Object> parameters) {
        return makeRequest(HttpMethod.GET, path, parameters, null);
    }

    protected <T> ResponseEntity<Object> post(String path, T body) {
        return makeRequest(HttpMethod.POST, path, null, body);
    }

    private <T> ResponseEntity<Object> makeRequest(HttpMethod httpMethod, String path,
                                                   Map<String, Object> parameters, T body) {
        HttpHeaders headers = createDefaultHeaders();
        HttpEntity<T> httpEntity = new HttpEntity<>(body, headers);

        try {
            if (parameters != null) {
                return restTemplate.exchange(path, httpMethod, httpEntity, Object.class, parameters);
            } else {
                return restTemplate.exchange(path, httpMethod, httpEntity, Object.class);
            }
        } catch (HttpClientErrorException exception) {
            return ResponseEntity.status(exception.getStatusCode())
                    .body(exception.getResponseBodyAsByteArray());
        }
    }

    private HttpHeaders createDefaultHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        return headers;
    }
}

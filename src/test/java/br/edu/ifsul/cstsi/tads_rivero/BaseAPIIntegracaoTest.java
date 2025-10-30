package br.edu.ifsul.cstsi.tads_rivero;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

public abstract class BaseAPIIntegracaoTest {

    @Autowired
    protected TestRestTemplate rest;

    protected HttpHeaders getHeaders() {
        return new HttpHeaders();
    }

    protected HttpHeaders getHeaders(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token);
        return headers;
    }

    protected <T> ResponseEntity<T> get(String url, Class<T> responseType) {
        return rest.exchange(
                url,
                HttpMethod.GET,
                new HttpEntity<>(getHeaders()),
                responseType
        );
    }

    protected <T> ResponseEntity<T> get(String url, Class<T> responseType, String token) {
        return rest.exchange(
                url,
                HttpMethod.GET,
                new HttpEntity<>(getHeaders(token)),
                responseType
        );
    }

    protected <T> ResponseEntity<T> post(String url, Object request, Class<T> responseType) {
        return rest.exchange(
                url,
                HttpMethod.POST,
                new HttpEntity<>(request, getHeaders()),
                responseType
        );
    }

    protected <T> ResponseEntity<T> post(String url, Object request, Class<T> responseType, String token) {
        return rest.exchange(
                url,
                HttpMethod.POST,
                new HttpEntity<>(request, getHeaders(token)),
                responseType
        );
    }

    protected <T> ResponseEntity<T> put(String url, Object request, Class<T> responseType) {
        return rest.exchange(
                url,
                HttpMethod.PUT,
                new HttpEntity<>(request, getHeaders()),
                responseType
        );
    }

    protected <T> ResponseEntity<T> put(String url, Object request, Class<T> responseType, String token) {
        return rest.exchange(
                url,
                HttpMethod.PUT,
                new HttpEntity<>(request, getHeaders(token)),
                responseType
        );
    }

    protected <T> ResponseEntity<T> delete(String url, Class<T> responseType) {
        return rest.exchange(
                url,
                HttpMethod.DELETE,
                new HttpEntity<>(getHeaders()),
                responseType
        );
    }

    protected <T> ResponseEntity<T> delete(String url, Class<T> responseType, String token) {
        return rest.exchange(
                url,
                HttpMethod.DELETE,
                new HttpEntity<>(getHeaders(token)),
                responseType
        );
    }
}

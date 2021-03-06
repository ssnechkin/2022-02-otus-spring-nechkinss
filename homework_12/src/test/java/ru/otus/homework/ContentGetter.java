package ru.otus.homework;

import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import ru.otus.homework.dto.out.Content;

import java.net.URI;

public class ContentGetter {
    private final int port;
    private String baseUrl;
    private boolean isSecure = true;

    private final TestRestTemplate restTemplate;

    public ContentGetter(int port, TestRestTemplate restTemplate) {
        this.port = port;
        this.restTemplate = restTemplate;
        baseUrl = "http://localhost:" + port;
    }

    public ContentGetter(int port, TestRestTemplate restTemplate, boolean isSecure) {
        this.port = port;
        this.restTemplate = restTemplate;
        baseUrl = "http://localhost:" + port;
        this.isSecure = isSecure;
    }

    public Content getContent(String url) {
        return getContent(HttpMethod.GET, url, null);
    }

    public <T> Content getContent(HttpMethod method, String url, T object) {
        RequestEntity<Content> requestEntity = (RequestEntity<Content>) new RequestEntity<T>(object, getHttpHeaders(), method, URI.create(baseUrl + url));
        ResponseEntity<Content> responseEntity = this.restTemplate.exchange(requestEntity, Content.class);
        return responseEntity.getBody();
    }

    private HttpHeaders getHttpHeaders() {
        if (!isSecure) {
            return new HttpHeaders();
        }
        String cookie = getCookieForUser(baseUrl + "/page/login");
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", cookie);
        return headers;
    }

    private String getCookieForUser(String loginUrl) {
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.set("username", "admin");
        form.set("password", "password");
        ResponseEntity<String> loginResponse = this.restTemplate.postForEntity(
                loginUrl,
                new HttpEntity<>(form, new HttpHeaders()),
                String.class);
        return loginResponse.getHeaders().get("Set-Cookie").get(0);
    }
}

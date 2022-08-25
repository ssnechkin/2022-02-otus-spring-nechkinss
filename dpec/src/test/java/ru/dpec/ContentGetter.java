package ru.dpec;

import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import ru.dpec.domain.dto.out.Content;

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

    public Content getContent(String url, String login, String password) {
        return getContent(HttpMethod.GET, url, login, password, null);
    }

    public <T> Content getContent(HttpMethod method, String url, String login, String password, T object) {
        RequestEntity<Content> requestEntity = (RequestEntity<Content>) new RequestEntity<T>(object, getHttpHeaders(login, password), method, URI.create(baseUrl + url));
        ResponseEntity<Content> responseEntity = this.restTemplate.exchange(requestEntity, Content.class);
        if (responseEntity.getStatusCode().value() > 299) {
            ResponseEntity<String> responseEntity1 = this.restTemplate.exchange(requestEntity, String.class);
            System.out.println(responseEntity1.getStatusCode());
            System.out.println(responseEntity1.getBody());
        }
        return responseEntity.getBody();
    }

    private HttpHeaders getHttpHeaders(String login, String password) {
        if (!isSecure) {
            return new HttpHeaders();
        }
        String cookie = getCookieForUser(baseUrl + "/page/login", login, password);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", cookie);
        return headers;
    }

    private String getCookieForUser(String loginUrl, String login, String password) {
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.set("username", login);
        form.set("password", password);
        ResponseEntity<String> loginResponse = this.restTemplate.postForEntity(
                loginUrl,
                new HttpEntity<>(form, new HttpHeaders()),
                String.class);
        return loginResponse.getHeaders().get("Set-Cookie").get(0);
    }
}

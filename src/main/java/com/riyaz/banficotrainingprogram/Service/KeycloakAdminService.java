package com.riyaz.banficotrainingprogram.Service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class KeycloakAdminService {

    @Value("${keycloak.admin.url}")
    private String adminUrl;

    @Value("${keycloak.admin.username}")
    private String adminUsername;

    @Value("${keycloak.admin.password}")
    private String adminPassword;

    @Value("${keycloak.admin.realm}")
    private String realm;

    private final RestTemplate restTemplate;

    public KeycloakAdminService(RestTemplateBuilder builder) {
        this.restTemplate = builder.build();
    }

    public String createUser(String username, String email, String firstName, String lastName, String temporaryPassword) {
        String token = getAdminToken();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> user = new HashMap<>();
        user.put("username", username);
        user.put("email", email);
        user.put("firstName", firstName);
        user.put("lastName", lastName);
        user.put("enabled", true);
        user.put("emailVerified", true);

        ResponseEntity<Void> createResp = restTemplate.postForEntity(
                adminUrl + "/admin/realms/" + realm + "/users",
                new HttpEntity<>(user, headers),
                Void.class
        );

        String location = createResp.getHeaders().getFirst("Location");
        String userId = location.substring(location.lastIndexOf('/') + 1);

        Map<String, Object> credential = new HashMap<>();
        credential.put("type", "password");
        credential.put("value", temporaryPassword);
        credential.put("temporary", true);

        restTemplate.exchange(
                adminUrl + "/admin/realms/" + realm + "/users/" + userId + "/reset-password",
                HttpMethod.PUT,
                new HttpEntity<>(credential, headers),
                Void.class
        );

        ResponseEntity<Map> roleResp = restTemplate.exchange(
                adminUrl + "/admin/realms/" + realm + "/roles/user",
                HttpMethod.GET,
                new HttpEntity<>(headers),
                Map.class
        );

        restTemplate.postForEntity(
                adminUrl + "/admin/realms/" + realm + "/users/" + userId + "/role-mappings/realm",
                new HttpEntity<>(List.of(roleResp.getBody()), headers),
                Void.class
        );

        return userId;
    }

    @SuppressWarnings("unchecked")
    private String getAdminToken() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "password");
        body.add("client_id", "admin-cli");
        body.add("username", adminUsername);
        body.add("password", adminPassword);

        ResponseEntity<Map> response = restTemplate.postForEntity(
                adminUrl + "/realms/master/protocol/openid-connect/token",
                new HttpEntity<>(body, headers),
                Map.class
        );

        return (String) response.getBody().get("access_token");
    }
}

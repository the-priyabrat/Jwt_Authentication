package com.com.pri_vrat.authentication.config;

import com.com.pri_vrat.authentication.util.Constants;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KeycloakConfig {

    @Value("${docmanager.keycloak.client.id}")
    private String clientId;

    @Value("${docmanager.keycloak.client.secret}")
    private String clientSecret;

    @Value("${docmanager.keycloak.server-uri}")
    private String serverUrl;

    @Value("${docmanager.keycloak.admin.username}")
    private String userName;

    @Value("${docmanager.keycloak.admin.password}")
    private String password;

    @Value("${docmanager.keycloak.realm}")
    private String realm;

    @Bean
    public Keycloak getKeycloakBean() {
        return KeycloakBuilder.builder()
                .clientId(clientId)
                .realm(realm)
                .clientSecret(clientSecret)
                .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
                .serverUrl(serverUrl)
                .password(password)
                .username(userName)
                .build();
    }
}

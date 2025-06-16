package com.example.progettopsw.configurations.keycloak;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;

@Configuration
public class KCBeans {

    @Bean
    public Keycloak keycloak() {
        return KeycloakBuilder.builder()
                .serverUrl("http://localhost:8080")
                .realm("ProgettoPSW")
                .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
                .clientId("album-app")
                .clientSecret("IlJkz6lNn9zSgsc7af8BCcsqGlWhe5K8")
                .build();
    }
}

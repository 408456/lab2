package goltsman.btuserservice.config.keycloak;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KeycloakConfig {

    @Bean
    public Keycloak keycloakClient(
            @Value("${keycloak.serverUrl}") String serverUrl,
            @Value("${keycloak.realm}") String realm,
            @Value("${keycloak.client.service-client.client-id}") String clientId,
            @Value("${keycloak.client.service-client.client-secret}") String clientSecret
    ) {

        return KeycloakBuilder.builder()
                .serverUrl(serverUrl)
                .realm(realm)
                .clientId(clientId)
                .clientSecret(clientSecret)
                .grantType("client_credentials")
                .build();
    }
}
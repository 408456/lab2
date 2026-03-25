package goltsman.btrestaurantservice.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.OAuthFlow;
import io.swagger.v3.oas.annotations.security.OAuthFlows;
import io.swagger.v3.oas.annotations.security.OAuthScope;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Restaurant service",
                version = "1.0"
        ),
        security = @SecurityRequirement(name = "keycloak_oauth_scheme")
)
@SecurityScheme(
        name = "keycloak_oauth_scheme",
        type = SecuritySchemeType.OAUTH2,
        flows = @OAuthFlows(
                password = @OAuthFlow(
//                        tokenUrl = "http://localhost:8080/realms/booking-app/protocol/openid-connect/token"
                        tokenUrl = "http://localhost:8085/realms/booking-app/protocol/openid-connect/token")
        )
)
public class SwaggerConfig {
}
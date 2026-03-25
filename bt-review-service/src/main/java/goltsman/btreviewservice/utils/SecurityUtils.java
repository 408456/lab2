package goltsman.btreviewservice.utils;

import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

public class SecurityUtils {

    private SecurityUtils() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static Long getUserId() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null) {
            throw new AuthenticationCredentialsNotFoundException("Аутентификация отсутствует");
        }

        if (authentication.getPrincipal() instanceof Jwt jwt) {
            Object claim = jwt.getClaim("user_id");

            if (claim instanceof Number num) {
                return num.longValue();
            }
            if (claim instanceof String str) {
                try {
                    return Long.parseLong(str);
                } catch (NumberFormatException ex) {
                    throw new BadCredentialsException("Claim 'user_id' имеет неверный формат", ex);
                }
            }
            throw new BadCredentialsException("Claim 'user_id' отсутствует или некорректен");
        }

        throw new AuthenticationCredentialsNotFoundException("JWT токен отсутствует в контексте");
    }

    public static String getCurrentBearerToken() {
        var auth = org.springframework.security.core.context.SecurityContextHolder
                .getContext().getAuthentication();
        if (auth instanceof org.springframework.security.oauth2.
                server.resource.authentication.JwtAuthenticationToken jwtAuth) {
            return jwtAuth.getToken().getTokenValue();
        }
        return null;
    }
}

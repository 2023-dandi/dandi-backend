package dandi.dandi.auth.support;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

import dandi.dandi.auth.exception.UnauthorizedException;
import java.util.Enumeration;
import java.util.Objects;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

@Component
public class AuthorizationExtractor {

    public static final String AUTHENTICATION_TYPE = "Bearer";

    public String extractAccessToken(HttpServletRequest request) {
        Enumeration<String> authorizationHeaders = request.getHeaders(AUTHORIZATION);
        String value = authorizationHeaders.nextElement();
        checkNull(value);
        if (isAccessToken(value)) {
            return parseAccessToken(value);
        }
        throw UnauthorizedException.invalid();
    }

    private void checkNull(String value) {
        if (Objects.isNull(value)) {
            throw UnauthorizedException.accessTokenNotFound();
        }
    }

    private boolean isAccessToken(String value) {
        return value.startsWith(AUTHENTICATION_TYPE);
    }

    private String parseAccessToken(String value) {
        return value.substring(AUTHENTICATION_TYPE.length())
                .trim();
    }
}

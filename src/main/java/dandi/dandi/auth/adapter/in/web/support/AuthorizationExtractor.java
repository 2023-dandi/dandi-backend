package dandi.dandi.auth.adapter.in.web.support;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

import dandi.dandi.auth.exception.UnauthorizedException;
import java.util.Enumeration;
import java.util.Objects;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

@Component
public class AuthorizationExtractor {

    private static final String AUTHENTICATION_TYPE = "Bearer";
    private static final char AUTH_VALUE_DELIMITER = ',';

    public String extractAccessToken(HttpServletRequest request) {
        Enumeration<String> authorizationHeaders = request.getHeaders(AUTHORIZATION);
        String value = extract(authorizationHeaders);
        checkNull(value);
        return value;
    }

    private String extract(Enumeration<String> headers) {
        while (headers.hasMoreElements()) {
            String value = headers.nextElement();
            if ((value.startsWith(AUTHENTICATION_TYPE))) {
                String authHeaderValue = value.substring(AUTHENTICATION_TYPE.length()).trim();
                authHeaderValue = parseMultipleAuthHeaderValue(authHeaderValue);
                return authHeaderValue;
            }
        }
        return null;
    }

    private String parseMultipleAuthHeaderValue(String authHeaderValue) {
        int commaIndex = authHeaderValue.indexOf(AUTH_VALUE_DELIMITER);
        if (hasMultipleAuthHeaderValues(authHeaderValue)) {
            return authHeaderValue.substring(0, commaIndex);
        }
        return authHeaderValue;
    }

    private boolean hasMultipleAuthHeaderValues(String authHeaderValue) {
        int commaIndex = authHeaderValue.indexOf(AUTH_VALUE_DELIMITER);
        return commaIndex > 0;
    }

    private void checkNull(String value) {
        if (Objects.isNull(value)) {
            throw UnauthorizedException.accessTokenNotFound();
        }
    }
}

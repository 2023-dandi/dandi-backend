package dandi.dandi.auth.support;

import dandi.dandi.auth.infrastructure.token.JwtTokenManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    private final JwtTokenManager jwtTokenManager;
    private final AuthorizationExtractor authorizationExtractor;

    public AuthInterceptor(JwtTokenManager jwtTokenManager, AuthorizationExtractor authorizationExtractor) {
        this.jwtTokenManager = jwtTokenManager;
        this.authorizationExtractor = authorizationExtractor;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String accessToken = authorizationExtractor.extractAccessToken(request);
        jwtTokenManager.validate(accessToken);
        return true;
    }
}


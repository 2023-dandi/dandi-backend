package dandi.dandi.auth.adapter.in.web.support;

import dandi.dandi.auth.adapter.out.jwt.AccessTokenManagerAdapter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    private final AccessTokenManagerAdapter accessTokenManagerAdapter;
    private final AuthorizationExtractor authorizationExtractor;

    public AuthInterceptor(AccessTokenManagerAdapter accessTokenManagerAdapter,
                           AuthorizationExtractor authorizationExtractor) {
        this.accessTokenManagerAdapter = accessTokenManagerAdapter;
        this.authorizationExtractor = authorizationExtractor;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String accessToken = authorizationExtractor.extractAccessToken(request);
        accessTokenManagerAdapter.validate(accessToken);
        return true;
    }
}

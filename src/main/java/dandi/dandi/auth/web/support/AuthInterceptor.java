package dandi.dandi.auth.web.support;

import dandi.dandi.auth.adapter.out.jwt.AccessTokenManagerAdapter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.HttpMethod;
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
        if (isPostDetailsRequest(request)) {
            return true;
        }
        String accessToken = authorizationExtractor.extractAccessToken(request);
        accessTokenManagerAdapter.validate(accessToken);
        return true;
    }

    private boolean isPostDetailsRequest(HttpServletRequest request) {
        return request.getRequestURI().startsWith("/posts/") &&
                request.getMethod().equals(HttpMethod.GET.name());
    }
}

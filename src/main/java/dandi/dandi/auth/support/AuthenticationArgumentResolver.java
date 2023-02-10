package dandi.dandi.auth.support;

import dandi.dandi.auth.infrastructure.token.JwtTokenManager;
import javax.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class AuthenticationArgumentResolver implements HandlerMethodArgumentResolver {

    private final JwtTokenManager jwtTokenManager;
    private final AuthorizationExtractor authorizationExtractor;

    public AuthenticationArgumentResolver(JwtTokenManager jwtTokenManager,
                                          AuthorizationExtractor authorizationExtractor) {
        this.jwtTokenManager = jwtTokenManager;
        this.authorizationExtractor = authorizationExtractor;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(Login.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        String token = authorizationExtractor.extractAccessToken(request);
        return jwtTokenManager.getPayload(token);
    }
}

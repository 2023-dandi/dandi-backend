package dandi.dandi.config;

import dandi.dandi.auth.adapter.in.web.support.AuthInterceptor;
import dandi.dandi.auth.adapter.in.web.support.AuthenticationArgumentResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private static final List<String> SWAGGER_REQUEST_URIS = List.of("/api-docs", "/swagger-ui/**",
            "/v3/api-docs/swagger-config", "/v3/api-docs");

    private final AuthenticationArgumentResolver authenticationArgumentResolver;
    private final AuthInterceptor authInterceptor;

    public WebConfig(AuthenticationArgumentResolver authenticationArgumentResolver, AuthInterceptor authInterceptor) {
        this.authenticationArgumentResolver = authenticationArgumentResolver;
        this.authInterceptor = authInterceptor;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(authenticationArgumentResolver);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/")
                .excludePathPatterns("/auth/login/oauth/apple")
                .excludePathPatterns("/auth/refresh")
                .excludePathPatterns("/exhibition/**")
                .excludePathPatterns("/batch/**")
                .excludePathPatterns(SWAGGER_REQUEST_URIS);
    }
}

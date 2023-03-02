package dandi.dandi.config;

import dandi.dandi.auth.support.AuthInterceptor;
import dandi.dandi.auth.support.AuthenticationArgumentResolver;
import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

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
                .excludePathPatterns("/login/oauth/apple")
                .excludePathPatterns("/refresh");
    }
}


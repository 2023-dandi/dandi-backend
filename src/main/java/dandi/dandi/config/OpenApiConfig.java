package dandi.dandi.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI openAPI(@Value("${springdoc.open-api-version}") String openApiVersion) {
        Info info = new Info()
                .title("Dandi API")
                .version(openApiVersion)
                .description("Global Media 2023 Graduation Exhibition Team Dandi")
                .termsOfService("Team Dandi");

        return new OpenAPI()
                .components(new Components())
                .info(info);
    }
}

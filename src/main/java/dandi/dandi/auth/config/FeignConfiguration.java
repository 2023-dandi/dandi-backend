package dandi.dandi.auth.config;

import dandi.dandi.DandiApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackageClasses = DandiApplication.class)
public class FeignConfiguration {
}

package dandi.dandi.auth.adapter.out.oauth.apple.client;

import feign.Retryer;
import org.springframework.context.annotation.Bean;

public class AppleFeignConfiguration {

    @Bean
    Retryer appleFeignRetryer() {
        return new Retryer.Default(1500, 1500, 1);
    }
}

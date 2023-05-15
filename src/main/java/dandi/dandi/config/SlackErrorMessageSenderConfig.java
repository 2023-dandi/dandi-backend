package dandi.dandi.config;

import net.gpedro.integrations.slack.SlackApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SlackErrorMessageSenderConfig {

    private final String webhookUrl;

    public SlackErrorMessageSenderConfig(@Value("${slack.webhook-url}") String webhookUrl) {
        this.webhookUrl = webhookUrl;
    }

    @Bean
    public SlackApi slackApi() {
        return new SlackApi(webhookUrl);
    }
}

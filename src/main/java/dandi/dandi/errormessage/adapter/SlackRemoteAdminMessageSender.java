package dandi.dandi.errormessage.adapter;

import dandi.dandi.errormessage.application.port.out.RemoteAdminMessageSender;
import net.gpedro.integrations.slack.SlackApi;
import net.gpedro.integrations.slack.SlackMessage;
import org.springframework.stereotype.Component;

@Component
public class SlackRemoteAdminMessageSender implements RemoteAdminMessageSender {

    private final SlackApi slackApi;

    public SlackRemoteAdminMessageSender(SlackApi slackApi) {
        this.slackApi = slackApi;
    }

    @Override
    public void sendMessage(String message) {
        slackApi.call(new SlackMessage(message));
    }
}

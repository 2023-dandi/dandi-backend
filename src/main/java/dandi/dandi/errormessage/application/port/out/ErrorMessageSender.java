package dandi.dandi.errormessage.application.port.out;

import org.springframework.scheduling.annotation.Async;

@Async(value = "errorMessageSendingAsyncExecutor")
public interface ErrorMessageSender {

    void sendMessage(String message);
}

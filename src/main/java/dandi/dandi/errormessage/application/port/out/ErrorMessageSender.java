package dandi.dandi.errormessage.application.port.out;

public interface ErrorMessageSender {

    void sendMessage(String message);
}

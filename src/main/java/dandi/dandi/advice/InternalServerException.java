package dandi.dandi.advice;

public class InternalServerException extends RuntimeException {

    private static final String PUSH_NOTIFICATION_NOT_FOUND_EXCEPTION_MESSAGE_FORMAT =
            "member(%d)의 PushNotification이 존재하지 않습니다.";

    public InternalServerException(String message) {
        super(message);
    }

    public static InternalServerException pushNotificationNotFound(Long memberId) {
        String exceptionMessage = String.format(PUSH_NOTIFICATION_NOT_FOUND_EXCEPTION_MESSAGE_FORMAT, memberId);
        return new InternalServerException(exceptionMessage);
    }
}

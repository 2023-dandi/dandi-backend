package dandi.dandi.advice;

public class InternalServerException extends RuntimeException {

    private static final String PUSH_NOTIFICATION_NOT_FOUND_EXCEPTION_MESSAGE_FORMAT =
            "member(%d)의 PushNotification이 존재하지 않습니다.";
    private static final String WITHDRAWN_MEMBER_POST_EXCEPTION_MESSAGE_FORMAT =
            "탈퇴한 회원(memberId : %d)의 게시글 조회되었습니다.";

    public InternalServerException(String message) {
        super(message);
    }

    public static InternalServerException pushNotificationNotFound(Long memberId) {
        String exceptionMessage = String.format(PUSH_NOTIFICATION_NOT_FOUND_EXCEPTION_MESSAGE_FORMAT, memberId);
        return new InternalServerException(exceptionMessage);
    }

    public static InternalServerException withdrawnMemberPost(Long memberId) {
        String exceptionMessage = String.format(WITHDRAWN_MEMBER_POST_EXCEPTION_MESSAGE_FORMAT, memberId);
        return new InternalServerException(exceptionMessage);
    }
}

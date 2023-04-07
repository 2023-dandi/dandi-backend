package dandi.dandi.advice;

public class InternalServerException extends RuntimeException {

    public InternalServerException(String message) {
        super(message);
    }

    public static InternalServerException pushNotificationNotFound(Long memberId) {
        String exceptionMessage = String.format("member(%d)의 PushNotification이 존재하지 않습니다.", memberId);
        return new InternalServerException(exceptionMessage);
    }

    public static InternalServerException withdrawnMemberPost(Long memberId) {
        String exceptionMessage = String.format("탈퇴한 회원(memberId : %d)의 게시글 조회되었습니다.", memberId);
        return new InternalServerException(exceptionMessage);
    }

    public static InternalServerException withdrawnMemberComment(Long memberId) {
        String exceptionMessage = String.format("탈퇴한 회원(memberId : %d)의 댓글 조회되었습니다.", memberId);
        return new InternalServerException(exceptionMessage);
    }

    public static InternalServerException notificationCommentNotFound(Long commentId) {
        String exceptionMessage = String.format("알림에 해당하는 댓글(commentId : %d)이 존재하지 않습니다.", commentId);
        return new InternalServerException(exceptionMessage);
    }

    public static InternalServerException commentNotificationConvert(Long notificationId) {
        String exceptionMessage = String.format("댓글 알림(notificationId : %d)은 도메인 매핑 실패", notificationId);
        return new InternalServerException(exceptionMessage);
    }
}

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
}

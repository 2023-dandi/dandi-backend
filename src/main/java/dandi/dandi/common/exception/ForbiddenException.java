package dandi.dandi.common.exception;

public class ForbiddenException extends RuntimeException {

    public ForbiddenException(String message) {
        super(message);
    }

    public static ForbiddenException postDeletion() {
        return new ForbiddenException("게시글을 삭제할 수 있는 권한이 없습니다.");
    }

    public static ForbiddenException clothesDeletion() {
        return new ForbiddenException("옷을 삭제할 수 있는 권한이 없습니다.");
    }

    public static ForbiddenException clothesLookUp() {
        return new ForbiddenException("다른 사람의 옷은 조회할 수 없습니다.");
    }

    public static ForbiddenException commentDeletion() {
        return new ForbiddenException("댓글을 삭제할 수 있는 권한이 없습니다.");
    }

    public static ForbiddenException notificationCheckModification() {
        return new ForbiddenException("알림의 확인 여부를 수정할 수 있는 권한이 없습니다.");
    }
}

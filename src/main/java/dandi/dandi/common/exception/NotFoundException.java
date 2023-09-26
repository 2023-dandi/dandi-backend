package dandi.dandi.common.exception;

public class NotFoundException extends RuntimeException {

    private static final String NOT_FOUND_MESSAGE_FORMAT = "존재하지 않는 %s입니다.";

    private NotFoundException(String resourceName) {
        super(String.format(NOT_FOUND_MESSAGE_FORMAT, resourceName));
    }

    public static NotFoundException post() {
        return new NotFoundException("게시글");
    }

    public static NotFoundException clothes() {
        return new NotFoundException("옷");
    }

    public static NotFoundException comment() {
        return new NotFoundException("댓글");
    }

    public static NotFoundException notification() {
        return new NotFoundException("알림");
    }

    public static NotFoundException member() {
        return new NotFoundException("사용자");
    }

    public static NotFoundException chunkSize(String name) {
        return new NotFoundException("Chunk Size(" + name + ")");
    }
}

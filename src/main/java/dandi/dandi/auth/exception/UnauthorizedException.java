package dandi.dandi.auth.exception;

public class UnauthorizedException extends RuntimeException {

    private UnauthorizedException(String message) {
        super(message);
    }

    public static UnauthorizedException accessTokenNotFound() {
        return new UnauthorizedException("Access Token이 존재하지 않습니다.");
    }

    public static UnauthorizedException rigged() {
        return new UnauthorizedException("조작된 Access Token입니다.");
    }

    public static UnauthorizedException expired() {
        return new UnauthorizedException("만료된 Access Token입니다.");
    }

    public static UnauthorizedException notExistentMember() {
        return new UnauthorizedException("존재하지 않는 사용자의 Access Token입니다.");
    }

    public static UnauthorizedException refreshTokenNotFound() {
        return new UnauthorizedException("유효하지 않은 Refresh Token입니다.");
    }

    public static UnauthorizedException expiredRefreshToken() {
        return new UnauthorizedException("만료된 Refresh Token 입니다.");
    }
}

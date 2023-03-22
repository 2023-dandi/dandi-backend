package dandi.dandi.auth.application.port.in;

public interface AuthUseCase {

    LoginResponse getToken(LoginCommand loginCommand);

    TokenResponse refresh(String refreshToken);

    void logout(Long memberId);
}

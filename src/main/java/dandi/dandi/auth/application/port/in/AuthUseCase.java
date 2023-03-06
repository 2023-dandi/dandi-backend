package dandi.dandi.auth.application.port.in;

import dandi.dandi.auth.application.port.out.LoginResponse;
import dandi.dandi.auth.application.port.out.TokenResponse;

public interface AuthUseCase {

    LoginResponse getToken(LoginCommand loginCommand);

    TokenResponse refresh(Long memberId, String refreshToken);

    void logout(Long memberId);
}

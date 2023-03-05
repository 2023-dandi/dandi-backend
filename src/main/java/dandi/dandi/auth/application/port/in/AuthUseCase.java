package dandi.dandi.auth.application.port.in;

import dandi.dandi.auth.application.port.in.dto.LoginRequest;
import dandi.dandi.auth.application.port.out.dto.LoginResponse;
import dandi.dandi.auth.application.port.out.dto.TokenResponse;
import javax.transaction.Transactional;

public interface AuthUseCase {
    @Transactional
    LoginResponse getToken(LoginRequest loginRequest);

    @Transactional
    TokenResponse refresh(Long memberId, String refreshToken);

    @Transactional
    void logout(Long memberId);
}

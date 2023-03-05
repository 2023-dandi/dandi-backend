package dandi.dandi.auth.application;

import dandi.dandi.auth.application.port.in.AuthUseCase;
import dandi.dandi.auth.application.port.in.dto.LoginRequest;
import dandi.dandi.auth.application.port.out.RefreshTokenPersistencePort;
import dandi.dandi.auth.application.port.out.dto.LoginResponse;
import dandi.dandi.auth.application.port.out.dto.TokenResponse;
import dandi.dandi.auth.domain.OAuthClient;
import dandi.dandi.auth.domain.RefreshToken;
import dandi.dandi.auth.exception.UnauthorizedException;
import dandi.dandi.auth.infrastructure.token.JwtTokenManager;
import dandi.dandi.auth.infrastructure.token.RefreshTokenManager;
import dandi.dandi.member.application.port.out.MemberPersistencePort;
import dandi.dandi.member.application.service.MemberSignupManager;
import dandi.dandi.member.domain.Member;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class AuthService implements AuthUseCase {

    private final MemberSignupManager memberSignupManager;
    private final OAuthClient oAuthClient;
    private final MemberPersistencePort memberPersistencePort;
    private final RefreshTokenPersistencePort refreshTokenPersistencePort;
    private final RefreshTokenManager refreshTokenManager;
    private final JwtTokenManager jwtTokenManager;

    public AuthService(MemberSignupManager memberSignupManager, OAuthClient oAuthClient,
                       MemberPersistencePort memberPersistencePort,
                       RefreshTokenPersistencePort refreshTokenPersistencePort,
                       RefreshTokenManager refreshTokenManager, JwtTokenManager jwtTokenManager) {
        this.memberSignupManager = memberSignupManager;
        this.oAuthClient = oAuthClient;
        this.memberPersistencePort = memberPersistencePort;
        this.refreshTokenPersistencePort = refreshTokenPersistencePort;
        this.refreshTokenManager = refreshTokenManager;
        this.jwtTokenManager = jwtTokenManager;
    }

    @Override
    @Transactional
    public LoginResponse getToken(LoginRequest loginRequest) {
        String oAuthMemberId = oAuthClient.getOAuthMemberId(loginRequest.getIdToken());
        Optional<Member> member = memberPersistencePort.findByOAuthId(oAuthMemberId);
        if (member.isPresent()) {
            String accessToken = jwtTokenManager.generateToken(String.valueOf(member.get().getId()));
            String refreshToken = generateRefreshToken(member.get().getId());
            return LoginResponse.existingUser(accessToken, refreshToken);
        }
        return getNewMemberLoginResponse(oAuthMemberId);
    }

    private LoginResponse getNewMemberLoginResponse(String oAuthMemberId) {
        Long newMemberId = memberSignupManager.signup(oAuthMemberId);
        String token = jwtTokenManager.generateToken(String.valueOf(newMemberId));
        String refreshToken = generateRefreshToken(newMemberId);
        return LoginResponse.newUser(token, refreshToken);
    }

    private String generateRefreshToken(Long memberId) {
        RefreshToken refreshToken = refreshTokenManager.generateToken(memberId);
        refreshTokenPersistencePort.save(refreshToken);
        return refreshToken.getValue();
    }

    @Override
    @Transactional
    public TokenResponse refresh(Long memberId, String refreshToken) {
        RefreshToken refreshTokenInfo = refreshTokenPersistencePort
                .findRefreshTokenByMemberIdAndValue(memberId, refreshToken)
                .orElseThrow(UnauthorizedException::refreshTokenNotFound);
        validateExpiration(refreshTokenInfo);
        String updatedRefreshToken = refreshTokenInfo.updateRefreshToken();
        String accessToken = jwtTokenManager.generateToken(String.valueOf(memberId));
        return new TokenResponse(accessToken, updatedRefreshToken);
    }

    private static void validateExpiration(RefreshToken refreshTokenInfo) {
        if (refreshTokenInfo.isExpired()) {
            throw UnauthorizedException.expiredRefreshToken();
        }
    }

    @Override
    @Transactional
    public void logout(Long memberId) {
        refreshTokenPersistencePort.deleteByMemberId(memberId);
    }
}

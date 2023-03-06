package dandi.dandi.auth.application.service;

import dandi.dandi.auth.application.port.in.AuthUseCase;
import dandi.dandi.auth.application.port.in.LoginCommand;
import dandi.dandi.auth.application.port.out.LoginResponse;
import dandi.dandi.auth.application.port.out.TokenResponse;
import dandi.dandi.auth.application.port.out.jwt.AccessTokenManagerPort;
import dandi.dandi.auth.application.port.out.jwt.RefreshTokenManagerPort;
import dandi.dandi.auth.application.port.out.oauth.OAuthClientPort;
import dandi.dandi.auth.application.port.out.persistence.RefreshTokenPersistencePort;
import dandi.dandi.auth.domain.RefreshToken;
import dandi.dandi.auth.exception.UnauthorizedException;
import dandi.dandi.member.application.port.out.MemberPersistencePort;
import dandi.dandi.member.application.service.MemberSignupManager;
import dandi.dandi.member.domain.Member;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class AuthService implements AuthUseCase {

    private final MemberSignupManager memberSignupManager;
    private final OAuthClientPort oAuthClientPort;
    private final MemberPersistencePort memberPersistencePort;
    private final RefreshTokenPersistencePort refreshTokenPersistencePort;
    private final AccessTokenManagerPort accessTokenManagerPort;
    private final RefreshTokenManagerPort refreshTokenManagerPort;

    public AuthService(MemberSignupManager memberSignupManager, OAuthClientPort oAuthClientPort,
                       MemberPersistencePort memberPersistencePort,
                       RefreshTokenPersistencePort refreshTokenPersistencePort,
                       RefreshTokenManagerPort refreshTokenManagerPort, AccessTokenManagerPort accessTokenManagerPort) {
        this.memberSignupManager = memberSignupManager;
        this.oAuthClientPort = oAuthClientPort;
        this.memberPersistencePort = memberPersistencePort;
        this.refreshTokenPersistencePort = refreshTokenPersistencePort;
        this.refreshTokenManagerPort = refreshTokenManagerPort;
        this.accessTokenManagerPort = accessTokenManagerPort;
    }

    @Override
    @Transactional
    public LoginResponse getToken(LoginCommand loginCommand) {
        String oAuthMemberId = oAuthClientPort.getOAuthMemberId(loginCommand.getIdToken());
        Optional<Member> member = memberPersistencePort.findByOAuthId(oAuthMemberId);
        if (member.isPresent()) {
            String accessToken = accessTokenManagerPort.generateToken(String.valueOf(member.get().getId()));
            String refreshToken = generateRefreshToken(member.get().getId());
            return LoginResponse.existingUser(accessToken, refreshToken);
        }
        return getNewMemberLoginResponse(oAuthMemberId);
    }

    private LoginResponse getNewMemberLoginResponse(String oAuthMemberId) {
        Long newMemberId = memberSignupManager.signup(oAuthMemberId);
        String token = accessTokenManagerPort.generateToken(String.valueOf(newMemberId));
        String refreshToken = generateRefreshToken(newMemberId);
        return LoginResponse.newUser(token, refreshToken);
    }

    private String generateRefreshToken(Long memberId) {
        RefreshToken refreshToken = refreshTokenManagerPort.generateToken(memberId);
        refreshTokenPersistencePort.save(refreshToken);
        return refreshToken.getValue();
    }

    @Override
    @Transactional
    public TokenResponse refresh(Long memberId, String refreshTokenValue) {
        RefreshToken refreshToken = refreshTokenPersistencePort
                .findRefreshTokenByMemberIdAndValue(memberId, refreshTokenValue)
                .orElseThrow(UnauthorizedException::refreshTokenNotFound);
        validateExpiration(refreshToken);
        RefreshToken updatedRefreshToken = refreshTokenManagerPort.generateToken(memberId);
        refreshTokenPersistencePort.update(refreshToken.getId(), updatedRefreshToken);
        String accessToken = accessTokenManagerPort.generateToken(String.valueOf(memberId));
        return new TokenResponse(accessToken, updatedRefreshToken.getValue());
    }

    private void validateExpiration(RefreshToken refreshTokenInfo) {
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

package dandi.dandi.auth.application;

import dandi.dandi.auth.application.dto.LoginRequest;
import dandi.dandi.auth.application.dto.LoginResponse;
import dandi.dandi.auth.application.dto.TokenRefreshResponse;
import dandi.dandi.auth.domain.OAuthClient;
import dandi.dandi.auth.domain.RefreshToken;
import dandi.dandi.auth.domain.RefreshTokenRepository;
import dandi.dandi.auth.exception.UnauthorizedException;
import dandi.dandi.auth.infrastructure.token.JwtTokenManager;
import dandi.dandi.auth.infrastructure.token.RefreshTokenManager;
import dandi.dandi.member.domain.Member;
import dandi.dandi.member.domain.MemberRepository;
import dandi.dandi.member.domain.MemberSignupManager;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final MemberSignupManager memberSignupManager;
    private final OAuthClient oAuthClient;
    private final MemberRepository memberRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final RefreshTokenManager refreshTokenManager;
    private final JwtTokenManager jwtTokenManager;

    public AuthService(MemberSignupManager memberSignupManager, OAuthClient oAuthClient,
                       MemberRepository memberRepository, RefreshTokenRepository refreshTokenRepository,
                       RefreshTokenManager refreshTokenManager, JwtTokenManager jwtTokenManager) {
        this.memberSignupManager = memberSignupManager;
        this.oAuthClient = oAuthClient;
        this.memberRepository = memberRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.refreshTokenManager = refreshTokenManager;
        this.jwtTokenManager = jwtTokenManager;
    }

    @Transactional
    public LoginResponse getToken(LoginRequest loginRequest) {
        String oAuthMemberId = oAuthClient.getOAuthMemberId(loginRequest.getIdToken());
        Optional<Member> member = memberRepository.findByOAuthId(oAuthMemberId);
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
        refreshTokenRepository.save(refreshToken);
        return refreshToken.getValue();
    }

    @Transactional
    public TokenRefreshResponse refresh(Long memberId, String refreshToken) {
        RefreshToken refreshTokenInfo = refreshTokenRepository
                .findRefreshTokenByMemberIdAndValue(memberId, refreshToken)
                .orElseThrow(UnauthorizedException::refreshTokenNotFound);
        validateExpiration(refreshTokenInfo);
        String updatedRefreshToken = refreshTokenInfo.updateRefreshToken();
        String accessToken = jwtTokenManager.generateToken(String.valueOf(memberId));
        return new TokenRefreshResponse(accessToken, updatedRefreshToken);
    }

    private static void validateExpiration(RefreshToken refreshTokenInfo) {
        if (refreshTokenInfo.isExpired()) {
            throw UnauthorizedException.expiredRefreshToken();
        }
    }

    @Transactional
    public void logout(Long memberId) {
        refreshTokenRepository.deleteByMemberId(memberId);
    }
}

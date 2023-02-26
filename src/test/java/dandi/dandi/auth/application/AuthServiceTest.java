package dandi.dandi.auth.application;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

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
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    private static final String ID_TOKEN = "idToken";
    private static final String TOKEN = "token";
    private static final String OAUTH_MEMBER_ID = "oAuthMemberId";
    private static final String NICKNAME = "nickname";
    private static final Long NEW_MEMBER_ID = 2L;
    private static final Long EXISTING_MEMBER_ID = 1L;
    private static final String REFRESH_TOKEN = "refreshToken";

    @Mock
    private MemberRepository memberRepository;
    @Mock
    private OAuthClient oAuthClient;
    @Mock
    private JwtTokenManager jwtTokenManager;
    @Mock
    private MemberSignupManager memberSignupManager;
    @Mock
    private RefreshTokenRepository refreshTokenRepository;
    @Mock
    private RefreshTokenManager refreshTokenManager;
    @InjectMocks
    private AuthService authService;

    @DisplayName("oAuthId를 받아, 새 회원이라면 회원 가입을 시키고 토큰과 새 회원 여부를 반환한다.")
    @Test
    void getAccessToken_NewMember() {
        when(oAuthClient.getOAuthMemberId(anyString()))
                .thenReturn(OAUTH_MEMBER_ID);
        when(memberRepository.findByOAuthId(OAUTH_MEMBER_ID))
                .thenReturn(Optional.empty());
        when(memberSignupManager.signup(OAUTH_MEMBER_ID))
                .thenReturn(NEW_MEMBER_ID);
        when(jwtTokenManager.generateToken(String.valueOf(NEW_MEMBER_ID)))
                .thenReturn(TOKEN);
        when(refreshTokenManager.generateToken(NEW_MEMBER_ID))
                .thenReturn(RefreshToken.generateNewWithExpiration(NEW_MEMBER_ID, LocalDateTime.MAX));

        LoginResponse loginResponse = authService.getToken(new LoginRequest(ID_TOKEN));

        assertAll(
                () -> assertThat(loginResponse.getAccessToken()).isEqualTo(TOKEN),
                () -> assertThat(loginResponse.getRefreshToken()).isNotNull(),
                () -> assertThat(loginResponse.isNewUser()).isTrue()
        );
    }

    @DisplayName("oAuthId를 받아, 기존 회원이라면 회원 가입 없이 토큰과 새 회원 여부를 반환한다.")
    @Test
    void getAccessToken_ExistingMember() {
        when(oAuthClient.getOAuthMemberId(anyString()))
                .thenReturn(OAUTH_MEMBER_ID);
        when(memberRepository.findByOAuthId(OAUTH_MEMBER_ID))
                .thenReturn(Optional.of(Member.initial(OAUTH_MEMBER_ID, NICKNAME)));
        when(jwtTokenManager.generateToken(anyString()))
                .thenReturn(TOKEN);
        when(refreshTokenManager.generateToken(any()))
                .thenReturn(RefreshToken.generateNewWithExpiration(EXISTING_MEMBER_ID, LocalDateTime.MAX));

        LoginResponse loginResponse = authService.getToken(new LoginRequest(ID_TOKEN));

        assertAll(
                () -> assertThat(loginResponse.getAccessToken()).isEqualTo(TOKEN),
                () -> assertThat(loginResponse.getRefreshToken()).isNotNull(),
                () -> assertThat(loginResponse.isNewUser()).isFalse()
        );
    }

    @DisplayName("MemberId와 Refresh Token을 받아, 새로운 Access Token과 Refresh Token을 반환한다.")
    @Test
    void refresh() {
        when(refreshTokenRepository.findRefreshTokenByMemberIdAndValue(EXISTING_MEMBER_ID, REFRESH_TOKEN))
                .thenReturn(Optional.of(RefreshToken.generateNewWithExpiration(EXISTING_MEMBER_ID, LocalDateTime.MAX)));
        when(jwtTokenManager.generateToken(anyString()))
                .thenReturn(TOKEN);

        TokenRefreshResponse tokenRefreshResponse = authService.refresh(EXISTING_MEMBER_ID, REFRESH_TOKEN);

        assertAll(
                () -> assertThat(tokenRefreshResponse.getAccessToken()).isNotNull(),
                () -> assertThat(tokenRefreshResponse.getRefreshToken()).isNotEqualTo(REFRESH_TOKEN)
        );
    }

    @DisplayName("MemberId와 Refresh Token 값에 해당하는 Refresh Token 객체가 존재하지 않는다면 예외를 발생시킨다.")
    @Test
    void refresh_RefreshTokenNotFound() {
        Long notFountMemberId = 2L;
        String notFountRefreshToken = "notFoundRefreshToken";
        when(refreshTokenRepository.findRefreshTokenByMemberIdAndValue(any(), any()))
                .thenThrow(UnauthorizedException.refreshTokenNotFound());

        assertThatThrownBy(() -> authService.refresh(notFountMemberId, notFountRefreshToken))
                .isInstanceOf(UnauthorizedException.class)
                .hasMessage(UnauthorizedException.refreshTokenNotFound().getMessage());
    }

    @DisplayName("만료된 Refresh Token으로 새로운 Access Token과 Refresh Token을 발급받으려 하면 예외를 발생시킨다.")
    @Test
    void refresh_ExpiredRefreshToken() {
        RefreshToken expiredRefreshToken =
                RefreshToken.generateNewWithExpiration(EXISTING_MEMBER_ID, LocalDateTime.MIN);
        when(refreshTokenRepository.findRefreshTokenByMemberIdAndValue(EXISTING_MEMBER_ID, REFRESH_TOKEN))
                .thenReturn(Optional.of(expiredRefreshToken));

        assertThatThrownBy(() -> authService.refresh(EXISTING_MEMBER_ID, REFRESH_TOKEN))
                .isInstanceOf(UnauthorizedException.class)
                .hasMessage(UnauthorizedException.expiredRefreshToken().getMessage());
    }
}

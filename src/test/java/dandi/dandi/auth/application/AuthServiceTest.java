package dandi.dandi.auth.application;

import static dandi.dandi.member.MemberTestFixture.OAUTH_ID;
import static dandi.dandi.member.MemberTestFixture.TEST_MEMBER;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import dandi.dandi.auth.application.port.in.LoginCommand;
import dandi.dandi.auth.application.port.in.LoginResponse;
import dandi.dandi.auth.application.port.in.TokenResponse;
import dandi.dandi.auth.application.port.out.jwt.AccessTokenManagerPort;
import dandi.dandi.auth.application.port.out.jwt.RefreshTokenManagerPort;
import dandi.dandi.auth.application.port.out.oauth.OAuthClientPort;
import dandi.dandi.auth.application.port.out.persistence.RefreshTokenPersistencePort;
import dandi.dandi.auth.application.service.AuthService;
import dandi.dandi.auth.domain.RefreshToken;
import dandi.dandi.auth.exception.UnauthorizedException;
import dandi.dandi.member.application.port.out.MemberPersistencePort;
import dandi.dandi.member.application.service.MemberSignupManager;
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
    private static final Long NEW_MEMBER_ID = 2L;
    private static final Long EXISTING_MEMBER_ID = 1L;
    private static final Long REFRESH_TOKEN_ID = 1L;
    private static final String REFRESH_TOKEN_VALUE = "refreshToken";
    private static final String UPDATED_REFRESH_TOKEN_VALUE = "updateRefreshToken";

    @Mock
    private MemberPersistencePort memberPersistencePort;
    @Mock
    private OAuthClientPort oAuthClientPort;
    @Mock
    private AccessTokenManagerPort accessTokenManagerPort;
    @Mock
    private MemberSignupManager memberSignupManager;
    @Mock
    private RefreshTokenPersistencePort refreshTokenPersistencePort;
    @Mock
    private RefreshTokenManagerPort refreshTokenManagerPort;
    @InjectMocks
    private AuthService authService;

    @DisplayName("oAuthId??? ??????, ??? ??????????????? ?????? ????????? ????????? ????????? ??? ?????? ????????? ????????????.")
    @Test
    void getAccessToken_NewMember() {
        when(oAuthClientPort.getOAuthMemberId(anyString()))
                .thenReturn(OAUTH_ID);
        when(memberPersistencePort.findByOAuthId(OAUTH_ID))
                .thenReturn(Optional.empty());
        when(memberSignupManager.signup(OAUTH_ID))
                .thenReturn(NEW_MEMBER_ID);
        when(accessTokenManagerPort.generateToken(String.valueOf(NEW_MEMBER_ID)))
                .thenReturn(TOKEN);
        when(refreshTokenManagerPort.generateToken(NEW_MEMBER_ID))
                .thenReturn(RefreshToken.generateNewWithExpiration(NEW_MEMBER_ID, LocalDateTime.MAX));

        LoginResponse loginResponse = authService.getToken(new LoginCommand(ID_TOKEN));

        assertAll(
                () -> assertThat(loginResponse.getAccessToken()).isEqualTo(TOKEN),
                () -> assertThat(loginResponse.getRefreshToken()).isNotNull(),
                () -> assertThat(loginResponse.isNewUser()).isTrue()
        );
    }

    @DisplayName("oAuthId??? ??????, ?????? ??????????????? ?????? ?????? ?????? ????????? ??? ?????? ????????? ????????????.")
    @Test
    void getAccessToken_ExistingMember() {
        when(oAuthClientPort.getOAuthMemberId(anyString()))
                .thenReturn(OAUTH_ID);
        when(memberPersistencePort.findByOAuthId(OAUTH_ID))
                .thenReturn(Optional.of(TEST_MEMBER));
        when(accessTokenManagerPort.generateToken(anyString()))
                .thenReturn(TOKEN);
        when(refreshTokenManagerPort.generateToken(any()))
                .thenReturn(RefreshToken.generateNewWithExpiration(EXISTING_MEMBER_ID, LocalDateTime.MAX));

        LoginResponse loginResponse = authService.getToken(new LoginCommand(ID_TOKEN));

        assertAll(
                () -> assertThat(loginResponse.getAccessToken()).isEqualTo(TOKEN),
                () -> assertThat(loginResponse.getRefreshToken()).isNotNull(),
                () -> assertThat(loginResponse.isNewUser()).isFalse()
        );
    }

    @DisplayName("MemberId??? Refresh Token??? ??????, ????????? Access Token??? Refresh Token??? ????????????.")
    @Test
    void refresh() {
        RefreshToken refreshToken =
                new RefreshToken(REFRESH_TOKEN_ID, EXISTING_MEMBER_ID, LocalDateTime.MAX, REFRESH_TOKEN_VALUE);
        when(refreshTokenPersistencePort.findRefreshTokenByMemberIdAndValue(EXISTING_MEMBER_ID, REFRESH_TOKEN_VALUE))
                .thenReturn(Optional.of(refreshToken));
        RefreshToken updateRefreshToken =
                new RefreshToken(REFRESH_TOKEN_ID, EXISTING_MEMBER_ID, LocalDateTime.MAX, UPDATED_REFRESH_TOKEN_VALUE);
        when(refreshTokenManagerPort.generateToken(refreshToken.getMemberId()))
                .thenReturn(updateRefreshToken);
        when(accessTokenManagerPort.generateToken(anyString()))
                .thenReturn(TOKEN);

        TokenResponse tokenResponse = authService.refresh(EXISTING_MEMBER_ID, REFRESH_TOKEN_VALUE);

        assertAll(
                () -> assertThat(tokenResponse.getAccessToken()).isEqualTo(TOKEN),
                () -> assertThat(tokenResponse.getRefreshToken()).isEqualTo(UPDATED_REFRESH_TOKEN_VALUE),
                () -> verify(refreshTokenPersistencePort).update(refreshToken.getId(), updateRefreshToken)
        );
    }

    @DisplayName("MemberId??? Refresh Token ?????? ???????????? Refresh Token ????????? ???????????? ???????????? ????????? ???????????????.")
    @Test
    void refresh_RefreshTokenNotFound() {
        Long notFountMemberId = 2L;
        String notFountRefreshToken = "notFoundRefreshToken";
        when(refreshTokenPersistencePort.findRefreshTokenByMemberIdAndValue(any(), any()))
                .thenThrow(UnauthorizedException.refreshTokenNotFound());

        assertThatThrownBy(() -> authService.refresh(notFountMemberId, notFountRefreshToken))
                .isInstanceOf(UnauthorizedException.class)
                .hasMessage(UnauthorizedException.refreshTokenNotFound().getMessage());
    }

    @DisplayName("????????? Refresh Token?????? ????????? Access Token??? Refresh Token??? ??????????????? ?????? ????????? ???????????????.")
    @Test
    void refresh_ExpiredRefreshToken() {
        RefreshToken expiredRefreshToken =
                RefreshToken.generateNewWithExpiration(EXISTING_MEMBER_ID, LocalDateTime.MIN);
        when(refreshTokenPersistencePort.findRefreshTokenByMemberIdAndValue(EXISTING_MEMBER_ID, REFRESH_TOKEN_VALUE))
                .thenReturn(Optional.of(expiredRefreshToken));

        assertThatThrownBy(() -> authService.refresh(EXISTING_MEMBER_ID, REFRESH_TOKEN_VALUE))
                .isInstanceOf(UnauthorizedException.class)
                .hasMessage(UnauthorizedException.expiredRefreshToken().getMessage());
    }

    @DisplayName("??????????????? ??? ??? ??????.")
    @Test
    void logout() {
        authService.logout(EXISTING_MEMBER_ID);

        verify(refreshTokenPersistencePort).deleteByMemberId(EXISTING_MEMBER_ID);
    }
}

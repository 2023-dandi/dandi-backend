package dandi.dandi.auth.application;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import dandi.dandi.auth.application.dto.LoginRequest;
import dandi.dandi.auth.application.dto.LoginResponse;
import dandi.dandi.auth.domain.OAuthClient;
import dandi.dandi.auth.infrastructure.token.JwtTokenManager;
import dandi.dandi.member.domain.Member;
import dandi.dandi.member.domain.MemberRepository;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class AuthServiceTest {

    private static final String ID_TOKEN = "idToken";
    private static final String OAUTH_MEMBER_ID = "oAuthMemberId";
    private static final String TOKEN = "token";

    private final MemberRepository memberRepository = Mockito.mock(MemberRepository.class);
    private final OAuthClient oAuthClient = Mockito.mock(OAuthClient.class);
    private final JwtTokenManager jwtTokenManager = Mockito.mock(JwtTokenManager.class);
    private final AuthService authService = new AuthService(oAuthClient, memberRepository, jwtTokenManager);

    @DisplayName("oAuthId를 받아, 새 회원이라면 회원 가입을 시키고 토큰과 새 회원 여부를 반환한다.")
    @Test
    void getAccessToken_NewMember() {
        when(oAuthClient.getOAuthMemberId(anyString()))
                .thenReturn(OAUTH_MEMBER_ID);
        when(memberRepository.findByOAuthId(OAUTH_MEMBER_ID))
                .thenReturn(Optional.empty());
        when(jwtTokenManager.generateToken(anyString()))
                .thenReturn(TOKEN);
        when(memberRepository.save(any()))
                .thenReturn(new Member(OAUTH_MEMBER_ID));

        LoginResponse loginResponse = authService.getAccessToken(new LoginRequest(ID_TOKEN));

        assertAll(
                () -> assertThat(loginResponse.getToken()).isEqualTo(TOKEN),
                () -> assertThat(loginResponse.isNewUser()).isTrue()
        );
    }

    @DisplayName("oAuthId를 받아, 기존 회원이라면 회원 가입 없이 토큰과 새 회원 여부를 반환한다.")
    @Test
    void getAccessToken_ExistingMember() {
        when(oAuthClient.getOAuthMemberId(anyString()))
                .thenReturn(OAUTH_MEMBER_ID);
        when(memberRepository.findByOAuthId(OAUTH_MEMBER_ID))
                .thenReturn(Optional.of(new Member(OAUTH_MEMBER_ID)));
        when(jwtTokenManager.generateToken(anyString()))
                .thenReturn(TOKEN);

        LoginResponse loginResponse = authService.getAccessToken(new LoginRequest(ID_TOKEN));

        assertAll(
                () -> assertThat(loginResponse.getToken()).isEqualTo(TOKEN),
                () -> assertThat(loginResponse.isNewUser()).isFalse()
        );
    }
}

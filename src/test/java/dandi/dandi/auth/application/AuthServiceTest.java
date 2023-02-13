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
import dandi.dandi.member.domain.nicknamegenerator.NicknameGenerator;
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

    @Mock
    private MemberRepository memberRepository;
    @Mock
    private OAuthClient oAuthClient;
    @Mock
    private JwtTokenManager jwtTokenManager;
    @Mock
    private NicknameGenerator nicknameGenerator;
    @InjectMocks
    private AuthService authService;

    @DisplayName("oAuthId를 받아, 새 회원이라면 회원 가입을 시키고 토큰과 새 회원 여부를 반환한다.")
    @Test
    void getAccessToken_NewMember() {
        when(oAuthClient.getOAuthMemberId(anyString()))
                .thenReturn(OAUTH_MEMBER_ID);
        when(memberRepository.findByOAuthId(OAUTH_MEMBER_ID))
                .thenReturn(Optional.empty());
        when(jwtTokenManager.generateToken(anyString()))
                .thenReturn(TOKEN);
        when(nicknameGenerator.generate())
                .thenReturn(NICKNAME);
        when(memberRepository.existsMemberByNicknameValue(NICKNAME))
                .thenReturn(false);
        when(memberRepository.save(any()))
                .thenReturn(Member.initial(OAUTH_MEMBER_ID, NICKNAME));

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
                .thenReturn(Optional.of(Member.initial(OAUTH_MEMBER_ID, NICKNAME)));
        when(jwtTokenManager.generateToken(anyString()))
                .thenReturn(TOKEN);

        LoginResponse loginResponse = authService.getAccessToken(new LoginRequest(ID_TOKEN));

        assertAll(
                () -> assertThat(loginResponse.getToken()).isEqualTo(TOKEN),
                () -> assertThat(loginResponse.isNewUser()).isFalse()
        );
    }
}

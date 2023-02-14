package dandi.dandi.auth.application;

import dandi.dandi.auth.application.dto.LoginRequest;
import dandi.dandi.auth.application.dto.LoginResponse;
import dandi.dandi.auth.domain.OAuthClient;
import dandi.dandi.auth.infrastructure.token.JwtTokenManager;
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
    private final JwtTokenManager jwtTokenManager;

    public AuthService(MemberSignupManager memberSignupManager, OAuthClient oAuthClient,
                       MemberRepository memberRepository,
                       JwtTokenManager jwtTokenManager) {
        this.memberSignupManager = memberSignupManager;
        this.oAuthClient = oAuthClient;
        this.memberRepository = memberRepository;
        this.jwtTokenManager = jwtTokenManager;
    }

    @Transactional
    public LoginResponse getAccessToken(LoginRequest loginRequest) {
        String oAuthMemberId = oAuthClient.getOAuthMemberId(loginRequest.getIdToken());
        Optional<Member> member = memberRepository.findByOAuthId(oAuthMemberId);

        if (member.isPresent()) {
            String token = jwtTokenManager.generateToken(String.valueOf(member.get().getId()));
            return LoginResponse.existingUser(token);
        }
        return getNewMemberLoginResponse(oAuthMemberId);
    }

    private LoginResponse getNewMemberLoginResponse(String oAuthMemberId) {
        Long newMemberId = memberSignupManager.signup(oAuthMemberId);
        String token = jwtTokenManager.generateToken(String.valueOf(newMemberId));
        return LoginResponse.newUser(token);
    }
}

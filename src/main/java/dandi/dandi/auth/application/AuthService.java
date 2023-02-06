package dandi.dandi.auth.application;

import dandi.dandi.auth.application.dto.LoginRequest;
import dandi.dandi.auth.application.dto.LoginResponse;
import dandi.dandi.auth.domain.OAuthClient;
import dandi.dandi.auth.infrastructure.token.JwtTokenManager;
import dandi.dandi.member.domain.Member;
import dandi.dandi.member.domain.MemberRepository;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final OAuthClient oAuthClient;
    private final MemberRepository memberRepository;
    private final JwtTokenManager jwtTokenManager;

    public AuthService(OAuthClient oAuthClient, MemberRepository memberRepository, JwtTokenManager jwtTokenManager) {
        this.oAuthClient = oAuthClient;
        this.memberRepository = memberRepository;
        this.jwtTokenManager = jwtTokenManager;
    }

    @Transactional
    public LoginResponse getAccessToken(LoginRequest loginRequest) {
        String idToken = loginRequest.getIdToken();
        String oAuthMemberId = oAuthClient.getOAuthMemberId(idToken);
        Optional<Member> member = memberRepository.findByOAuthId(oAuthMemberId);
        if (member.isPresent()) {
            String memberId = String.valueOf(member.get().getId());
            return LoginResponse.existingUser(jwtTokenManager.generateToken(memberId));
        }
        return getNewMemberLoginResponse(oAuthMemberId);
    }

    private LoginResponse getNewMemberLoginResponse(String oAuthMemberId) {
        Member newMember = memberRepository.save(new Member(oAuthMemberId));
        String memberId = String.valueOf(newMember.getId());
        return LoginResponse.newUser(jwtTokenManager.generateToken(memberId));
    }
}

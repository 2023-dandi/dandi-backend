package dandi.dandi.member.domain;

import dandi.dandi.member.domain.nicknamegenerator.NicknameGenerator;
import org.springframework.stereotype.Component;

@Component
public class MemberSignupManager {

    private final MemberRepository memberRepository;
    private final NicknameGenerator nicknameGenerator;

    public MemberSignupManager(MemberRepository memberRepository, NicknameGenerator nicknameGenerator) {
        this.memberRepository = memberRepository;
        this.nicknameGenerator = nicknameGenerator;
    }

    public Long signup(String oAuthMemberId) {
        String randomNickname = nicknameGenerator.generate();
        while (memberRepository.existsMemberByNicknameValue(randomNickname)) {
            randomNickname = nicknameGenerator.generate();
        }
        Member newMember = memberRepository.save(Member.initial(oAuthMemberId, randomNickname));
        return newMember.getId();
    }
}

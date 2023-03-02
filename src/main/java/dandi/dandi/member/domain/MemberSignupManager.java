package dandi.dandi.member.domain;

import dandi.dandi.member.domain.nicknamegenerator.NicknameGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class MemberSignupManager {

    private final MemberRepository memberRepository;
    private final NicknameGenerator nicknameGenerator;
    private final String initialProfileImageUrl;

    public MemberSignupManager(MemberRepository memberRepository, NicknameGenerator nicknameGenerator,
                               @Value("${image.member-initial-profile-image-url}") String initialProfileImageUrl) {
        this.memberRepository = memberRepository;
        this.nicknameGenerator = nicknameGenerator;
        this.initialProfileImageUrl = initialProfileImageUrl;
    }

    public Long signup(String oAuthMemberId) {
        String randomNickname = nicknameGenerator.generate();
        while (memberRepository.existsMemberByNicknameValue(randomNickname)) {
            randomNickname = nicknameGenerator.generate();
        }
        Member newMember = memberRepository.save(Member.initial(oAuthMemberId, randomNickname, initialProfileImageUrl));
        return newMember.getId();
    }
}

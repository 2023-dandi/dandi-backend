package dandi.dandi.member.application.service;

import dandi.dandi.event.application.port.out.EventPort;
import dandi.dandi.member.application.port.out.MemberPersistencePort;
import dandi.dandi.member.domain.Member;
import dandi.dandi.member.domain.NewMemberCreatedEvent;
import dandi.dandi.member.domain.nicknamegenerator.NicknameGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
public class MemberSignupManager {

    private final MemberPersistencePort memberPersistencePort;
    private final NicknameGenerator nicknameGenerator;
    private final String initialProfileImageUrl;
    private final EventPort eventPort;

    public MemberSignupManager(MemberPersistencePort memberPersistencePort, NicknameGenerator nicknameGenerator,
                               @Value("${image.member-initial-profile-image-url}") String initialProfileImageUrl,
                               EventPort eventPort) {
        this.memberPersistencePort = memberPersistencePort;
        this.nicknameGenerator = nicknameGenerator;
        this.initialProfileImageUrl = initialProfileImageUrl;
        this.eventPort = eventPort;
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public Long signup(String oAuthMemberId, String pushNotificationToken) {
        String randomNickname = nicknameGenerator.generate();
        while (memberPersistencePort.existsMemberByNickname(randomNickname)) {
            randomNickname = nicknameGenerator.generate();
        }
        Member newMember = memberPersistencePort.save(
                Member.initial(oAuthMemberId, randomNickname, initialProfileImageUrl));
        eventPort.publishEvent(new NewMemberCreatedEvent(newMember.getId(), pushNotificationToken));
        return newMember.getId();
    }
}

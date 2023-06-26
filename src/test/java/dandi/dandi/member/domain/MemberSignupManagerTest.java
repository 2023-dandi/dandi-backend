package dandi.dandi.member.domain;

import static dandi.dandi.member.MemberTestFixture.MEMBER;
import static dandi.dandi.member.MemberTestFixture.NICKNAME;
import static dandi.dandi.member.MemberTestFixture.OAUTH_ID;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import dandi.dandi.event.application.port.out.EventPort;
import dandi.dandi.member.application.port.out.MemberPersistencePort;
import dandi.dandi.member.application.service.MemberSignupManager;
import dandi.dandi.member.domain.nicknamegenerator.NicknameGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MemberSignupManagerTest {

    @Mock
    private NicknameGenerator nicknameGenerator;

    @Mock
    private MemberPersistencePort memberPersistencePort;

    @Mock
    private EventPort eventPort;

    @InjectMocks
    private MemberSignupManager memberSignupManager;

    @DisplayName("oAuthMemberId를 받아 회원 가입시키고 회원 가입한 Member의 Id를 반환한다.")
    @Test
    void signup() {
        when(nicknameGenerator.generate())
                .thenReturn(NICKNAME);
        when(memberPersistencePort.existsMemberByNickname(NICKNAME))
                .thenReturn(false);
        when(memberPersistencePort.save(any(Member.class)))
                .thenReturn(MEMBER);
        String pushNotitificationToken = "asdasad";

        Long newMemberId = memberSignupManager.signup(OAUTH_ID, pushNotitificationToken);

        assertThat(newMemberId).isEqualTo(MEMBER.getId());
        verify(eventPort).publishEvent(
                new NewMemberCreatedEvent(MEMBER.getId(), pushNotitificationToken));
    }
}

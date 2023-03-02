package dandi.dandi.member.domain;

import static dandi.dandi.member.MemberTestFixture.INITIAL_PROFILE_IMAGE_URL;
import static dandi.dandi.member.MemberTestFixture.NICKNAME;
import static dandi.dandi.member.MemberTestFixture.OAUTH_ID;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import dandi.dandi.member.domain.nicknamegenerator.NicknameGenerator;
import java.lang.reflect.Field;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MemberSignupManagerTest {

    private static final long GENERATED_MEMBER_ID = 1L;

    @Mock
    private NicknameGenerator nicknameGenerator;

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private MemberSignupManager memberSignupManager;

    @DisplayName("oAuthMemberId를 받아 회원 가입시키고 회원 가입한 Member의 Id를 반환한다.")
    @Test
    void signup() throws NoSuchFieldException, IllegalAccessException {
        when(nicknameGenerator.generate())
                .thenReturn(NICKNAME);
        when(memberRepository.existsMemberByNicknameValue(NICKNAME))
                .thenReturn(false);
        when(memberRepository.save(any(Member.class)))
                .thenReturn(
                        allocateMemberId(Member.initial(OAUTH_ID, NICKNAME, INITIAL_PROFILE_IMAGE_URL)));

        Long newMemberId = memberSignupManager.signup(OAUTH_ID);

        assertThat(newMemberId).isEqualTo(GENERATED_MEMBER_ID);
    }

    private Member allocateMemberId(Member member) throws NoSuchFieldException, IllegalAccessException {
        Class<Member> memberClass = Member.class;
        Field id = memberClass.getDeclaredField("id");
        id.setAccessible(true);
        id.set(member, GENERATED_MEMBER_ID);
        return member;
    }
}

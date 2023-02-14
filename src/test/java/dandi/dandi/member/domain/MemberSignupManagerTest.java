package dandi.dandi.member.domain;

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

    private static final String MEMBER_OAUTH_ID = "oAuthMemberId";
    private static final String RANDOM_NICKNAME = "randomNickname";
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
                .thenReturn(RANDOM_NICKNAME);
        when(memberRepository.existsMemberByNicknameValue(RANDOM_NICKNAME))
                .thenReturn(false);
        when(memberRepository.save(any(Member.class)))
                .thenReturn(allocateMemberId(Member.initial(MEMBER_OAUTH_ID, RANDOM_NICKNAME)));

        Long newMemberId = memberSignupManager.signup(MEMBER_OAUTH_ID);

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

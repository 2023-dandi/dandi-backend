package dandi.dandi.member.domain;

import static dandi.dandi.member.MemberTestFixture.OAUTH_ID;
import static dandi.dandi.member.MemberTestFixture.TEST_MEMBER_JPA_ENTITY;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import dandi.dandi.member.adapter.out.persistence.MemberJpaEntity;
import dandi.dandi.member.adapter.out.persistence.MemberRepository;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @DisplayName("oAuthId를 가진 Member를 찾는다.")
    @Test
    void findByOAuthId() {
        memberRepository.save(TEST_MEMBER_JPA_ENTITY);

        Optional<MemberJpaEntity> member = memberRepository.findByOAuthId(OAUTH_ID);

        assertThat(member).isPresent();
    }

    @DisplayName("nickname을 가진 Member가 존재하는지 반환한다.")
    @ParameterizedTest
    @CsvSource({"memberNickname, true", "jklq123, false"})
    void existsMemberByNicknameValue(String nickname, boolean expected) {
        memberRepository.save(TEST_MEMBER_JPA_ENTITY);

        boolean actual = memberRepository.existsMemberByNickname(nickname);

        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("존재하는 nickname인지 반환한다.")
    @ParameterizedTest
    @CsvSource({"memberNickname, true", "asdas521, false"})
    void existsByNicknameValue(String nickname, boolean expected) {
        memberRepository.save(TEST_MEMBER_JPA_ENTITY);

        boolean actual = memberRepository.existsByNickname(nickname);

        assertThat(actual).isEqualTo(expected);
    }
}

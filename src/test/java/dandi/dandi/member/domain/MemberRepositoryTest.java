package dandi.dandi.member.domain;

import static dandi.dandi.member.MemberTestFixture.MEMBER_JPA_ENTITY;
import static dandi.dandi.member.MemberTestFixture.OAUTH_ID;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import dandi.dandi.member.adapter.out.persistence.jpa.MemberEntity;
import dandi.dandi.member.adapter.out.persistence.jpa.MemberRepository;
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
        memberRepository.save(MEMBER_JPA_ENTITY);

        Optional<MemberEntity> member = memberRepository.findByOAuthId(OAUTH_ID);

        assertThat(member).isPresent();
    }

    @DisplayName("nickname을 가진 Member가 존재하는지 반환한다.")
    @ParameterizedTest
    @CsvSource({"memberNickname, true", "jklq123, false"})
    void existsMemberByNicknameValue(String nickname, boolean expected) {
        memberRepository.save(MEMBER_JPA_ENTITY);

        boolean actual = memberRepository.existsMemberByNickname(nickname);

        assertThat(actual).isEqualTo(expected);
    }
}

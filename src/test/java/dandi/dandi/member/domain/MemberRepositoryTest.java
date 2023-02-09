package dandi.dandi.member.domain;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class MemberRepositoryTest {

    private static final String NICKNAME = "abcd123";

    @Autowired
    private MemberRepository memberRepository;

    @DisplayName("oAuthId를 가진 Member를 찾는다.")
    @Test
    void findByOAuthId() {
        String oAuthId = "oAuthId";
        memberRepository.save(new Member(oAuthId, NICKNAME));

        Optional<Member> member = memberRepository.findByOAuthId(oAuthId);

        assertThat(member).isPresent();
    }
}

package dandi.dandi.member.domain;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MemberTest {

    @DisplayName("닉네임을 변경할 수 있다.")
    @Test
    void updateNickname() {
        Member member = Member.initial("oAuthId", "nickname");
        String newNickname = "newNickname";

        member.updateNickname(newNickname);

        assertThat(member.getNickname()).isEqualTo(newNickname);
    }
}

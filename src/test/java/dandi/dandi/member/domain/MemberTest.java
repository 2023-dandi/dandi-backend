package dandi.dandi.member.domain;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

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

    @DisplayName("위치 정보를 변경할 수 있다.")
    @Test
    void updateLocation() {
        Member member = Member.initial("oAuthId", "nickname");
        double latitude = 10.0;
        double longitude = 20.0;

        member.updateLocation(latitude, longitude);

        assertAll(
                () -> assertThat(member.getLatitude()).isEqualTo(latitude),
                () -> assertThat(member.getLongitude()).isEqualTo(longitude)
        );
    }
}
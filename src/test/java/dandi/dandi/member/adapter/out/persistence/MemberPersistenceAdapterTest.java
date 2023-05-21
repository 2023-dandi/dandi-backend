package dandi.dandi.member.adapter.out.persistence;

import static dandi.dandi.member.MemberTestFixture.INITIAL_PROFILE_IMAGE_URL;
import static dandi.dandi.member.MemberTestFixture.NICKNAME;
import static dandi.dandi.member.MemberTestFixture.OAUTH_ID;
import static dandi.dandi.pushnotification.PushNotificationFixture.PUSH_NOTIFICATION_TOKEN;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import dandi.dandi.common.PersistenceAdapterTest;
import dandi.dandi.member.domain.Location;
import dandi.dandi.member.domain.Member;
import dandi.dandi.pushnotification.adapter.out.persistence.PushNotificationPersistenceAdapter;
import dandi.dandi.pushnotification.domain.PushNotification;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

class MemberPersistenceAdapterTest extends PersistenceAdapterTest {

    @Autowired
    private MemberPersistenceAdapter memberPersistenceAdapter;

    @Autowired
    private PushNotificationPersistenceAdapter pushNotificationPersistenceAdapter;

    @DisplayName("회원을 저장할 수 있다.")
    @Test
    void save() {
        Member member = Member.initial(OAUTH_ID, NICKNAME, INITIAL_PROFILE_IMAGE_URL);

        Member saved = memberPersistenceAdapter.save(member);

        assertThat(saved.getId()).isNotNull();
    }

    @DisplayName("id로 회원을 찾을 수 있다.")
    @Test
    void findById() {
        Member member = Member.initial(OAUTH_ID, NICKNAME, INITIAL_PROFILE_IMAGE_URL);
        Member saved = memberPersistenceAdapter.save(member);

        Optional<Member> actual = memberPersistenceAdapter.findById(saved.getId());

        assertThat(actual).contains(saved);
    }

    @DisplayName("OAuthId로 회원을 찾을 수 있다.")
    @Test
    void findByOAuthId() {
        Member member = Member.initial(OAUTH_ID, NICKNAME, INITIAL_PROFILE_IMAGE_URL);
        Member saved = memberPersistenceAdapter.save(member);

        Optional<Member> actual = memberPersistenceAdapter.findByOAuthId(saved.getOAuthId());

        assertThat(actual).contains(saved);
    }

    @DisplayName("닉네임이 존재하는지 반환할 수 있다.")
    @ParameterizedTest
    @CsvSource({"nickname, true", "nickname123, false"})
    void existsMemberByNickname(String nickname, boolean expected) {
        Member member = Member.initial(OAUTH_ID, "nickname", INITIAL_PROFILE_IMAGE_URL);
        memberPersistenceAdapter.save(member);

        boolean actual = memberPersistenceAdapter.existsMemberByNickname(nickname);

        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("memberId에 해당하는 닉네임을 제외하고 닉네임이 존재하는지 반환할 수 있다")
    @ParameterizedTest
    @CsvSource({"nickname, 1, false", "nickname, 2, true", "nickname123, 1, false", "nickname123, 2, false"})
    void existsMemberByNicknameExceptMine(String nickname, Long memberId, boolean expected) {
        Member member = Member.initial(OAUTH_ID, "nickname", INITIAL_PROFILE_IMAGE_URL);
        Member save = memberPersistenceAdapter.save(member);
        System.out.println(save.getId());

        entityManager.flush();
        boolean actual = memberPersistenceAdapter.existsMemberByNicknameExceptMine(memberId, nickname);

        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("프로필 이미지를 변경할 수 있다.")
    @Test
    void updateProfileImageUrl() {
        Member member = Member.initial(OAUTH_ID, "nickname", INITIAL_PROFILE_IMAGE_URL);
        Member saved = memberPersistenceAdapter.save(member);
        String newProfileImageUrl = "newProfileImageUrl";

        memberPersistenceAdapter.updateProfileImageUrl(saved.getId(), newProfileImageUrl);

        entityManager.clear();
        Member profileImageUrlUpdatedMember = memberPersistenceAdapter.findById(saved.getId()).get();
        assertThat(profileImageUrlUpdatedMember.getProfileImgUrl()).isEqualTo(newProfileImageUrl);
    }

    @DisplayName("닉네임을 변경할 수 있다.")
    @Test
    void updateNickname() {
        Member member = Member.initial(OAUTH_ID, "nickname", INITIAL_PROFILE_IMAGE_URL);
        Member saved = memberPersistenceAdapter.save(member);
        String newNickname = "newNickname";

        memberPersistenceAdapter.updateNickname(saved.getId(), newNickname);

        entityManager.clear();
        Member nicknameUpdatedMember = memberPersistenceAdapter.findById(saved.getId()).get();
        assertThat(nicknameUpdatedMember.getNickname()).isEqualTo(newNickname);
    }

    @DisplayName("주거주 위치를 변경할 수 있다.")
    @Test
    void updateLocation() {
        Member member = Member.initial(OAUTH_ID, "nickname", INITIAL_PROFILE_IMAGE_URL);
        Member saved = memberPersistenceAdapter.save(member);
        double newLatitude = 10.0;
        double newLongitude = 20.0;

        memberPersistenceAdapter.updateLocation(saved.getId(), newLatitude, newLongitude);

        entityManager.clear();
        Member locationUpdatedMember = memberPersistenceAdapter.findById(saved.getId()).get();
        assertAll(
                () -> assertThat(locationUpdatedMember.getLatitude()).isEqualTo(newLatitude),
                () -> assertThat(locationUpdatedMember.getLongitude()).isEqualTo(newLongitude)
        );
    }

    @DisplayName("id에 해당하는 사용자가 존재하는지 찾을 수 있다.")
    @ParameterizedTest
    @CsvSource({"1, true", "2, false"})
    void existsById(Long id, boolean expected) {
        Member member = Member.initial(OAUTH_ID, "nickname", INITIAL_PROFILE_IMAGE_URL);
        memberPersistenceAdapter.save(member);

        boolean actual = memberPersistenceAdapter.existsById(id);

        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("푸시 알림을 허용한 사용자를 찾을 수 있다.")
    @Test
    void findPushNotificationAllowingMember() {
        Member firstMember = memberPersistenceAdapter.save(
                Member.initial(OAUTH_ID, "nickname1", INITIAL_PROFILE_IMAGE_URL));
        Member secondMember = memberPersistenceAdapter.save(
                Member.initial(OAUTH_ID, "nickname2", INITIAL_PROFILE_IMAGE_URL));
        Member thirdMember = memberPersistenceAdapter.save(
                Member.initial(OAUTH_ID, "nickname3", INITIAL_PROFILE_IMAGE_URL));
        pushNotificationPersistenceAdapter.save(PushNotification.initial(firstMember.getId(), PUSH_NOTIFICATION_TOKEN));
        pushNotificationPersistenceAdapter.save(
                PushNotification.initial(secondMember.getId(), PUSH_NOTIFICATION_TOKEN));
        pushNotificationPersistenceAdapter.save(PushNotification.initial(thirdMember.getId(), PUSH_NOTIFICATION_TOKEN));
        PushNotification secondMemberPushNotification = pushNotificationPersistenceAdapter.findPushNotificationByMemberId(
                        firstMember.getId())
                .get();
        pushNotificationPersistenceAdapter.updatePushNotificationAllowance(secondMemberPushNotification.getId(), false);
        Pageable pageable = PageRequest.of(0, 10);

        Slice<Member> actual = memberPersistenceAdapter.findPushNotificationAllowingMember(pageable);

        assertThat(actual.getContent()).isEqualTo(List.of(secondMember, thirdMember));
    }

    @DisplayName("회원의 위치 정보를 찾을 수 있다.")
    @Test
    void findLocationById() {
        Location location = new Location(10.0, 12.0);
        Member member = memberPersistenceAdapter.save(
                new Member(null, OAUTH_ID, NICKNAME, location, INITIAL_PROFILE_IMAGE_URL));

        Optional<Location> actual = memberPersistenceAdapter.findLocationById(member.getId());

        assertAll(
                () -> assertThat(actual).isPresent(),
                () -> assertThat(actual).contains(location)
        );
    }

    @DisplayName("닉네임으로 회원 id를 찾을 수 있다.")
    @Test
    void findIdByNickname() {
        Member member = memberPersistenceAdapter.save(
                new Member(null, OAUTH_ID, NICKNAME, new Location(10, 10), INITIAL_PROFILE_IMAGE_URL));

        Optional<Long> actual = memberPersistenceAdapter.findIdByNickname(NICKNAME);

        assertThat(actual).contains(1L);
    }
}

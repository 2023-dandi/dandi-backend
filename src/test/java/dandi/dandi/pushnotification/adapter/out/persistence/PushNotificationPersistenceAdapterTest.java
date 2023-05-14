package dandi.dandi.pushnotification.adapter.out.persistence;

import static dandi.dandi.member.MemberTestFixture.MEMBER_ID;
import static dandi.dandi.pushnotification.PushNotificationFixture.PUSH_NOTIFICATION_TOKEN;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import dandi.dandi.common.PersistenceAdapterTest;
import dandi.dandi.pushnotification.domain.PushNotification;
import dandi.dandi.pushnotification.domain.PushNotificationTime;
import java.time.LocalTime;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

class PushNotificationPersistenceAdapterTest extends PersistenceAdapterTest {

    @Autowired
    private PushNotificationPersistenceAdapter pushNotificationPersistenceAdapter;

    @DisplayName("푸시 알림을 저장할 수 있다.")
    @Test
    void save() {
        PushNotification pushNotification = PushNotification.initial(MEMBER_ID, PUSH_NOTIFICATION_TOKEN);

        assertThatCode(() -> pushNotificationPersistenceAdapter.save(pushNotification))
                .doesNotThrowAnyException();
    }

    @DisplayName("회원에 따른 푸시 알림을 찾을 수 있다.")
    @ParameterizedTest
    @CsvSource({"1, true", "2, false"})
    void findPushNotificationByMemberId(Long memberId, boolean expectedPresentation) {
        PushNotification pushNotification = PushNotification.initial(MEMBER_ID, PUSH_NOTIFICATION_TOKEN);
        pushNotificationPersistenceAdapter.save(pushNotification);

        Optional<PushNotification> found = pushNotificationPersistenceAdapter.findPushNotificationByMemberId(memberId);

        assertThat(found.isPresent()).isEqualTo(expectedPresentation);
    }

    @DisplayName("푸시 알림 시간을 변경할 수 있다.")
    @Test
    void updatePushNotificationTime() {
        PushNotification pushNotification = PushNotification.initial(MEMBER_ID, PUSH_NOTIFICATION_TOKEN);
        PushNotification saved = pushNotificationPersistenceAdapter.save(pushNotification);
        LocalTime newPushNotificationTime = LocalTime.of(10, 10);

        pushNotificationPersistenceAdapter.updatePushNotificationTime(saved.getId(), newPushNotificationTime);

        entityManager.clear();
        PushNotification foundAfterPushNotificationTimeUpdate =
                pushNotificationPersistenceAdapter.findPushNotificationByMemberId(MEMBER_ID)
                        .get();
        assertThat(foundAfterPushNotificationTimeUpdate.getPushNotificationTime()).isEqualTo(newPushNotificationTime);
    }

    @DisplayName("푸시 알림 허용 여부를 변경할 수 있다.")
    @Test
    void updatePushNotificationAllowance() {
        PushNotification pushNotification = PushNotification.initial(MEMBER_ID, PUSH_NOTIFICATION_TOKEN);
        PushNotification saved = pushNotificationPersistenceAdapter.save(pushNotification);
        boolean allowance = false;

        pushNotificationPersistenceAdapter.updatePushNotificationAllowance(saved.getId(), allowance);

        entityManager.clear();
        PushNotification foundAfterPushNotificationAllowanceUpdate =
                pushNotificationPersistenceAdapter.findPushNotificationByMemberId(MEMBER_ID)
                        .get();
        assertThat(foundAfterPushNotificationAllowanceUpdate.isAllowed()).isEqualTo(allowance);
    }

    @DisplayName("푸시 알림 토큰 값을 변경할 수 있다.")
    @Test
    void updatePushNotificationToken() {
        PushNotification pushNotification = PushNotification.initial(MEMBER_ID, PUSH_NOTIFICATION_TOKEN);
        PushNotification saved = pushNotificationPersistenceAdapter.save(pushNotification);
        String newPushNotificationToken = "newPushNotificationToken";

        pushNotificationPersistenceAdapter.updatePushNotificationToken(saved.getId(), newPushNotificationToken);

        entityManager.clear();
        PushNotification foundAfterPushNotificationAllowanceUpdate =
                pushNotificationPersistenceAdapter.findPushNotificationByMemberId(MEMBER_ID)
                        .get();
        assertThat(foundAfterPushNotificationAllowanceUpdate.getToken()).isEqualTo(newPushNotificationToken);
    }

    @DisplayName("푸시 알림을 허용해둔 사용자들의 id를 조회할 수 있다.")
    @Test
    void findPushNotificationAllowedMemberIds() {
        PushNotification pushNotification1 =
                pushNotificationPersistenceAdapter.save(PushNotification.initial(1L, PUSH_NOTIFICATION_TOKEN));
        PushNotification pushNotification2 =
                pushNotificationPersistenceAdapter.save(PushNotification.initial(2L, PUSH_NOTIFICATION_TOKEN));
        PushNotification unAllowedPushNotification = new PushNotification(null, 4L, PUSH_NOTIFICATION_TOKEN,
                PushNotificationTime.initial(), false);
        PushNotification pushNotification3 = pushNotificationPersistenceAdapter.save(unAllowedPushNotification);
        Pageable pageable = PageRequest.of(0, 10);

        Slice<PushNotification> actual = pushNotificationPersistenceAdapter.findAllowedPushNotification(pageable);

        assertThat(actual.getContent()).contains(pushNotification1, pushNotification2)
                .doesNotContain(pushNotification3);
    }
}

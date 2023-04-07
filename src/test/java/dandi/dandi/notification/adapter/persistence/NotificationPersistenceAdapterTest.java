package dandi.dandi.notification.adapter.persistence;

import static dandi.dandi.member.MemberTestFixture.MEMBER_ID;
import static dandi.dandi.post.PostFixture.POST_ID;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;

import dandi.dandi.common.PersistenceAdapterTest;
import dandi.dandi.notification.domain.Notification;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class NotificationPersistenceAdapterTest extends PersistenceAdapterTest {

    @Autowired
    private NotificationPersistenceAdapter notificationPersistenceAdapter;

    @DisplayName("알림을 저장할 수 있다.")
    @Test
    void save() {
        Notification notification = Notification.postLike(MEMBER_ID, POST_ID);

        assertThatCode(() -> notificationPersistenceAdapter.save(notification))
                .doesNotThrowAnyException();
    }
}

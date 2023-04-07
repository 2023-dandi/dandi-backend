package dandi.dandi.event.notification;

import static dandi.dandi.member.MemberTestFixture.MEMBER_ID;
import static dandi.dandi.post.PostFixture.POST_ID;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class PostNotificationEventTest {

    @DisplayName("사용자에게 알려야 할 알림 이벤트인지 반환한다.")
    @ParameterizedTest
    @CsvSource({"1, false", "2, true"})
    void isNotifiable(Long publisherId, boolean expected) {
        PostNotificationEvent postNotificationEvent = PostNotificationEvent.postLike(MEMBER_ID, publisherId, POST_ID);

        boolean actual = postNotificationEvent.isNotifiable();

        assertThat(actual).isEqualTo(expected);
    }
}

package dandi.dandi.pushnotification.adapter.out.webpush.fcm;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.google.firebase.messaging.FirebaseMessaging;
import dandi.dandi.pushnotification.domain.PushNotificationSource;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FcmWebPushManagerTest {

    @Mock
    private FirebaseMessaging firebaseMessaging;

    @InjectMocks
    private FcmWebPushManager fcmWebPushManager;

    @ParameterizedTest
    @DisplayName("최대 100개 단위로 FCM 서버에 푸시할 수 있다.")
    @CsvSource({"10, 1", "200, 2", "301, 4"})
    void pushMessages(int messageSize, int expectedSendingCount) {
        List<PushNotificationSource> pushNotificationSources = generatePushNotificationSourceOfSize(messageSize);

        fcmWebPushManager.pushMessages("title", pushNotificationSources);

        verify(firebaseMessaging, times(expectedSendingCount)).sendAllAsync(any());
    }

    private List<PushNotificationSource> generatePushNotificationSourceOfSize(int messageSize) {
        return IntStream.range(0, messageSize)
                .mapToObj(ignored -> new PushNotificationSource("token", "body"))
                .collect(Collectors.toUnmodifiableList());
    }
}

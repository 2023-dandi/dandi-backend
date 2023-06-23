package dandi.dandi.notification.application.service;

import static dandi.dandi.member.MemberTestFixture.MEMBER_ID;
import static dandi.dandi.notification.domain.NotificationType.WEATHER;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import dandi.dandi.common.exception.ForbiddenException;
import dandi.dandi.common.exception.NotFoundException;
import dandi.dandi.notification.application.port.out.NotificationPersistencePort;
import dandi.dandi.notification.domain.WeatherNotification;
import java.time.LocalDate;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class NotificationCommandServiceAdapterTest {

    private static final Long NOTIFICATION_ID = 1L;

    @Mock
    private NotificationPersistencePort notificationPersistencePort;

    @InjectMocks
    private NotificationCommandServiceAdapter notificationCommandServiceAdapter;

    @DisplayName("존재하지 않는 알림의 id로 확인 여부를 true로 변경하려하면 예외를 발생시킨다.")
    @Test
    void checkNotification_NotFound() {
        when(notificationPersistencePort.findById(NOTIFICATION_ID))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> notificationCommandServiceAdapter.checkNotification(MEMBER_ID, NOTIFICATION_ID))
                .isInstanceOf(NotFoundException.class)
                .hasMessage(NotFoundException.notification().getMessage());
    }

    @DisplayName("다른 사용자의 알림 확인 여부를 변경하려하면 예외를 발생시킨다.")
    @Test
    void checkNotification_Forbidden() {
        WeatherNotification notification =
                new WeatherNotification(NOTIFICATION_ID, MEMBER_ID, WEATHER, LocalDate.now(), false, LocalDate.now());
        when(notificationPersistencePort.findById(NOTIFICATION_ID))
                .thenReturn(Optional.of(notification));
        Long anotherMemberId = 2L;

        assertThatThrownBy(() -> notificationCommandServiceAdapter.checkNotification(anotherMemberId, NOTIFICATION_ID))
                .isInstanceOf(ForbiddenException.class)
                .hasMessage(ForbiddenException.notificationCheckModification().getMessage());
    }

    @DisplayName("자신의 알림 확인 여부를 true로 변경할 수 있다.")
    @Test
    void checkNotification() {
        WeatherNotification notification =
                new WeatherNotification(NOTIFICATION_ID, MEMBER_ID, WEATHER, LocalDate.now(), false, LocalDate.now());
        when(notificationPersistencePort.findById(NOTIFICATION_ID))
                .thenReturn(Optional.of(notification));

        notificationCommandServiceAdapter.checkNotification(MEMBER_ID, NOTIFICATION_ID);

        verify(notificationPersistencePort).updateCheckTrue(NOTIFICATION_ID);
    }
}

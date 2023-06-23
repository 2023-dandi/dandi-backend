package dandi.dandi.notification.adapter.out.persistence.jpa.convertor;

import static dandi.dandi.notification.domain.NotificationType.WEATHER;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import dandi.dandi.notification.adapter.out.persistence.jpa.NotificationJpaEntity;
import dandi.dandi.notification.domain.Notification;
import dandi.dandi.notification.domain.NotificationType;
import dandi.dandi.notification.domain.WeatherNotification;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class WeatherNotificationConvertorTest {

    private final WeatherNotificationConvertor weatherNotificationConvertor = new WeatherNotificationConvertor();

    @DisplayName("날씨 타입 알림을 변환할 수 있다.")
    @ParameterizedTest
    @CsvSource({"WEATHER, true", "POST_LIKE, false", "COMMENT, false"})
    void canConvert(NotificationType type, boolean expected) {
        NotificationJpaEntity notificationJpaEntity = new NotificationJpaEntity(
                1L, null, type, true, null, null, null, null);

        boolean actual = weatherNotificationConvertor.canConvert(notificationJpaEntity);

        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("날씨 타입 알림을 Notification 객체로 변환할 수 있다.")
    @Test
    void convert() {
        Long id = 1L;
        LocalDateTime createdAt = LocalDateTime.now();
        LocalDate weatherDate = LocalDate.now();
        NotificationJpaEntity notificationJpaEntity = new NotificationJpaEntity(
                id, null, WEATHER, true, null, null, weatherDate, createdAt);

        Notification actual = weatherNotificationConvertor.convert(notificationJpaEntity);

        assertAll(
                () -> assertThat(actual).isInstanceOf(WeatherNotification.class),
                () -> assertThat(actual.getId()).isEqualTo(id),
                () -> assertThat(actual.getWeatherDate()).isEqualTo(weatherDate),
                () -> assertThat(actual.getCreatedAt()).isEqualTo(createdAt.toLocalDate())
        );
    }
}

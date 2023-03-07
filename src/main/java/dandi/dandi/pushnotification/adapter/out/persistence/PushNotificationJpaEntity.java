package dandi.dandi.pushnotification.adapter.out.persistence;

import dandi.dandi.pushnotification.domain.PushNotification;
import dandi.dandi.pushnotification.domain.PushNotificationTime;
import java.time.LocalTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "push_notification")
public class PushNotificationJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "push_notification_id")
    private Long id;

    private Long memberId;

    private LocalTime pushNotificationTime;

    private boolean allowance;

    protected PushNotificationJpaEntity() {
    }

    public PushNotificationJpaEntity(Long id, Long memberId, LocalTime pushNotificationTime, boolean allowance) {
        this.id = id;
        this.memberId = memberId;
        this.pushNotificationTime = pushNotificationTime;
        this.allowance = allowance;
    }

    public static PushNotificationJpaEntity fromPushNotification(PushNotification pushNotification) {
        return new PushNotificationJpaEntity(
                pushNotification.getId(),
                pushNotification.getMemberId(),
                pushNotification.getPushNotificationTime(),
                pushNotification.isAllowed()
        );
    }

    public PushNotification toPushNotification() {
        return new PushNotification(
                id,
                memberId,
                PushNotificationTime.from(pushNotificationTime),
                allowance
        );
    }
}

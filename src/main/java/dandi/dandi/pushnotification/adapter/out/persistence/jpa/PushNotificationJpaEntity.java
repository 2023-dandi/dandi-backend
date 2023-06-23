package dandi.dandi.pushnotification.adapter.out.persistence.jpa;

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

    private String token;

    private LocalTime pushNotificationTime;

    private boolean allowance;

    protected PushNotificationJpaEntity() {
    }

    public PushNotificationJpaEntity(Long id, Long memberId, String token, LocalTime pushNotificationTime,
                                     boolean allowance) {
        this.id = id;
        this.memberId = memberId;
        this.token = token;
        this.pushNotificationTime = pushNotificationTime;
        this.allowance = allowance;
    }

    public static PushNotificationJpaEntity fromPushNotification(PushNotification pushNotification) {
        return new PushNotificationJpaEntity(
                pushNotification.getId(),
                pushNotification.getMemberId(),
                pushNotification.getToken(),
                pushNotification.getPushNotificationTime(),
                pushNotification.isAllowed()
        );
    }

    public PushNotification toPushNotification() {
        return new PushNotification(
                id,
                memberId,
                token,
                PushNotificationTime.from(pushNotificationTime),
                allowance
        );
    }
}

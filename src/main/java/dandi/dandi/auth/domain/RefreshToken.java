package dandi.dandi.auth.domain;

import java.time.LocalDateTime;
import java.util.UUID;

public class RefreshToken {

    private Long id;

    private Long memberId;

    private LocalDateTime expired;

    private String value;

    public RefreshToken(Long id, Long memberId, LocalDateTime expired, String value) {
        this.id = id;
        this.memberId = memberId;
        this.expired = expired;
        this.value = value;
    }

    public static RefreshToken generateNewWithExpiration(Long memberId, LocalDateTime expired) {
        String refreshToken = UUID.randomUUID().toString();
        return new RefreshToken(null, memberId, expired, refreshToken);
    }

    public boolean isExpired() {
        LocalDateTime current = LocalDateTime.now();
        return expired.isBefore(current);
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getExpired() {
        return expired;
    }

    public Long getMemberId() {
        return memberId;
    }

    public String getValue() {
        return value;
    }
}

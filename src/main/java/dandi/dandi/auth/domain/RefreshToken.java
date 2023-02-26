package dandi.dandi.auth.domain;

import java.time.LocalDateTime;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "refresh_token_id")
    private Long id;

    private Long memberId;

    private LocalDateTime expired;

    private String value;

    protected RefreshToken() {
    }

    private RefreshToken(Long id, Long memberId, LocalDateTime expired, String value) {
        this.id = id;
        this.memberId = memberId;
        this.expired = expired;
        this.value = value;
    }

    public RefreshToken(Long memberId, LocalDateTime expired, String value) {
        this(null, memberId, expired, value);
    }

    public static RefreshToken generateNewWithExpiration(Long memberId, LocalDateTime expired) {
        String refreshToken = UUID.randomUUID().toString();
        return new RefreshToken(null, memberId, expired, refreshToken);
    }

    public boolean isExpired() {
        LocalDateTime current = LocalDateTime.now();
        return expired.isBefore(current);
    }

    public String updateRefreshToken() {
        String newRefreshToken = UUID.randomUUID().toString();
        this.value = newRefreshToken;
        return newRefreshToken;
    }

    public String getValue() {
        return value;
    }

    public Long getMemberId() {
        return memberId;
    }
}

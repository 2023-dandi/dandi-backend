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

    private LocalDateTime issuedAt;

    private String value;

    protected RefreshToken() {
    }

    private RefreshToken(Long id, Long memberId, LocalDateTime issuedAt, String value) {
        this.id = id;
        this.memberId = memberId;
        this.issuedAt = issuedAt;
        this.value = value;
    }

    public RefreshToken(Long memberId, LocalDateTime issuedAt, String value) {
        this(null, memberId, issuedAt, value);
    }

    public static RefreshToken generateNew(Long memberId) {
        String refreshToken = UUID.randomUUID().toString();
        return new RefreshToken(null, memberId, LocalDateTime.now(), refreshToken);
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

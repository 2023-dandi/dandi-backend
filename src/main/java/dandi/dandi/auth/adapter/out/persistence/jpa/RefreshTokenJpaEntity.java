package dandi.dandi.auth.adapter.out.persistence.jpa;

import dandi.dandi.auth.domain.RefreshToken;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "refresh_token")
public class RefreshTokenJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "refresh_token_id")
    private Long id;

    private Long memberId;

    private LocalDateTime expired;

    private String value;

    protected RefreshTokenJpaEntity() {
    }

    public RefreshTokenJpaEntity(Long id, Long memberId, LocalDateTime expired, String value) {
        this.id = id;
        this.memberId = memberId;
        this.expired = expired;
        this.value = value;
    }

    public static RefreshTokenJpaEntity fromRefreshToken(RefreshToken refreshToken) {
        return new RefreshTokenJpaEntity(
                refreshToken.getId(),
                refreshToken.getMemberId(),
                refreshToken.getExpired(),
                refreshToken.getValue()
        );
    }

    public RefreshToken toRefreshToken() {
        return new RefreshToken(
                id,
                memberId,
                expired,
                value
        );
    }

}

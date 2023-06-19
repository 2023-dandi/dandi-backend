package dandi.dandi.member.adapter.out.persistence;

import dandi.dandi.member.domain.Member;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "member")
public class MemberJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    private String oAuthId;

    @Column(nullable = false, unique = true)
    private String nickname;

    @Embedded
    private LocationJpaEntity locationJpaEntity;

    private String profileImgUrl;

    protected MemberJpaEntity() {
    }

    private MemberJpaEntity(Long id, String oAuthId, String nickname, LocationJpaEntity locationJpaEntity,
                            String profileImgUrl) {
        this.id = id;
        this.oAuthId = oAuthId;
        this.nickname = nickname;
        this.locationJpaEntity = locationJpaEntity;
        this.profileImgUrl = profileImgUrl;
    }

    public static MemberJpaEntity fromMember(Member member) {
        return new MemberJpaEntity(
                member.getId(),
                member.getOAuthId(),
                member.getNickname(),
                new LocationJpaEntity(member.getLatitude(), member.getLongitude(), member.getDistrict()),
                member.getProfileImgUrl()
        );
    }

    public String getNickname() {
        return nickname;
    }

    public Member toMember() {
        return new Member(
                id,
                oAuthId,
                nickname,
                locationJpaEntity.toLocation(),
                profileImgUrl
        );
    }
}

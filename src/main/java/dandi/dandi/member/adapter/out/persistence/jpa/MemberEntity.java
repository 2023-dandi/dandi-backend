package dandi.dandi.member.adapter.out.persistence.jpa;

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
public class MemberEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    private String oAuthId;

    @Column(nullable = false, unique = true)
    private String nickname;

    @Embedded
    private LocationEntity locationEntity;

    private String profileImgUrl;

    protected MemberEntity() {
    }

    private MemberEntity(Long id, String oAuthId, String nickname, LocationEntity locationEntity,
                         String profileImgUrl) {
        this.id = id;
        this.oAuthId = oAuthId;
        this.nickname = nickname;
        this.locationEntity = locationEntity;
        this.profileImgUrl = profileImgUrl;
    }

    public static MemberEntity fromMember(Member member) {
        return new MemberEntity(
                member.getId(),
                member.getOAuthId(),
                member.getNickname(),
                new LocationEntity(member.getLatitude(), member.getLongitude(), member.getDistrict()),
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
                locationEntity.toLocation(),
                profileImgUrl
        );
    }
}

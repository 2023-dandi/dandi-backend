package dandi.dandi.member.adapter.out.persistence;

import dandi.dandi.member.domain.Location;
import dandi.dandi.member.domain.Member;
import dandi.dandi.member.domain.Nickname;
import javax.persistence.Column;
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

    @Column(nullable = false)
    private String nickname;

    private double latitude;

    private double longitude;

    private String profileImgUrl;

    protected MemberJpaEntity() {
    }

    private MemberJpaEntity(Long id, String oAuthId, String nickname, double latitude, double longitude,
                            String profileImgUrl) {
        this.id = id;
        this.oAuthId = oAuthId;
        this.nickname = nickname;
        this.latitude = latitude;
        this.longitude = longitude;
        this.profileImgUrl = profileImgUrl;
    }

    public static MemberJpaEntity fromMember(Member member) {
        return new MemberJpaEntity(
                member.getId(),
                member.getOAuthId(),
                member.getNickname(),
                member.getLongitude(),
                member.getLatitude(),
                member.getProfileImgUrl()
        );
    }

    public Member toMember() {
        return new Member(
                id,
                oAuthId,
                Nickname.from(nickname),
                new Location(latitude, longitude),
                profileImgUrl
        );
    }
}

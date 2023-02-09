package dandi.dandi.member.domain;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    private String oAuthId;

    @Embedded
    @Column(nullable = false)
    private Nickname nickname;

    protected Member() {
    }

    private Member(Long id, String oAuthId, Nickname nickname) {
        this.id = id;
        this.oAuthId = oAuthId;
        this.nickname = nickname;
    }

    public Member(String oAuthId, String nickname) {
        this(null, oAuthId, Nickname.from(nickname));
    }

    public Long getId() {
        return id;
    }
}

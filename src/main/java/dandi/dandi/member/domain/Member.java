package dandi.dandi.member.domain;

import javax.persistence.Column;
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

    protected Member() {
    }

    private Member(Long id, String oAuthId) {
        this.id = id;
        this.oAuthId = oAuthId;
    }

    public Member(String oAuthId) {
        this(null, oAuthId);
    }

    public Long getId() {
        return id;
    }
}

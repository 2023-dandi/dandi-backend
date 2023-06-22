package dandi.dandi.member.adapter.out.persistence.jpa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "member_block")
public class MemberBlockEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_block_id")
    private Long id;

    private Long blockingMemberId;
    private Long blockedMemberId;

    protected MemberBlockEntity() {
    }

    public MemberBlockEntity(Long blockingMemberId, Long blockedMemberId) {
        this.blockingMemberId = blockingMemberId;
        this.blockedMemberId = blockedMemberId;
    }
}

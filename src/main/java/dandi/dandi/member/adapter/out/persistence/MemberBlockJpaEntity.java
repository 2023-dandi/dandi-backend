package dandi.dandi.member.adapter.out.persistence;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "member_block")
public class MemberBlockJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_block_id")
    private Long id;

    private Long blockingMemberId;
    private Long blockedMemberId;

    protected MemberBlockJpaEntity() {
    }

    public MemberBlockJpaEntity(Long blockingMemberId, Long blockedMemberId) {
        this.blockingMemberId = blockingMemberId;
        this.blockedMemberId = blockedMemberId;
    }
}

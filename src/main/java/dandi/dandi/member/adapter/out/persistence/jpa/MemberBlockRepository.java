package dandi.dandi.member.adapter.out.persistence.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberBlockRepository extends JpaRepository<MemberBlockEntity, Long> {

    boolean existsByBlockingMemberIdAndBlockedMemberId(Long blockingMemberId, Long blockedMemberId);
}

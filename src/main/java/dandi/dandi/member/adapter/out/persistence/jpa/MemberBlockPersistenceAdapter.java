package dandi.dandi.member.adapter.out.persistence.jpa;

import dandi.dandi.member.application.port.out.MemberBlockPersistencePort;
import org.springframework.stereotype.Component;

@Component
public class MemberBlockPersistenceAdapter implements MemberBlockPersistencePort {

    private final MemberBlockRepository memberBlockRepository;

    public MemberBlockPersistenceAdapter(MemberBlockRepository memberBlockRepository) {
        this.memberBlockRepository = memberBlockRepository;
    }

    @Override
    public void saveMemberBlockOf(Long blockingMemberId, Long blockedMemberId) {
        MemberBlockEntity memberBlockEntity = new MemberBlockEntity(blockingMemberId, blockedMemberId);
        memberBlockRepository.save(memberBlockEntity);
    }

    @Override
    public boolean existsByBlockingMemberIdAndBlockedMemberId(Long blockingMemberId, Long blockedMemberId) {
        return memberBlockRepository.existsByBlockingMemberIdAndBlockedMemberId(blockingMemberId, blockedMemberId);
    }
}

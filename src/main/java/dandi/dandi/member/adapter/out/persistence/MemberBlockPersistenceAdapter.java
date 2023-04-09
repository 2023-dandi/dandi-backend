package dandi.dandi.member.adapter.out.persistence;

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
        MemberBlockJpaEntity memberBlockJpaEntity = new MemberBlockJpaEntity(blockingMemberId, blockedMemberId);
        memberBlockRepository.save(memberBlockJpaEntity);
    }
}

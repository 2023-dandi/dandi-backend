package dandi.dandi.member.application.service;

import dandi.dandi.common.exception.NotFoundException;
import dandi.dandi.member.application.port.in.MemberBlockCommand;
import dandi.dandi.member.application.port.in.MemberBlockUseCasePort;
import dandi.dandi.member.application.port.out.MemberBlockPersistencePort;
import dandi.dandi.member.application.port.out.MemberPersistencePort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MemberBlockUseCaseServiceAdapter implements MemberBlockUseCasePort {

    private final MemberBlockPersistencePort memberBlockPersistencePort;
    private final MemberPersistencePort memberPersistencePort;

    public MemberBlockUseCaseServiceAdapter(MemberBlockPersistencePort memberBlockPersistencePort,
                                            MemberPersistencePort memberPersistencePort) {
        this.memberBlockPersistencePort = memberBlockPersistencePort;
        this.memberPersistencePort = memberPersistencePort;
    }

    @Override
    public void blockMember(Long memberId, MemberBlockCommand memberBlockCommand) {
        Long blockedMemberId = memberBlockCommand.getBlockerMemberId();
        validateMemberExistence(blockedMemberId);
        validateAlreadyBlocked(memberId, blockedMemberId);
        memberBlockPersistencePort.saveMemberBlockOf(memberId, blockedMemberId);
    }

    private void validateMemberExistence(Long blockedMemberId) {
        if (!memberPersistencePort.existsById(blockedMemberId)) {
            throw NotFoundException.member();
        }
    }

    private void validateAlreadyBlocked(Long memberId, Long blockedMemberId) {
        if (memberBlockPersistencePort.existsByBlockingMemberIdAndBlockedMemberId(memberId, blockedMemberId)) {
            throw new IllegalStateException("이미 차단한 사용자입니다.");
        }
    }
}

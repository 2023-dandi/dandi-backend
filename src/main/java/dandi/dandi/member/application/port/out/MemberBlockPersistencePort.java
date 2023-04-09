package dandi.dandi.member.application.port.out;

public interface MemberBlockPersistencePort {

    void saveMemberBlockOf(Long blockingMemberId, Long blockedMemberId);

    boolean existsByBlockingMemberIdAndBlockedMemberId(Long blockingMemberId, Long blockedMemberId);
}

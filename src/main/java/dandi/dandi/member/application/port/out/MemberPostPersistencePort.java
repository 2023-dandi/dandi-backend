package dandi.dandi.member.application.port.out;

public interface MemberPostPersistencePort {

    int countPostByMemberId(Long memberId);
}

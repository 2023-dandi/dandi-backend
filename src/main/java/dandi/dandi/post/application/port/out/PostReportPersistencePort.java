package dandi.dandi.post.application.port.out;

public interface PostReportPersistencePort {

    boolean existsByMemberIdAndPostId(Long memberId, Long postId);

    void savePostReportOf(Long memberId, Long postId);
}

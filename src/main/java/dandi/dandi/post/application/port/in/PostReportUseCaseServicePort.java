package dandi.dandi.post.application.port.in;

public interface PostReportUseCaseServicePort {

    void reportPost(Long memberId, Long postId);
}

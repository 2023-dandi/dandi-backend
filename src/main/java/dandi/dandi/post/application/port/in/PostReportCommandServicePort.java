package dandi.dandi.post.application.port.in;

public interface PostReportCommandServicePort {

    void reportPost(Long memberId, Long postId);
}

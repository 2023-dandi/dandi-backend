package dandi.dandi.postreport.application.port.in;

public interface PostReportUseCase {

    void reportPost(Long memberId, Long postId);
}

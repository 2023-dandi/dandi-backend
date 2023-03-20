package dandi.dandi.postlike.application.port.in;

public interface PostLikeUseCase {

    void reverseLike(Long memberId, Long postId);
}

package dandi.dandi.postlike.application.port.in;

public interface PostLikeCommandServicePort {

    void reverseLike(Long memberId, Long postId);
}

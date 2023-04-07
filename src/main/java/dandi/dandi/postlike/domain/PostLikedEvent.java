package dandi.dandi.postlike.domain;

public class PostLikedEvent {

    private final Long targetMemberId;
    private final Long postId;

    public PostLikedEvent(Long targetMemberId, Long postId) {
        this.targetMemberId = targetMemberId;
        this.postId = postId;
    }

    public Long getTargetMemberId() {
        return targetMemberId;
    }

    public Long getPostId() {
        return postId;
    }
}

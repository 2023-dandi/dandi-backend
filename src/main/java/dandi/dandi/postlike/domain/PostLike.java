package dandi.dandi.postlike.domain;

import java.util.Objects;

public class PostLike {

    private final Long memberId;
    private final Long postId;

    public PostLike(Long memberId, Long postId) {
        this.memberId = memberId;
        this.postId = postId;
    }

    public static PostLike initial(Long memberId, Long postId) {
        return new PostLike(memberId, postId);
    }

    public Long getMemberId() {
        return memberId;
    }

    public Long getPostId() {
        return postId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PostLike)) return false;
        PostLike postLike = (PostLike) o;
        return Objects.equals(memberId, postLike.memberId) && Objects.equals(postId, postLike.postId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(memberId, postId);
    }
}

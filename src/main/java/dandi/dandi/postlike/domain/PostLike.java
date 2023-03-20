package dandi.dandi.postlike.domain;

import java.util.Objects;

public class PostLike {

    private final Long id;
    private final Long memberId;
    private final Long postId;

    public PostLike(Long id, Long memberId, Long postId) {
        this.id = id;
        this.memberId = memberId;
        this.postId = postId;
    }

    public static PostLike initial(Long memberId, Long postId) {
        return new PostLike(null, memberId, postId);
    }

    public Long getId() {
        return id;
    }

    public Long getMemberId() {
        return memberId;
    }

    public Long getPostId() {
        return postId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PostLike)) {
            return false;
        }
        PostLike postLike = (PostLike) o;
        return Objects.equals(id, postLike.id) && Objects.equals(memberId, postLike.memberId)
                && Objects.equals(postId, postLike.postId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, memberId, postId);
    }
}

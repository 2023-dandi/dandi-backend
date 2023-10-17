package dandi.dandi.postlike.adapter.out.persistence.jpa;

import dandi.dandi.postlike.domain.PostLike;

import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "post_like")
public class PostLikeJpaEntity {

    @EmbeddedId
    private PostLikePrimaryKey postLikeKey;

    protected PostLikeJpaEntity() {
    }

    public PostLikeJpaEntity(PostLikePrimaryKey postLikeKey) {
        this.postLikeKey = postLikeKey;
    }

    public static PostLikeJpaEntity of(Long postId, Long memberId) {
        return new PostLikeJpaEntity(new PostLikePrimaryKey(postId, memberId));
    }

    public Long getMemberId() {
        return postLikeKey.getMemberId();
    }

    public PostLike toPostLike() {
        return new PostLike(postLikeKey.getMemberId(), postLikeKey.getPostId());
    }

    @Embeddable
    static class PostLikePrimaryKey implements Serializable {


        private Long postId;
        private Long memberId;

        protected PostLikePrimaryKey() {
        }

        private PostLikePrimaryKey(Long postId, Long memberId) {
            this.postId = postId;
            this.memberId = memberId;
        }

        public Long getPostId() {
            return postId;
        }

        public Long getMemberId() {
            return memberId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof PostLikePrimaryKey)) return false;
            PostLikePrimaryKey that = (PostLikePrimaryKey) o;
            return Objects.equals(postId, that.postId) && Objects.equals(memberId, that.memberId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(postId, memberId);
        }
    }
}

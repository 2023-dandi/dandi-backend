package dandi.dandi.postlike.adapter.out.persistence.jpa;

import dandi.dandi.post.adapter.out.persistence.jpa.PostJpaEntity;
import dandi.dandi.postlike.domain.PostLike;

import javax.persistence.*;
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

    public static PostLikeJpaEntity of(PostJpaEntity post, Long memberId) {
        return new PostLikeJpaEntity(new PostLikePrimaryKey(post, memberId));
    }

    public Long getMemberId() {
        return postLikeKey.memberId;
    }

    public PostLike toPostLike() {
        return new PostLike(postLikeKey.memberId, postLikeKey.postJpaEntity.getId());
    }

    @Embeddable
    static class PostLikePrimaryKey implements Serializable {

        @ManyToOne
        @JoinColumn(name = "post_id")
        private PostJpaEntity postJpaEntity;
        private Long memberId;

        protected PostLikePrimaryKey() {
        }

        private PostLikePrimaryKey(PostJpaEntity postJpaEntity, Long memberId) {
            this.postJpaEntity = postJpaEntity;
            this.memberId = memberId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof PostLikePrimaryKey)) return false;
            PostLikePrimaryKey that = (PostLikePrimaryKey) o;
            return Objects.equals(postJpaEntity.getId(), that.postJpaEntity.getId()) && Objects.equals(memberId, that.memberId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(postJpaEntity.getId(), memberId);
        }
    }
}

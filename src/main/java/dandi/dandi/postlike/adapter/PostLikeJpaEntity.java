package dandi.dandi.postlike.adapter;

import dandi.dandi.postlike.domain.PostLike;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "post_like")
public class PostLikeJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_like_id")
    private Long id;

    private Long postId;

    private Long memberId;


    protected PostLikeJpaEntity() {
    }

    public PostLikeJpaEntity(Long id, Long postId, Long memberId) {
        this.id = id;
        this.postId = postId;
        this.memberId = memberId;
    }

    public static PostLikeJpaEntity fromPostLike(PostLike postLike) {
        return new PostLikeJpaEntity(
                postLike.getId(),
                postLike.getPostId(),
                postLike.getMemberId()
        );
    }

    public PostLike toPostLike() {
        return new PostLike(
                id,
                postId,
                memberId
        );
    }
}

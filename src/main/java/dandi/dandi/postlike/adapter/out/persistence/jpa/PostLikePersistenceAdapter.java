package dandi.dandi.postlike.adapter.out.persistence.jpa;

import dandi.dandi.advice.InternalServerException;
import dandi.dandi.post.adapter.out.persistence.jpa.PostJpaEntity;
import dandi.dandi.post.adapter.out.persistence.jpa.PostRepository;
import dandi.dandi.postlike.application.port.out.PostLikePersistencePort;
import dandi.dandi.postlike.domain.PostLike;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class PostLikePersistenceAdapter implements PostLikePersistencePort {

    private final PostLikeRepository postLikeRepository;
    private final PostRepository postRepository;

    public PostLikePersistenceAdapter(PostLikeRepository postLikeRepository, PostRepository postRepository) {
        this.postLikeRepository = postLikeRepository;
        this.postRepository = postRepository;
    }

    @Override
    public Optional<PostLike> findByMemberIdAndPostId(Long memberId, Long postId) {
        return postLikeRepository.findByMemberIdAndPostId(memberId, postId)
                .map(PostLikeJpaEntity::toPostLike);
    }

    @Override
    public void save(PostLike postLike) {
        PostJpaEntity postJpaEntity = postRepository.findById(postLike.getPostId())
                .orElseThrow(() -> InternalServerException.uncheckPostExistenceBeforePostLikeSave(postLike.getPostId()));
        PostLikeJpaEntity postLikeJpaEntity = PostLikeJpaEntity.of(postJpaEntity, postJpaEntity.getMemberId());
        postLikeRepository.save(postLikeJpaEntity);
    }

    @Override
    public void deleteByPostIdAndMemberId(Long postId, Long memberId) {
        postLikeRepository.deleteByPostIdAndMemberId(postId, memberId);
    }

    @Override
    public boolean existsByPostIdAndMemberId(Long memberId, Long postId) {
        return postLikeRepository.findByMemberIdAndPostId(memberId, postId)
                .isPresent();
    }
}

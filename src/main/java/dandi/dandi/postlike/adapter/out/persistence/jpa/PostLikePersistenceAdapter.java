package dandi.dandi.postlike.adapter.out.persistence.jpa;

import dandi.dandi.postlike.application.port.out.PostLikePersistencePort;
import dandi.dandi.postlike.domain.PostLike;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class PostLikePersistenceAdapter implements PostLikePersistencePort {

    private final PostLikeRepository postLikeRepository;

    public PostLikePersistenceAdapter(PostLikeRepository postLikeRepository) {
        this.postLikeRepository = postLikeRepository;
    }

    @Override
    public Optional<PostLike> findByMemberIdAndPostId(Long memberId, Long postId) {
        return postLikeRepository.findByMemberIdAndPostId(memberId, postId)
                .map(PostLikeJpaEntity::toPostLike);
    }

    @Override
    public void save(PostLike postLike) {
        PostLikeJpaEntity postLikeJpaEntity = PostLikeJpaEntity.of(postLike.getPostId(), postLike.getMemberId());
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

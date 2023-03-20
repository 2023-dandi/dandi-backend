package dandi.dandi.postlike.adapter;

import dandi.dandi.postlike.application.port.out.PostLikePersistencePort;
import dandi.dandi.postlike.domain.PostLike;
import java.util.Optional;
import org.springframework.stereotype.Component;

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
        PostLikeJpaEntity postLikeJpaEntity = PostLikeJpaEntity.fromPostLike(postLike);
        postLikeRepository.save(postLikeJpaEntity);
    }

    @Override
    public void deleteById(Long id) {
        postLikeRepository.deleteById(id);
    }
}

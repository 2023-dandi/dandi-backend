package dandi.dandi.postlike.application.service;

import dandi.dandi.common.exception.NotFoundException;
import dandi.dandi.post.application.port.out.PostPersistencePort;
import dandi.dandi.postlike.application.port.in.PostLikeUseCase;
import dandi.dandi.postlike.application.port.out.PostLikePersistencePort;
import dandi.dandi.postlike.domain.PostLike;
import org.springframework.stereotype.Service;

@Service
public class PostLikeService implements PostLikeUseCase {

    private final PostPersistencePort postPersistencePort;
    private final PostLikePersistencePort postLikePersistencePort;

    public PostLikeService(PostPersistencePort postPersistencePort, PostLikePersistencePort postLikePersistencePort) {
        this.postPersistencePort = postPersistencePort;
        this.postLikePersistencePort = postLikePersistencePort;
    }

    @Override
    public void reverseLike(Long memberId, Long postId) {
        validatePostNotFound(postId);
        postLikePersistencePort.findByMemberIdAndPostId(memberId, postId)
                .ifPresentOrElse(
                        pl -> postLikePersistencePort.deleteById(pl.getId()),
                        () -> registerNewPostLike(memberId, postId)
                );
    }

    private void validatePostNotFound(Long postId) {
        if (!postPersistencePort.existsById(postId)) {
            throw NotFoundException.post();
        }
    }

    private void registerNewPostLike(Long memberId, Long postId) {
        PostLike initial = PostLike.initial(memberId, postId);
        postLikePersistencePort.save(initial);
    }
}

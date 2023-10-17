package dandi.dandi.postlike.application.service;

import dandi.dandi.common.exception.NotFoundException;
import dandi.dandi.event.application.port.out.EventPort;
import dandi.dandi.post.application.port.out.PostPersistencePort;
import dandi.dandi.post.domain.Post;
import dandi.dandi.postlike.application.port.in.PostLikeCommandServicePort;
import dandi.dandi.postlike.application.port.out.PostLikePersistencePort;
import dandi.dandi.postlike.domain.PostLike;
import dandi.dandi.postlike.domain.PostLikedEvent;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PostLikeCommandServiceAdapter implements PostLikeCommandServicePort {

    private final PostPersistencePort postPersistencePort;
    private final PostLikePersistencePort postLikePersistencePort;
    private final EventPort eventPort;

    public PostLikeCommandServiceAdapter(PostPersistencePort postPersistencePort,
                                         PostLikePersistencePort postLikePersistencePort, EventPort eventPort) {
        this.postPersistencePort = postPersistencePort;
        this.postLikePersistencePort = postLikePersistencePort;
        this.eventPort = eventPort;
    }

    @Override
    public void reverseLike(Long memberId, Long postId) {
        Post post = postPersistencePort.findById(postId)
                .orElseThrow(NotFoundException::post);
        postLikePersistencePort.findByMemberIdAndPostId(memberId, postId)
                .ifPresentOrElse(
                        postLike -> postLikePersistencePort.deleteByPostIdAndMemberId(postId, memberId),
                        () -> registerNewPostLike(memberId, post)
                );
    }

    private void registerNewPostLike(Long memberId, Post post) {
        PostLike initial = PostLike.initial(memberId, post.getId());
        postLikePersistencePort.save(initial);
        publishPostLikeEventIfNotifiable(memberId, post);
    }

    private void publishPostLikeEventIfNotifiable(Long memberId, Post post) {
        if (!post.isWrittenBy(memberId)) {
            eventPort.publishEvent(new PostLikedEvent(post.getWriterId(), post.getId()));
        }
    }
}

package dandi.dandi.post.application.port.out;

import dandi.dandi.post.domain.Post;
import java.util.Optional;

public interface PostPersistencePort {

    Long save(Post post, Long memberId);

    Optional<Post> findById(Long postId);
}

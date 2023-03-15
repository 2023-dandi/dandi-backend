package dandi.dandi.post.application.port.out;

import dandi.dandi.member.domain.Member;
import dandi.dandi.post.domain.Post;
import java.util.Optional;

public interface PostPersistencePort {

    Long save(Post post, Member member);

    Optional<Post> findById(Long postId);
}

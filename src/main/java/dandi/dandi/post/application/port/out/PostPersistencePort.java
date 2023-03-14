package dandi.dandi.post.application.port.out;

import dandi.dandi.post.domain.Post;

public interface PostPersistencePort {

    Long save(Post post);

    Long update();
}

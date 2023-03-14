package dandi.dandi.post.adapter.out;

import dandi.dandi.post.application.port.out.PostPersistencePort;
import dandi.dandi.post.domain.Post;
import org.springframework.stereotype.Component;

@Component
public class PostPersistenceAdapter implements PostPersistencePort {

    private final PostRepository postRepository;

    public PostPersistenceAdapter(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Override
    public Long save(Post post) {
        PostJpaEntity postJpaEntity = PostJpaEntity.fromPost(post);
        return postRepository.save(postJpaEntity)
                .toPost()
                .getId();
    }

    @Override
    public Long update() {
        return null;
    }
}

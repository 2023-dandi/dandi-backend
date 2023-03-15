package dandi.dandi.post.adapter.out;

import dandi.dandi.member.adapter.out.persistence.MemberJpaEntity;
import dandi.dandi.member.domain.Member;
import dandi.dandi.post.application.port.out.PostPersistencePort;
import dandi.dandi.post.domain.Post;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
public class
PostPersistenceAdapter implements PostPersistencePort {

    private final PostRepository postRepository;

    public PostPersistenceAdapter(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Override
    public Long save(Post post, Member member) {
        MemberJpaEntity memberJpaEntity = MemberJpaEntity.fromMember(member);
        PostJpaEntity postJpaEntity = PostJpaEntity.fromPostAndMemberJpaEntity(post, memberJpaEntity);
        return postRepository.save(postJpaEntity)
                .toPost()
                .getId();
    }

    @Override
    public Optional<Post> findById(Long postId) {
        return postRepository.findById(postId)
                .map(PostJpaEntity::toPost);
    }
}

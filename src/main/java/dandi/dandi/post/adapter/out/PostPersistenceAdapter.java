package dandi.dandi.post.adapter.out;

import dandi.dandi.member.adapter.out.persistence.MemberRepository;
import dandi.dandi.post.application.port.out.PostPersistencePort;
import dandi.dandi.post.domain.Post;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
public class PostPersistenceAdapter implements PostPersistencePort {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;

    public PostPersistenceAdapter(PostRepository postRepository, MemberRepository memberRepository) {
        this.postRepository = postRepository;
        this.memberRepository = memberRepository;
    }

    @Override
    public Long save(Post post, Long memberId) {
        PostJpaEntity postJpaEntity = PostJpaEntity.fromPostAndMemberId(post, memberId);
        return postRepository.save(postJpaEntity)
                .getId();
    }

    @Override
    public Optional<Post> findById(Long postId) {
        return postRepository.findByIdWithAdditionalFeelingIndicesJpaEntities(postId)
                .map(this::toPost);
    }

    @Override
    public boolean existsById(Long postId) {
        return postRepository.existsById(postId);
    }

    private Post toPost(PostJpaEntity postJpaEntity) {
        Long postWriterId = postJpaEntity.getMemberId();
        String postWriterNickname = memberRepository.findNicknameById(postWriterId);
        return postJpaEntity.toPost(postWriterNickname);
    }
}

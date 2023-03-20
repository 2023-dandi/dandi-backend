package dandi.dandi.post.adapter.out;

import dandi.dandi.advice.InternalServerException;
import dandi.dandi.member.adapter.out.persistence.MemberJpaEntity;
import dandi.dandi.member.adapter.out.persistence.MemberRepository;
import dandi.dandi.member.domain.Member;
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
        MemberJpaEntity postWriter = memberRepository.findById(postJpaEntity.getMemberId())
                .orElseThrow(() -> InternalServerException.withdrawnMemberPost(
                        postJpaEntity.getMemberId(), postJpaEntity.getId()));
        Member member = postWriter.toMember();
        return postJpaEntity.toPost(member);
    }
}

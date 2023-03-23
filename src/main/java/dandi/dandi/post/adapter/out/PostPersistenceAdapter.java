package dandi.dandi.post.adapter.out;

import dandi.dandi.advice.InternalServerException;
import dandi.dandi.member.adapter.out.persistence.MemberRepository;
import dandi.dandi.member.domain.Member;
import dandi.dandi.post.application.port.out.PostPersistencePort;
import dandi.dandi.post.domain.Post;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
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
                .map(postJpaEntity -> postJpaEntity.toPost(findMember(postJpaEntity)));
    }

    @Override
    public boolean existsById(Long postId) {
        return postRepository.existsById(postId);
    }

    @Override
    public void deleteById(Long postId) {
        postRepository.deleteById(postId);
    }

    @Override
    public Slice<Post> findByMemberId(Long memberId, Pageable pageable) {
        Slice<PostJpaEntity> postJpaEntities = postRepository.findAllByMemberId(memberId, pageable);
        Member member = findMember(postJpaEntities.getContent().get(0));
        List<Post> posts = postJpaEntities.stream()
                .map(postJpaEntity -> postJpaEntity.toPost(member))
                .collect(Collectors.toUnmodifiableList());
        return new SliceImpl<>(posts, pageable, postJpaEntities.hasNext());
    }

    private Member findMember(PostJpaEntity postJpaEntity) {
        return memberRepository.findById(postJpaEntity.getMemberId())
                .orElseThrow(() -> InternalServerException.withdrawnMemberPost(postJpaEntity.getMemberId()))
                .toMember();
    }
}

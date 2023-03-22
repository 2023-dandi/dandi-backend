package dandi.dandi.post.adapter.out;

import static dandi.dandi.post.adapter.out.QPostJpaEntity.postJpaEntity;

import com.querydsl.jpa.impl.JPAQueryFactory;
import dandi.dandi.advice.InternalServerException;
import dandi.dandi.member.adapter.out.persistence.MemberJpaEntity;
import dandi.dandi.member.adapter.out.persistence.MemberRepository;
import dandi.dandi.member.domain.Member;
import dandi.dandi.post.application.port.in.MyPostResponse;
import dandi.dandi.post.application.port.out.PostPersistencePort;
import dandi.dandi.post.domain.Post;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import org.springframework.stereotype.Component;


@Component
public class PostPersistenceAdapter implements PostPersistencePort {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final EntityManager entityManager;

    public PostPersistenceAdapter(PostRepository postRepository, MemberRepository memberRepository,
                                  EntityManager entityManager) {
        this.postRepository = postRepository;
        this.memberRepository = memberRepository;
        this.entityManager = entityManager;
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

    @Override
    public void deleteById(Long postId) {
        postRepository.deleteById(postId);
    }

    @Override
    public List<MyPostResponse> findPostIdAndPostImageUrlByMemberId(Long memberId) {
        JPAQueryFactory jpaQueryFactory = new JPAQueryFactory(entityManager);
        return jpaQueryFactory.from(postJpaEntity)
                .select(postJpaEntity.id, postJpaEntity.postImageUrl)
                .where(postJpaEntity.memberId.eq(memberId))
                .fetch()
                .stream()
                .map(p -> new MyPostResponse(p.get(postJpaEntity.id), p.get(postJpaEntity.postImageUrl)))
                .collect(Collectors.toUnmodifiableList());
    }
}

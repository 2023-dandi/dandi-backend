package dandi.dandi.post.adapter.out;

import dandi.dandi.advice.InternalServerException;
import dandi.dandi.member.adapter.out.persistence.MemberRepository;
import dandi.dandi.member.domain.Member;
import dandi.dandi.post.application.port.out.PostPersistencePort;
import dandi.dandi.post.domain.Post;
import dandi.dandi.postlike.adapter.PostLikeJpaEntity;
import dandi.dandi.postlike.adapter.PostLikeRepository;
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
    private final PostLikeRepository postLikeRepository;

    public PostPersistenceAdapter(PostRepository postRepository, MemberRepository memberRepository,
                                  PostLikeRepository postLikeRepository) {
        this.postRepository = postRepository;
        this.memberRepository = memberRepository;
        this.postLikeRepository = postLikeRepository;
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

    @Override
    public void deleteById(Long postId) {
        postRepository.deleteById(postId);
    }

    @Override
    public Slice<Post> findByMemberId(Long memberId, Pageable pageable) {
        Slice<PostJpaEntity> postJpaEntities = postRepository.findAllByMemberId(memberId, pageable);
        List<Post> posts = postJpaEntities.stream()
                .map(this::toPost)
                .collect(Collectors.toUnmodifiableList());
        return new SliceImpl<>(posts, pageable, postJpaEntities.hasNext());
    }

    private Member findMember(PostJpaEntity postJpaEntity) {
        return memberRepository.findById(postJpaEntity.getMemberId())
                .orElseThrow(() -> InternalServerException.withdrawnMemberPost(postJpaEntity.getMemberId()))
                .toMember();
    }

    private Post toPost(PostJpaEntity postJpaEntity) {
        Member member = findMember(postJpaEntity);
        List<Long> postLikingMemberIds = findPostLikingMemberIds(postJpaEntity);
        return postJpaEntity.toPost(member, postLikingMemberIds);
    }

    private List<Long> findPostLikingMemberIds(PostJpaEntity postJpaEntity) {
        return postLikeRepository.findByPostId(postJpaEntity.getId())
                .stream()
                .map(PostLikeJpaEntity::getMemberId)
                .collect(Collectors.toUnmodifiableList());
    }
}

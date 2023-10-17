package dandi.dandi.post.adapter.out.persistence.jpa;

import dandi.dandi.advice.InternalServerException;
import dandi.dandi.member.adapter.out.persistence.jpa.MemberRepository;
import dandi.dandi.member.domain.Member;
import dandi.dandi.post.application.port.out.PostPersistencePort;
import dandi.dandi.post.domain.Post;
import dandi.dandi.post.domain.TemperatureSearchCondition;
import dandi.dandi.postlike.adapter.out.persistence.jpa.PostLikeJpaEntity;
import dandi.dandi.postlike.adapter.out.persistence.jpa.PostLikeRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


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
        PostJpaEntity postJpaEntity = PostJpaEntity.initial(post, memberId);
        return postRepository.save(postJpaEntity)
                .getId();
    }

    @Override
    public Optional<Post> findById(Long postId) {
        return postRepository.findById(postId)
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

    @Override
    public Slice<Post> findByTemperature(Long memberId, TemperatureSearchCondition temperatureSearchCondition,
                                         Pageable pageable) {
        Slice<PostJpaEntity> postJpaEntities = postRepository.findByTemperature(
                memberId,
                temperatureSearchCondition.getMinTemperatureMinSearchCondition(),
                temperatureSearchCondition.getMinTemperatureMaxSearchCondition(),
                temperatureSearchCondition.getMaxTemperatureMinSearchCondition(),
                temperatureSearchCondition.getMaxTemperatureMaxSearchCondition(),
                pageable);

        List<Post> posts = postJpaEntities.stream()
                .map(this::toPost)
                .collect(Collectors.toUnmodifiableList());
        return new SliceImpl<>(posts, pageable, postJpaEntities.hasNext());
    }

    @Override
    public Slice<Post> findByMemberIdAndTemperature(Long memberId,
                                                    TemperatureSearchCondition temperatureSearchCondition,
                                                    Pageable pageable) {
        Slice<PostJpaEntity> myPostsByTemperature = postRepository.findByMemberIdAndTemperature(
                memberId,
                temperatureSearchCondition.getMinTemperatureMinSearchCondition(),
                temperatureSearchCondition.getMinTemperatureMaxSearchCondition(),
                temperatureSearchCondition.getMaxTemperatureMinSearchCondition(),
                temperatureSearchCondition.getMaxTemperatureMaxSearchCondition(),
                pageable);

        Member member = findMemberFromOnlyMemberWritingPosts(myPostsByTemperature.getContent());
        List<Post> posts = myPostsByTemperature.stream()
                .map(postJpaEntity -> postJpaEntity.toPost(member, findPostLikingMemberIds(postJpaEntity)))
                .collect(Collectors.toUnmodifiableList());
        return new SliceImpl<>(posts, pageable, myPostsByTemperature.hasNext());
    }

    private Member findMemberFromOnlyMemberWritingPosts(List<PostJpaEntity> posts) {
        if (posts.isEmpty()) {
            return null;
        }
        return findMember(posts.get(0));
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

    @Override
    public Slice<Post> findLikedPosts(Long memberId, Pageable pageable) {
        Slice<PostJpaEntity> likedPostJpaEntities = postRepository.findLikedPostsByMemberId(memberId, pageable);
        List<Post> posts = likedPostJpaEntities.stream()
                .map(this::toPost)
                .collect(Collectors.toUnmodifiableList());
        return new SliceImpl<>(posts, pageable, likedPostJpaEntities.hasNext());
    }
}

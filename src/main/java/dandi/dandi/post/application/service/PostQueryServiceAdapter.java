package dandi.dandi.post.application.service;

import dandi.dandi.common.exception.NotFoundException;
import dandi.dandi.image.aspect.ImageUrlInclusion;
import dandi.dandi.post.application.port.in.FeedResponse;
import dandi.dandi.post.application.port.in.LikedPostResponse;
import dandi.dandi.post.application.port.in.LikedPostResponses;
import dandi.dandi.post.application.port.in.MyPostByTemperatureResponse;
import dandi.dandi.post.application.port.in.MyPostResponse;
import dandi.dandi.post.application.port.in.MyPostResponses;
import dandi.dandi.post.application.port.in.MyPostsByTemperatureResponses;
import dandi.dandi.post.application.port.in.PostDetailResponse;
import dandi.dandi.post.application.port.in.PostQueryServicePort;
import dandi.dandi.post.application.port.in.PostResponse;
import dandi.dandi.post.application.port.in.PostWriterResponse;
import dandi.dandi.post.application.port.out.PostPersistencePort;
import dandi.dandi.post.domain.Post;
import dandi.dandi.post.domain.TemperatureSearchCondition;
import dandi.dandi.post.domain.Temperatures;
import dandi.dandi.postlike.application.port.out.PostLikePersistencePort;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

@Service
public class PostQueryServiceAdapter implements PostQueryServicePort {

    private static final int TEMPERATURE_SEARCH_THRESHOLD = 3;

    private final PostPersistencePort postPersistencePort;
    private final PostLikePersistencePort postLikePersistencePort;

    public PostQueryServiceAdapter(PostPersistencePort postPersistencePort,
                                   PostLikePersistencePort postLikePersistencePort) {
        this.postPersistencePort = postPersistencePort;
        this.postLikePersistencePort = postLikePersistencePort;
    }

    @Override
    @ImageUrlInclusion
    public PostDetailResponse getPostDetails(Long memberId, Long postId) {
        Post post = postPersistencePort.findById(postId)
                .orElseThrow(NotFoundException::post);
        boolean mine = post.isWrittenBy(memberId);
        boolean liked = postLikePersistencePort.existsByPostIdAndMemberId(memberId, postId);
        return new PostDetailResponse(post, mine, liked);
    }

    @Override
    @ImageUrlInclusion
    public MyPostResponses getMyPostIdsAndPostImageUrls(Long memberId, Pageable pageable) {
        Slice<Post> posts = postPersistencePort.findByMemberId(memberId, pageable);
        List<MyPostResponse> myPostResponses = posts.stream()
                .map(MyPostResponse::new)
                .collect(Collectors.toUnmodifiableList());
        return new MyPostResponses(myPostResponses, posts.isLast());
    }

    @Override
    @ImageUrlInclusion
    public FeedResponse getPostsByTemperature(Long memberId, Double minTemperature, Double maxTemperature,
                                              Pageable pageable) {
        Temperatures temperatures = new Temperatures(minTemperature, maxTemperature);
        TemperatureSearchCondition searchCondition =
                temperatures.calculateTemperatureSearchCondition(TEMPERATURE_SEARCH_THRESHOLD);
        Slice<Post> posts = postPersistencePort.findByTemperature(memberId, searchCondition, pageable);
        return convertToFeedResponse(memberId, posts);
    }

    private FeedResponse convertToFeedResponse(Long memberId, Slice<Post> posts) {
        List<PostResponse> postResponses = posts.stream()
                .map(post -> new PostResponse(post, post.isLikedBy(memberId)))
                .collect(Collectors.toUnmodifiableList());
        return new FeedResponse(postResponses, posts.isLast());
    }

    @Override
    @ImageUrlInclusion
    public MyPostsByTemperatureResponses getMyPostsByTemperature(Long memberId, Double minTemperature,
                                                                 Double maxTemperature, Pageable pageable) {
        Temperatures temperatures = new Temperatures(minTemperature, maxTemperature);
        TemperatureSearchCondition searchCondition =
                temperatures.calculateTemperatureSearchCondition(TEMPERATURE_SEARCH_THRESHOLD);
        Slice<Post> myPostsByTemperature =
                postPersistencePort.findByMemberIdAndTemperature(memberId, searchCondition, pageable);
        return convertToMyPostsByTemperatureResponses(memberId, myPostsByTemperature);
    }

    private MyPostsByTemperatureResponses convertToMyPostsByTemperatureResponses(Long memberId,
                                                                                 Slice<Post> myPostsByTemperature) {
        List<MyPostByTemperatureResponse> myPostByTemperatureResponses = myPostsByTemperature.stream()
                .map(post -> new MyPostByTemperatureResponse(post, post.isLikedBy(memberId)))
                .collect(Collectors.toUnmodifiableList());
        PostWriterResponse postWriterResponse = generatePostWriterResponse(myPostsByTemperature.getContent());
        return new MyPostsByTemperatureResponses(
                myPostByTemperatureResponses, postWriterResponse, myPostsByTemperature.isLast());
    }

    private PostWriterResponse generatePostWriterResponse(List<Post> myPostsByTemperature) {
        if (myPostsByTemperature.isEmpty()) {
            return null;
        }
        Post post = myPostsByTemperature.get(0);
        return new PostWriterResponse(post);
    }

    @Override
    @ImageUrlInclusion
    public LikedPostResponses getLikedPost(Long memberId, Pageable pageable) {
        Slice<Post> posts = postPersistencePort.findLikedPosts(memberId, pageable);
        List<LikedPostResponse> likedPostResponses = posts.stream()
                .map(LikedPostResponse::new)
                .collect(Collectors.toUnmodifiableList());
        return new LikedPostResponses(likedPostResponses, posts.isLast());
    }
}

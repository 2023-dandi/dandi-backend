package dandi.dandi.post.application.port.in;

import org.springframework.data.domain.Pageable;

public interface PostQueryServicePort {

    PostDetailResponse getPostDetails(Long memberId, Long postId);

    MyPostResponses getMyPostIdsAndPostImageUrls(Long memberId, Pageable pageable);

    FeedResponse getPostsByTemperature(Long memberId, Double minTemperature, Double maxTemperature,
                                       Pageable pageable);

    MyPostsByTemperatureResponses getMyPostsByTemperature(Long memberId, Double minTemperature,
                                                          Double maxTemperature, Pageable pageable);

    LikedPostResponses getLikedPost(Long memberId, Pageable pageable);
}

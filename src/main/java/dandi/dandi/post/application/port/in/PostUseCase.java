package dandi.dandi.post.application.port.in;

public interface PostUseCase {

    PostRegisterResponse registerPost(Long memberId, PostRegisterCommand postRegisterCommand);

    PostDetailResponse getPostDetails(Long memberId, Long postId);

    void deletePost(Long memberId, Long postId);

    MyPostResponses getMyPostIdsAndPostImageUrls(Long memberId);
}

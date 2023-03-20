package dandi.dandi.post.application.port.in;

public interface PostUseCase {

    PostRegisterResponse registerPost(Long memberId, PostRegisterCommand postRegisterCommand);

    PostDetailResponse getPostDetails(Long postId);
}

package dandi.dandi.post.application.port.in;

public interface PostUseCase {

    Long registerPost(Long memberId, PostRegisterCommand postRegisterCommand);

    PostDetailResponse getPostDetails(Long postId);
}

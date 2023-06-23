package dandi.dandi.post.application.port.in;

public interface PostUseCase {

    PostRegisterResponse registerPost(Long memberId, PostRegisterCommand postRegisterCommand);

    void deletePost(Long memberId, Long postId);

    void reportPost(Long memberId, Long postId);
}

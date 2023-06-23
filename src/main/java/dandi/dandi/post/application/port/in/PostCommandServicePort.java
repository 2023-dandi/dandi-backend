package dandi.dandi.post.application.port.in;

public interface PostCommandServicePort {

    PostRegisterResponse registerPost(Long memberId, PostRegisterCommand postRegisterCommand);

    void deletePost(Long memberId, Long postId);
}

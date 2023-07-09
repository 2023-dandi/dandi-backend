package dandi.dandi.post.application.service;

import dandi.dandi.common.exception.ForbiddenException;
import dandi.dandi.common.exception.NotFoundException;
import dandi.dandi.post.application.port.in.PostCommandServicePort;
import dandi.dandi.post.application.port.in.PostRegisterCommand;
import dandi.dandi.post.application.port.in.PostRegisterResponse;
import dandi.dandi.post.application.port.out.PostPersistencePort;
import dandi.dandi.post.domain.Post;
import dandi.dandi.post.domain.Temperatures;
import dandi.dandi.post.domain.WeatherFeeling;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PostCommandServiceAdapter implements PostCommandServicePort {

    private final PostPersistencePort postPersistencePort;
    private final PostImageCommandService postImageCommandService;

    public PostCommandServiceAdapter(PostPersistencePort postPersistencePort,
                                     PostImageCommandService postImageCommandService) {
        this.postPersistencePort = postPersistencePort;
        this.postImageCommandService = postImageCommandService;
    }

    @Override
    public PostRegisterResponse registerPost(Long memberId, PostRegisterCommand postRegisterCommand) {
        Temperatures temperatures = new Temperatures(
                postRegisterCommand.getMinTemperature(), postRegisterCommand.getMaxTemperature());
        WeatherFeeling weatherFeeling = new WeatherFeeling(
                postRegisterCommand.getFeelingIndex(), postRegisterCommand.getAdditionalFeelingIndices());
        Post post = Post.initial(temperatures, postRegisterCommand.getPostImageUrl(), weatherFeeling);
        Long postId = postPersistencePort.save(post, memberId);
        postImageCommandService.deletePostImageUrlInUnused(postRegisterCommand.getPostImageUrl());
        return new PostRegisterResponse(postId);
    }

    @Override
    public void deletePost(Long memberId, Long postId) {
        Post post = postPersistencePort.findById(postId)
                .orElseThrow(NotFoundException::post);
        validateDeleteAuthorization(post, memberId);
        postPersistencePort.deleteById(postId);
        postImageCommandService.deletePostImage(post.getPostImageUrl());
    }

    public void validateDeleteAuthorization(Post post, Long memberId) {
        if (!post.isWrittenBy(memberId)) {
            throw ForbiddenException.postDeletion();
        }
    }
}

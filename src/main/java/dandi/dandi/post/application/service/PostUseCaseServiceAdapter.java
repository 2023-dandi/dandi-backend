package dandi.dandi.post.application.service;

import dandi.dandi.common.exception.ForbiddenException;
import dandi.dandi.common.exception.NotFoundException;
import dandi.dandi.post.application.port.in.PostRegisterCommand;
import dandi.dandi.post.application.port.in.PostRegisterResponse;
import dandi.dandi.post.application.port.in.PostUseCaseServicePort;
import dandi.dandi.post.application.port.out.PostPersistencePort;
import dandi.dandi.post.domain.Post;
import dandi.dandi.post.domain.Temperatures;
import dandi.dandi.post.domain.WeatherFeeling;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PostUseCaseServiceAdapter implements PostUseCaseServicePort {

    private static final int POST_IMAGE_URL_INDEX = 1;

    private final PostPersistencePort postPersistencePort;
    private final String imageAccessUrl;

    public PostUseCaseServiceAdapter(PostPersistencePort postPersistencePort,
                                     @Value("${cloud.aws.cloud-front.uri}") String imageAccessUrl) {
        this.postPersistencePort = postPersistencePort;
        this.imageAccessUrl = imageAccessUrl;
    }

    @Override
    @Transactional
    public PostRegisterResponse registerPost(Long memberId, PostRegisterCommand postRegisterCommand) {
        Temperatures temperatures = new Temperatures(
                postRegisterCommand.getMinTemperature(), postRegisterCommand.getMaxTemperature());
        WeatherFeeling weatherFeeling = new WeatherFeeling(
                postRegisterCommand.getFeelingIndex(), postRegisterCommand.getAdditionalFeelingIndices());
        String postImageUrl = removeImageAccessUrl(postRegisterCommand.getPostImageUrl());

        Post post = Post.initial(temperatures, postImageUrl, weatherFeeling);
        Long postId = postPersistencePort.save(post, memberId);
        return new PostRegisterResponse(postId);
    }

    private String removeImageAccessUrl(String postImageUrl) {
        return postImageUrl.split(imageAccessUrl)[POST_IMAGE_URL_INDEX];
    }

    @Override
    @Transactional
    public void deletePost(Long memberId, Long postId) {
        Post post = postPersistencePort.findById(postId)
                .orElseThrow(NotFoundException::post);
        validateDeleteAuthorization(post, memberId);
        postPersistencePort.deleteById(postId);
    }

    public void validateDeleteAuthorization(Post post, Long memberId) {
        if (!post.isWrittenBy(memberId)) {
            throw ForbiddenException.postDeletion();
        }
    }
}

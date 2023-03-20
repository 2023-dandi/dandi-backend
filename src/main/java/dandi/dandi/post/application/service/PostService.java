package dandi.dandi.post.application.service;

import dandi.dandi.common.exception.NotFoundException;
import dandi.dandi.post.application.port.in.PostDetailResponse;
import dandi.dandi.post.application.port.in.PostRegisterCommand;
import dandi.dandi.post.application.port.in.PostRegisterResponse;
import dandi.dandi.post.application.port.in.PostUseCase;
import dandi.dandi.post.application.port.out.PostPersistencePort;
import dandi.dandi.post.domain.Post;
import dandi.dandi.post.domain.Temperatures;
import dandi.dandi.post.domain.WeatherFeeling;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PostService implements PostUseCase {

    private final PostPersistencePort postPersistencePort;
    private final String imageAccessUrl;
    private final String postImageDir;

    public PostService(PostPersistencePort postPersistencePort,
                       @Value("${cloud.aws.cloud-front.uri}") String imageAccessUrl,
                       @Value("${image.post-dir}") String postImageDir) {
        this.postPersistencePort = postPersistencePort;
        this.imageAccessUrl = imageAccessUrl;
        this.postImageDir = postImageDir;
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
        return postImageUrl.substring(postImageUrl.indexOf(postImageDir));
    }

    @Override
    @Transactional(readOnly = true)
    public PostDetailResponse getPostDetails(Long postId) {
        Post post = postPersistencePort.findById(postId)
                .orElseThrow(NotFoundException::post);
        return new PostDetailResponse(post, imageAccessUrl);
    }
}

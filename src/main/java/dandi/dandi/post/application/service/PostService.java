package dandi.dandi.post.application.service;

import dandi.dandi.post.application.port.in.PostRegisterCommand;
import dandi.dandi.post.application.port.in.PostUseCase;
import dandi.dandi.post.application.port.out.PostPersistencePort;
import dandi.dandi.post.domain.Post;
import dandi.dandi.post.domain.Temperatures;
import dandi.dandi.post.domain.WeatherFeeling;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PostService implements PostUseCase {

    private final PostPersistencePort postPersistencePort;

    public PostService(PostPersistencePort postPersistencePort) {
        this.postPersistencePort = postPersistencePort;
    }

    @Override
    @Transactional
    public Long registerPost(Long memberId, PostRegisterCommand postRegisterCommand) {
        Temperatures temperatures = new Temperatures(
                postRegisterCommand.getMinTemperature(), postRegisterCommand.getMaxTemperature());
        WeatherFeeling weatherFeeling = new WeatherFeeling(
                postRegisterCommand.getFeelingIndex(), postRegisterCommand.getAdditionalFeelingIndices());
        Post post = new Post(memberId, temperatures, postRegisterCommand.getPostImageUrl(), weatherFeeling);
        return postPersistencePort.save(post);
    }
}

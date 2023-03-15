package dandi.dandi.post.application.service;

import dandi.dandi.auth.exception.UnauthorizedException;
import dandi.dandi.member.application.port.out.MemberPersistencePort;
import dandi.dandi.member.domain.Member;
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
    private final MemberPersistencePort memberPersistencePort;

    public PostService(PostPersistencePort postPersistencePort, MemberPersistencePort memberPersistencePort) {
        this.postPersistencePort = postPersistencePort;
        this.memberPersistencePort = memberPersistencePort;
    }

    @Override
    @Transactional
    public Long registerPost(Long memberId, PostRegisterCommand postRegisterCommand) {
        Temperatures temperatures = new Temperatures(
                postRegisterCommand.getMinTemperature(), postRegisterCommand.getMaxTemperature());
        WeatherFeeling weatherFeeling = new WeatherFeeling(
                postRegisterCommand.getFeelingIndex(), postRegisterCommand.getAdditionalFeelingIndices());
        Post post = Post.initial(temperatures, postRegisterCommand.getPostImageUrl(), weatherFeeling);
        Member member = memberPersistencePort.findById(memberId)
                .orElseThrow(UnauthorizedException::notExistentMember);
        return postPersistencePort.save(post, member);
    }
}

package dandi.dandi.post.application.service;

import static dandi.dandi.member.MemberTestFixture.TEST_MEMBER;
import static dandi.dandi.post.PostFixture.ADDITIONAL_OUTFIT_FEELING_INDICES;
import static dandi.dandi.post.PostFixture.MAX_TEMPERATURE;
import static dandi.dandi.post.PostFixture.MIN_TEMPERATURE;
import static dandi.dandi.post.PostFixture.OUTFIT_FEELING_INDEX;
import static dandi.dandi.post.PostFixture.POST_IMAGE_URL;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import dandi.dandi.member.application.port.out.MemberPersistencePort;
import dandi.dandi.member.domain.Member;
import dandi.dandi.post.application.port.in.PostRegisterCommand;
import dandi.dandi.post.application.port.out.PostPersistencePort;
import dandi.dandi.post.domain.Post;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {

    @Mock
    private PostPersistencePort postPersistencePort;

    @Mock
    private MemberPersistencePort memberPersistencePort;

    @InjectMocks
    private PostService postService;

    @DisplayName("게시글을 작성할 수 있다.")
    @Test
    void registerPost() {
        Long memberId = 1L;
        PostRegisterCommand postRegisterCommand = new PostRegisterCommand(MIN_TEMPERATURE, MAX_TEMPERATURE,
                POST_IMAGE_URL, OUTFIT_FEELING_INDEX, ADDITIONAL_OUTFIT_FEELING_INDICES);
        when(memberPersistencePort.findById(memberId))
                .thenReturn(Optional.of(TEST_MEMBER));
        when(postPersistencePort.save(any(Post.class), any(Member.class)))
                .thenReturn(1L);

        Long postId = postService.registerPost(memberId, postRegisterCommand);

        assertThat(postId).isEqualTo(1L);
    }
}

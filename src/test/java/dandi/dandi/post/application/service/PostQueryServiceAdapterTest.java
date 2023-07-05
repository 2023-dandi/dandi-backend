package dandi.dandi.post.application.service;

import static dandi.dandi.common.constant.Constant.IMAGE_ACCESS_URL;
import static dandi.dandi.member.MemberTestFixture.MEMBER;
import static dandi.dandi.member.MemberTestFixture.MEMBER_ID;
import static dandi.dandi.post.PostFixture.POST;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;

import dandi.dandi.common.exception.NotFoundException;
import dandi.dandi.post.application.port.in.PostDetailResponse;
import dandi.dandi.post.application.port.in.PostWriterResponse;
import dandi.dandi.post.application.port.out.PostPersistencePort;
import dandi.dandi.postlike.application.port.out.PostLikePersistencePort;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PostQueryServiceAdapterTest {

    @Mock
    private PostPersistencePort postPersistencePort;
    @Mock
    private PostLikePersistencePort postLikePersistencePort;
    @InjectMocks
    private PostQueryServiceAdapter postQueryServiceAdapter;

    @DisplayName("게시글의 상세정보를 반환할 수 있다.")
    @ParameterizedTest
    @CsvSource({"1, true", "2, false"})
    void getPostDetails(Long memberId, boolean expectedMine) {
        Long postId = 1L;
        when(postPersistencePort.findById(postId))
                .thenReturn(Optional.of(POST));
        when(postLikePersistencePort.existsByPostIdAndMemberId(memberId, postId))
                .thenReturn(true);

        PostDetailResponse postDetailsResponse = postQueryServiceAdapter.getPostDetails(memberId, postId);

        PostWriterResponse postWriterResponse = postDetailsResponse.getWriter();
        assertAll(
                () -> assertThat(postWriterResponse.getProfileImageUrl())
                        .isEqualTo(System.getProperty(IMAGE_ACCESS_URL) + MEMBER.getProfileImgUrl()),
                () -> assertThat(postWriterResponse.getId())
                        .isEqualTo(POST.getWriterId()),
                () -> assertThat(postWriterResponse.getNickname())
                        .isEqualTo(POST.getWriterNickname()),
                () -> assertThat(postDetailsResponse.isMine()).isEqualTo(expectedMine),
                () -> assertThat(postDetailsResponse.isLiked()).isTrue(),
                () -> assertThat(postDetailsResponse.getPostImageUrl())
                        .isEqualTo(System.getProperty(IMAGE_ACCESS_URL) + POST.getPostImageUrl()),
                () -> assertThat(postDetailsResponse.getTemperatures().getMin())
                        .isEqualTo(POST.getMinTemperature()),
                () -> assertThat(postDetailsResponse.getTemperatures().getMax())
                        .isEqualTo(POST.getMaxTemperature()),
                () -> assertThat(postDetailsResponse.getOutfitFeelings().getFeelingIndex())
                        .isEqualTo(POST.getWeatherFeelingIndex()),
                () -> assertThat(postDetailsResponse.getOutfitFeelings().getAdditionalFeelingIndices())
                        .isEqualTo(POST.getAdditionalWeatherFeelingIndices())
        );
    }

    @DisplayName("상세 정보를 조회하려는 postId에 해당하는 게시글이 존재하지 않는다면 예외를 발생시킨다.")
    @Test
    void getPostDetails_PostNotFound() {
        Long postId = 1L;
        when(postPersistencePort.findById(postId))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> postQueryServiceAdapter.getPostDetails(MEMBER_ID, postId))
                .isInstanceOf(NotFoundException.class)
                .hasMessage(NotFoundException.post().getMessage());
    }
}

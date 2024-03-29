package dandi.dandi.member.application;

import static dandi.dandi.member.MemberTestFixture.INITIAL_PROFILE_IMAGE_URL;
import static dandi.dandi.member.MemberTestFixture.NICKNAME;
import static dandi.dandi.member.MemberTestFixture.OAUTH_ID;
import static dandi.dandi.utils.image.TestImageUtils.TEST_IMAGE_FILE_NAME;
import static dandi.dandi.utils.image.TestImageUtils.generateTestImgMultipartFile;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.amazonaws.SdkClientException;
import dandi.dandi.auth.exception.UnauthorizedException;
import dandi.dandi.image.exception.ImageUploadFailedException;
import dandi.dandi.member.application.port.out.MemberPersistencePort;
import dandi.dandi.member.application.port.out.ProfileImageUploader;
import dandi.dandi.member.application.service.ProfileImageService;
import dandi.dandi.member.domain.Member;
import java.io.IOException;
import java.util.Optional;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProfileImageServiceTest {

    private static final String NOT_INITIAL_PROFILE_IMAGE_URL = "notInitialProfileImageUrl";

    private static final String TEST_PROFILE_BUCKET_IMG_DIR = "test-dir";

    private final MemberPersistencePort memberPersistencePort = Mockito.mock(MemberPersistencePort.class);
    private final ProfileImageUploader profileImageUploader = Mockito.mock(ProfileImageUploader.class);
    private final ProfileImageService profileImageService = new ProfileImageService(memberPersistencePort,
            profileImageUploader, INITIAL_PROFILE_IMAGE_URL, TEST_PROFILE_BUCKET_IMG_DIR);

    @DisplayName("기본 프로필 사진이 아닌 회원의 프로필 사진을 변경하면 기존 사진을 삭제하고 사진을 업로드 한 후, 사진의 식별값을 반환한다.")
    @Test
    void updateProfileImage_NotInitialProfileImage() throws IOException {
        Long memberId = 1L;
        Member notInitialProfileImageMember =
                Member.initial(OAUTH_ID, NICKNAME, NOT_INITIAL_PROFILE_IMAGE_URL);
        when(memberPersistencePort.findById(memberId))
                .thenReturn(Optional.of(notInitialProfileImageMember));

        String imgUrl = profileImageService.updateProfileImage(memberId, generateTestImgMultipartFile())
                .getProfileImageUrl();

        assertAll(
                () -> assertThat(imgUrl).contains(TEST_PROFILE_BUCKET_IMG_DIR, TEST_IMAGE_FILE_NAME),
                () -> verify(profileImageUploader).delete(anyString())
        );
    }

    @DisplayName("기본 프로필 사진을 가진 회원의 사진을 변경하면 단순 사진 업로드 후, 사진의 식별값을 반환한다.")
    @Test
    void updateProfileImage_InitialProfileImageMember() throws IOException {
        Long memberId = 1L;
        when(memberPersistencePort.findById(memberId))
                .thenReturn(Optional.of(Member.initial(OAUTH_ID, NICKNAME, INITIAL_PROFILE_IMAGE_URL)));

        String imgUrl = profileImageService.updateProfileImage(memberId, generateTestImgMultipartFile())
                .getProfileImageUrl();

        assertAll(
                () -> assertThat(imgUrl).contains(TEST_PROFILE_BUCKET_IMG_DIR, TEST_IMAGE_FILE_NAME),
                () -> verify(profileImageUploader, never()).delete(anyString())
        );
    }

    @DisplayName("새로운 프로필 사진 업로드에 실패하면 사진 업로드 실패 예외를 발생시킨다.")
    @ParameterizedTest
    @MethodSource("provideImageUploadAvailableException")
    void updateProfileImageUrl_ImageUploadFailed(Exception exception) throws IOException {
        Long memberId = 1L;
        when(memberPersistencePort.findById(memberId))
                .thenReturn(Optional.of(Member.initial(OAUTH_ID, NICKNAME, INITIAL_PROFILE_IMAGE_URL)));
        doThrow(exception)
                .when(profileImageUploader)
                .upload(anyString(), any());

        assertThatThrownBy(() -> profileImageService.updateProfileImage(memberId, generateTestImgMultipartFile()))
                .isInstanceOf(ImageUploadFailedException.class);
    }

    private static Stream<Arguments> provideImageUploadAvailableException() {
        return Stream.of(
                Arguments.of(new SdkClientException("사진 저장 실패")),
                Arguments.of(new IOException())
        );
    }

    @DisplayName("존재하지 않는 회원의 프로필 사진을 업로드하려고 하면 예외를 발생시킨다.")
    @Test
    void updateProfileImage_notExistentMember() {
        Long notExistentMemberId = 1L;
        when(memberPersistencePort.findById(notExistentMemberId))
                .thenReturn(Optional.empty());

        assertThatThrownBy(
                () -> profileImageService.updateProfileImage(notExistentMemberId, generateTestImgMultipartFile()))
                .isInstanceOf(UnauthorizedException.class)
                .hasMessage(UnauthorizedException.notExistentMember().getMessage());
    }
}

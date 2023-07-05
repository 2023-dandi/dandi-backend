package dandi.dandi.member.application.service;

import static dandi.dandi.member.MemberTestFixture.INITIAL_PROFILE_IMAGE_URL;
import static dandi.dandi.member.MemberTestFixture.NICKNAME;
import static dandi.dandi.member.MemberTestFixture.OAUTH_ID;
import static dandi.dandi.utils.TestImageUtils.TEST_IMAGE_FILE_NAME;
import static dandi.dandi.utils.TestImageUtils.generateTestImgMultipartFile;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import dandi.dandi.auth.exception.UnauthorizedException;
import dandi.dandi.common.constant.Constant;
import dandi.dandi.image.application.out.ImageManager;
import dandi.dandi.image.exception.ImageUploadFailedException;
import dandi.dandi.member.application.port.out.MemberPersistencePort;
import dandi.dandi.member.domain.Member;
import java.io.IOException;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MemberImageServiceTest {

    private static final String NOT_INITIAL_PROFILE_IMAGE_URL = "notInitialProfileImageUrl";

    private static final String TEST_PROFILE_BUCKET_IMG_DIR = "test-dir";

    private final MemberPersistencePort memberPersistencePort = Mockito.mock(MemberPersistencePort.class);
    private final ImageManager imageManager = Mockito.mock(ImageManager.class);
    private final MemberImageService memberImageService = new MemberImageService(memberPersistencePort, imageManager,
            INITIAL_PROFILE_IMAGE_URL, TEST_PROFILE_BUCKET_IMG_DIR);

    @DisplayName("기본 프로필 사진이 아닌 회원의 프로필 사진을 변경하면 기존 사진을 삭제하고 사진을 업로드 한 후, 사진의 식별값을 반환한다.")
    @Test
    void updateProfileImage_NotInitialProfileImage() throws IOException {
        Long memberId = 1L;
        Member initialProfileImageMember = Member.initial(OAUTH_ID, NICKNAME, NOT_INITIAL_PROFILE_IMAGE_URL);
        when(memberPersistencePort.findById(memberId))
                .thenReturn(Optional.of(initialProfileImageMember));

        String imgUrl = memberImageService.updateProfileImage(memberId, generateTestImgMultipartFile())
                .getProfileImageUrl();

        assertAll(
                () -> assertThat(imgUrl)
                        .startsWith(System.getProperty(Constant.IMAGE_ACCESS_URL) + TEST_PROFILE_BUCKET_IMG_DIR)
                        .contains(TEST_IMAGE_FILE_NAME),
                () -> verify(imageManager).delete(initialProfileImageMember.getProfileImgUrl())
        );
    }

    @DisplayName("기본 프로필 사진을 가진 회원의 사진을 변경하면 단순 사진 업로드 후, 사진의 식별값을 반환한다.")
    @Test
    void updateProfileImage_InitialProfileImageMember() throws IOException {
        Long memberId = 1L;
        when(memberPersistencePort.findById(memberId))
                .thenReturn(Optional.of(Member.initial(OAUTH_ID, NICKNAME, INITIAL_PROFILE_IMAGE_URL)));

        String imgUrl = memberImageService.updateProfileImage(memberId, generateTestImgMultipartFile())
                .getProfileImageUrl();

        assertAll(
                () -> assertThat(imgUrl).contains(TEST_PROFILE_BUCKET_IMG_DIR, TEST_IMAGE_FILE_NAME),
                () -> verify(imageManager, never()).delete(anyString())
        );
    }

    @DisplayName("새로운 프로필 사진 업로드에 실패하면 사진 업로드 실패 예외를 발생시킨다.")
    @Test
    void updateProfileImageUrl_ImageUploadFailed() throws IOException {
        Long memberId = 1L;
        when(memberPersistencePort.findById(memberId))
                .thenReturn(Optional.of(Member.initial(OAUTH_ID, NICKNAME, INITIAL_PROFILE_IMAGE_URL)));
        doThrow(new ImageUploadFailedException())
                .when(imageManager)
                .upload(anyString(), any());

        assertThatThrownBy(() -> memberImageService.updateProfileImage(memberId, generateTestImgMultipartFile()))
                .isInstanceOf(ImageUploadFailedException.class);
    }

    @DisplayName("존재하지 않는 회원의 프로필 사진을 업로드하려고 하면 예외를 발생시킨다.")
    @Test
    void updateProfileImage_notExistentMember() {
        Long notExistentMemberId = 1L;
        when(memberPersistencePort.findById(notExistentMemberId))
                .thenReturn(Optional.empty());

        assertThatThrownBy(
                () -> memberImageService.updateProfileImage(notExistentMemberId, generateTestImgMultipartFile()))
                .isInstanceOf(UnauthorizedException.class)
                .hasMessage(UnauthorizedException.notExistentMember().getMessage());
    }
}

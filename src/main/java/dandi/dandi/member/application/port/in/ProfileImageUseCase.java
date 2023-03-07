package dandi.dandi.member.application.port.in;

import org.springframework.web.multipart.MultipartFile;

public interface ProfileImageUseCase {

    ProfileImageUpdateResponse updateProfileImage(Long memberId, MultipartFile profileImage);
}

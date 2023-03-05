package dandi.dandi.member.application.port.in;

import dandi.dandi.member.application.port.out.dto.ProfileImageUpdateResponse;
import org.springframework.web.multipart.MultipartFile;

public interface ProfileImageUseCase {

    ProfileImageUpdateResponse updateProfileImage(Long memberId, MultipartFile profileImage);
}

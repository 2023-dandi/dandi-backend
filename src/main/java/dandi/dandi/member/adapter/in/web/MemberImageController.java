package dandi.dandi.member.adapter.in.web;

import dandi.dandi.auth.adapter.in.web.support.Login;
import dandi.dandi.member.application.port.in.MemberImageUseCase;
import dandi.dandi.member.application.port.in.ProfileImageUpdateResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class MemberImageController implements MemberImageControllerDocs {

    private final MemberImageUseCase memberImageUseCase;

    public MemberImageController(MemberImageUseCase memberImageUseCase) {
        this.memberImageUseCase = memberImageUseCase;
    }

    @PutMapping(path = "/members/profile-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProfileImageUpdateResponse> updateMemberProfileImage(@Login Long memberId,
                                                                               @RequestPart(value = "profileImage")
                                                                               MultipartFile profileImage) {
        return ResponseEntity.ok(memberImageUseCase.updateProfileImage(memberId, profileImage));
    }
}

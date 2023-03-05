package dandi.dandi.member.web;

import dandi.dandi.auth.support.Login;
import dandi.dandi.member.application.port.in.MemberUseCase;
import dandi.dandi.member.application.port.in.ProfileImageUseCase;
import dandi.dandi.member.application.port.in.dto.LocationUpdateRequest;
import dandi.dandi.member.application.port.in.dto.NicknameUpdateRequest;
import dandi.dandi.member.application.port.out.dto.MemberInfoResponse;
import dandi.dandi.member.application.port.out.dto.NicknameDuplicationCheckResponse;
import dandi.dandi.member.application.port.out.dto.ProfileImageUpdateResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/members")
public class MemberController implements MemberControllerDocs {

    private final MemberUseCase memberUseCase;
    private final ProfileImageUseCase profileImageUseCase;

    public MemberController(MemberUseCase memberUseCase, ProfileImageUseCase profileImageUseCase) {
        this.memberUseCase = memberUseCase;
        this.profileImageUseCase = profileImageUseCase;
    }

    @GetMapping
    public ResponseEntity<MemberInfoResponse> getMemberInfo(@Login Long memberId) {
        return ResponseEntity.ok(memberUseCase.findMemberInfo(memberId));
    }

    @GetMapping(value = "/nickname/duplication", params = "nickname")
    public ResponseEntity<NicknameDuplicationCheckResponse> checkNicknameDuplication(@RequestParam String nickname) {
        return ResponseEntity.ok(memberUseCase.checkDuplication(nickname));
    }

    @PatchMapping("/nickname")
    public ResponseEntity<Void> updateMemberNickname(@Login Long memberId,
                                                     @RequestBody NicknameUpdateRequest nicknameUpdateRequest) {
        memberUseCase.updateNickname(memberId, nicknameUpdateRequest);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/location")
    public ResponseEntity<Void> updateMemberLocation(@Login Long memberId,
                                                     @RequestBody LocationUpdateRequest locationUpdateRequest) {
        memberUseCase.updateLocation(memberId, locationUpdateRequest);
        return ResponseEntity.noContent().build();
    }

    @PutMapping(path = "/profile-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProfileImageUpdateResponse> updateMemberProfileImage(@Login Long memberId,
                                                                               @RequestPart(value = "profileImage")
                                                                               MultipartFile profileImage) {
        return ResponseEntity.ok(profileImageUseCase.updateProfileImage(memberId, profileImage));
    }
}

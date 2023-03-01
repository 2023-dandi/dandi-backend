package dandi.dandi.member.presentation;

import dandi.dandi.auth.support.Login;
import dandi.dandi.member.application.MemberService;
import dandi.dandi.member.application.ProfileImageService;
import dandi.dandi.member.application.dto.LocationUpdateRequest;
import dandi.dandi.member.application.dto.MemberInfoResponse;
import dandi.dandi.member.application.dto.NicknameUpdateRequest;
import dandi.dandi.member.application.dto.ProfileImageUpdateResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/members")
public class MemberController implements MemberControllerDocs {

    private final MemberService memberService;
    private final ProfileImageService profileImageService;

    public MemberController(MemberService memberService, ProfileImageService profileImageService) {
        this.memberService = memberService;
        this.profileImageService = profileImageService;
    }

    @GetMapping
    public ResponseEntity<MemberInfoResponse> getMemberInfo(@Login Long memberId) {
        return ResponseEntity.ok(memberService.findMemberInfo(memberId));
    }

    @PatchMapping("/nickname")
    public ResponseEntity<Void> updateMemberNickname(@Login Long memberId,
                                                     @RequestBody NicknameUpdateRequest nicknameUpdateRequest) {
        memberService.updateNickname(memberId, nicknameUpdateRequest);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/location")
    public ResponseEntity<Void> updateMemberLocation(@Login Long memberId,
                                                     @RequestBody LocationUpdateRequest locationUpdateRequest) {
        memberService.updateLocation(memberId, locationUpdateRequest);
        return ResponseEntity.noContent().build();
    }

    @PutMapping(path = "/profile-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProfileImageUpdateResponse> updateMemberProfileImage(@Login Long memberId,
                                                                               @RequestPart(value = "profileImage")
                                                                               MultipartFile profileImage) {
        return ResponseEntity.ok(profileImageService.updateProfileImage(memberId, profileImage));
    }
}

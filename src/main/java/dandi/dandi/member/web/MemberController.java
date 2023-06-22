package dandi.dandi.member.web;

import dandi.dandi.auth.web.support.Login;
import dandi.dandi.member.application.port.in.MemberBlockCommand;
import dandi.dandi.member.application.port.in.MemberInfoResponse;
import dandi.dandi.member.application.port.in.MemberUseCase;
import dandi.dandi.member.application.port.in.NicknameDuplicationCheckResponse;
import dandi.dandi.member.web.dto.in.LocationUpdateRequest;
import dandi.dandi.member.web.dto.in.NicknameUpdateRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/members")
public class MemberController implements MemberControllerDocs {

    private final MemberUseCase memberUseCase;

    public MemberController(MemberUseCase memberUseCase) {
        this.memberUseCase = memberUseCase;
    }

    @GetMapping
    public ResponseEntity<MemberInfoResponse> getMemberInfo(@Login Long memberId) {
        return ResponseEntity.ok(memberUseCase.findMemberInfo(memberId));
    }

    @GetMapping(value = "/nickname/duplication", params = "nickname")
    public ResponseEntity<NicknameDuplicationCheckResponse> checkNicknameDuplication(@Login Long memberId,
                                                                                     @RequestParam String nickname) {
        return ResponseEntity.ok(memberUseCase.checkDuplication(memberId, nickname));
    }

    @PatchMapping("/nickname")
    public ResponseEntity<Void> updateMemberNickname(@Login Long memberId,
                                                     @RequestBody NicknameUpdateRequest nicknameUpdateRequest) {
        memberUseCase.updateNickname(memberId, nicknameUpdateRequest.toCommand());
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/location")
    public ResponseEntity<Void> updateMemberLocation(@Login Long memberId,
                                                     @RequestBody LocationUpdateRequest locationUpdateRequest) {
        memberUseCase.updateLocation(memberId, locationUpdateRequest.toCommand());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/blocks")
    public ResponseEntity<Void> blockMember(@Login Long memberId, @RequestBody MemberBlockCommand memberBlockCommand) {
        memberUseCase.blockMember(memberId, memberBlockCommand);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}

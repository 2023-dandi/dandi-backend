package dandi.dandi.member.web;

import dandi.dandi.auth.web.support.Login;
import dandi.dandi.member.application.port.in.MemberInfoResponse;
import dandi.dandi.member.application.port.in.MemberQueryServicePort;
import dandi.dandi.member.application.port.in.MemberUseCaseServicePort;
import dandi.dandi.member.application.port.in.NicknameDuplicationCheckResponse;
import dandi.dandi.member.web.dto.in.LocationUpdateRequest;
import dandi.dandi.member.web.dto.in.NicknameUpdateRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/members")
public class MemberController implements MemberControllerDocs {

    private final MemberUseCaseServicePort memberUseCaseServicePort;
    private final MemberQueryServicePort memberQueryServicePort;

    public MemberController(MemberUseCaseServicePort memberUseCaseServicePort,
                            MemberQueryServicePort memberQueryServicePort) {
        this.memberUseCaseServicePort = memberUseCaseServicePort;
        this.memberQueryServicePort = memberQueryServicePort;
    }

    @GetMapping
    public ResponseEntity<MemberInfoResponse> getMemberInfo(@Login Long memberId) {
        return ResponseEntity.ok(memberQueryServicePort.findMemberInfo(memberId));
    }

    @GetMapping(value = "/nickname/duplication", params = "nickname")
    public ResponseEntity<NicknameDuplicationCheckResponse> checkNicknameDuplication(@Login Long memberId,
                                                                                     @RequestParam String nickname) {
        return ResponseEntity.ok(memberQueryServicePort.checkDuplication(memberId, nickname));
    }

    @PatchMapping("/nickname")
    public ResponseEntity<Void> updateMemberNickname(@Login Long memberId,
                                                     @RequestBody NicknameUpdateRequest nicknameUpdateRequest) {
        memberUseCaseServicePort.updateNickname(memberId, nicknameUpdateRequest.toCommand());
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/location")
    public ResponseEntity<Void> updateMemberLocation(@Login Long memberId,
                                                     @RequestBody LocationUpdateRequest locationUpdateRequest) {
        memberUseCaseServicePort.updateLocation(memberId, locationUpdateRequest.toCommand());
        return ResponseEntity.noContent().build();
    }
}

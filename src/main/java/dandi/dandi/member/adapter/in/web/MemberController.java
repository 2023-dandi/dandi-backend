package dandi.dandi.member.adapter.in.web;

import dandi.dandi.auth.web.support.Login;
import dandi.dandi.member.adapter.in.web.dto.in.LocationUpdateRequest;
import dandi.dandi.member.adapter.in.web.dto.in.NicknameUpdateRequest;
import dandi.dandi.member.application.port.in.MemberUseCaseServicePort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/members")
public class MemberController implements MemberControllerDocs {

    private final MemberUseCaseServicePort memberUseCaseServicePort;

    public MemberController(MemberUseCaseServicePort memberUseCaseServicePort) {
        this.memberUseCaseServicePort = memberUseCaseServicePort;
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

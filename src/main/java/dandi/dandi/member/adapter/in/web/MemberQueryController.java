package dandi.dandi.member.adapter.in.web;

import dandi.dandi.auth.adapter.in.web.support.Login;
import dandi.dandi.member.application.port.in.MemberInfoResponse;
import dandi.dandi.member.application.port.in.MemberQueryServicePort;
import dandi.dandi.member.application.port.in.NicknameDuplicationCheckResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/members")
public class MemberQueryController implements MemberQueryControllerDocs {

    private final MemberQueryServicePort memberQueryServicePort;

    public MemberQueryController(MemberQueryServicePort memberQueryServicePort) {
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
}

package dandi.dandi.member.presentation;

import dandi.dandi.auth.support.Login;
import dandi.dandi.member.application.MemberService;
import dandi.dandi.member.application.dto.MemberInfoResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/members")
    public ResponseEntity<MemberInfoResponse> getMemberNickname(@Login Long memberId) {
        return ResponseEntity.ok(memberService.findMemberInfo(memberId));
    }
}

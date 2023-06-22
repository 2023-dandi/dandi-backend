package dandi.dandi.member.web;

import dandi.dandi.auth.web.support.Login;
import dandi.dandi.member.application.port.in.MemberBlockCommand;
import dandi.dandi.member.application.port.in.MemberBlockUseCasePort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MemberBlockController implements MemberBlockControllerDocs {

    private final MemberBlockUseCasePort memberBlockUseCasePort;

    public MemberBlockController(MemberBlockUseCasePort memberBlockUseCasePort) {
        this.memberBlockUseCasePort = memberBlockUseCasePort;
    }

    @PostMapping("/members/blocks")
    public ResponseEntity<Void> blockMember(@Login Long memberId, @RequestBody MemberBlockCommand memberBlockCommand) {
        memberBlockUseCasePort.blockMember(memberId, memberBlockCommand);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}

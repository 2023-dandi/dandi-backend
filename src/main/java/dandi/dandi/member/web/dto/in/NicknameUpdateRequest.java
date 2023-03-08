package dandi.dandi.member.web.dto.in;

import dandi.dandi.member.application.port.in.NicknameUpdateCommand;
import io.swagger.v3.oas.annotations.media.Schema;

public class NicknameUpdateRequest {

    @Schema(example = "neNickname")
    private String nickname;

    public NicknameUpdateRequest() {
    }

    public NicknameUpdateRequest(String nickname) {
        this.nickname = nickname;
    }

    public String getNickname() {
        return nickname;
    }

    public NicknameUpdateCommand toCommand() {
        return new NicknameUpdateCommand(nickname);
    }
}

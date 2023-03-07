package dandi.dandi.member.web.dto.in;

import dandi.dandi.member.application.port.in.NicknameUpdateCommand;
import io.swagger.v3.oas.annotations.media.Schema;

public class NicknameUpdateRequest {

    @Schema(example = "new Nickname")
    private String newNickname;

    public NicknameUpdateRequest() {
    }

    public NicknameUpdateRequest(String newNickname) {
        this.newNickname = newNickname;
    }

    public String getNewNickname() {
        return newNickname;
    }

    public NicknameUpdateCommand toCommand() {
        return new NicknameUpdateCommand(newNickname);
    }
}

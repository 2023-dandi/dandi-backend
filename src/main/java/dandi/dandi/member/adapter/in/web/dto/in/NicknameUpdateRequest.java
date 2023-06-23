package dandi.dandi.member.adapter.in.web.dto.in;

import dandi.dandi.member.application.port.in.NicknameUpdateCommand;

public class NicknameUpdateRequest {

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

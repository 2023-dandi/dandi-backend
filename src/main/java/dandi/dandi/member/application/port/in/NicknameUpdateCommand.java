package dandi.dandi.member.application.port.in;

import dandi.dandi.common.validation.SelfValidating;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;

public class NicknameUpdateCommand extends SelfValidating<NicknameUpdateCommand> {

    private static final String INVALID_NICKNAME_MESSAGE = "닉네임은 공백없이 (.), (_), 영어와 숫자로 이루어진 2 ~ 23자여야 합니다.";

    @NotNull
    @NotBlank
    @Pattern(regexp = "[a-zA-Z0-9_.]*")
    @Length(min = 2, max = 23)
    private final String nickname;

    public NicknameUpdateCommand(String nickname) {
        this.nickname = nickname;
        this.validateSelf(INVALID_NICKNAME_MESSAGE);
    }

    public String getNickname() {
        return nickname;
    }
}

package dandi.dandi.auth.application.port.in;

import dandi.dandi.common.validation.SelfValidating;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class LoginCommand extends SelfValidating<LoginCommand> {

    private static final String NULL_BLANK_LOGIN_COMMAND_EXCEPTION_MESSAGE = "idToken은 빈 문자열일 수 없습니다.";

    @NotNull
    @NotBlank
    private final String idToken;

    public LoginCommand(String idToken) {
        this.idToken = idToken;
        this.validateSelf(NULL_BLANK_LOGIN_COMMAND_EXCEPTION_MESSAGE);
    }

    public String getIdToken() {
        return idToken;
    }
}

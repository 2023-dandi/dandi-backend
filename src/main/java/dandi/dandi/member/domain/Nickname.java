package dandi.dandi.member.domain;

import java.util.regex.Pattern;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Nickname {

    private static final Pattern AVAILABLE_NICKNAME_CHARACTERS = Pattern.compile("[a-zA-Z0-9-.]*");
    private static final int MIN_LENGTH = 2;
    private static final int MAX_LENGTH = 23;

    @Column(name = "nickname", unique = true)
    private String value;

    private Nickname(String value) {
        this.value = value;
    }

    private Nickname() {
    }

    public static Nickname from(String value) {
        validate(value);
        return new Nickname(value);
    }

    private static void validate(String value) {
        if (value.length() < MIN_LENGTH || value.length() > MAX_LENGTH) {
            throw new IllegalArgumentException("닉네임은 2 ~ 23자만 가능합니다.");
        }
        if (!AVAILABLE_NICKNAME_CHARACTERS.matcher(value).matches()) {
            throw new IllegalArgumentException("닉네임에 가능한 문자는 공백없이 (.), (-), 영어와 숫자입니다.");
        }
    }

    public String getValue() {
        return value;
    }
}

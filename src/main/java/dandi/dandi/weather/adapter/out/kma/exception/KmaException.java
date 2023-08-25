package dandi.dandi.weather.adapter.out.kma.exception;

import dandi.dandi.advice.ExternalServerException;

public class KmaException extends ExternalServerException {

    private static final String INVALID_CODE_MESSAGE = "기상청 %s 코드를 찾을 수 없습니다.";

    public KmaException(String message) {
        super(message);
    }

    public static KmaException categoryCode() {
        return new KmaException(String.format(INVALID_CODE_MESSAGE, "카테고리"));
    }

    public static KmaException precipitationTypeCode() {
        return new KmaException(String.format(INVALID_CODE_MESSAGE, "강수량"));
    }

    public static KmaException responseCode() {
        return new KmaException(String.format(INVALID_CODE_MESSAGE, "응답"));
    }

    public static KmaException windDirectionCode() {
        return new KmaException(String.format(INVALID_CODE_MESSAGE, "풍향"));
    }
}

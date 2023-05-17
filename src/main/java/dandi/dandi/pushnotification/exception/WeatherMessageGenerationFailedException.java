package dandi.dandi.pushnotification.exception;

public class WeatherMessageGenerationFailedException extends RuntimeException {

    private static final String MESSAGE = "실패한 기상청 날씨 결과로 날씨 메시지를 생성할 수 없습니다.";

    public WeatherMessageGenerationFailedException() {
        super(MESSAGE);
    }
}

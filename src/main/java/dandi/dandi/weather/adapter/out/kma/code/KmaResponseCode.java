package dandi.dandi.weather.adapter.out.kma.code;

import dandi.dandi.weather.application.port.out.WeatherRequestFatalException;
import org.apache.http.protocol.HTTP;

import java.util.Arrays;

public enum KmaResponseCode {

    NORMAL_SERVICE("00"),
    APPLICATION_ERROR("01"),
    DB_ERROR("02"),
    NO_DATA_ERROR("03"),
    HTTP_ERROR("04"),
    SERVICE_TIME_OUT("05"),
    INVALID_REQUEST_PARAMETER_ERROR("10"),
    NO_MANDATORY_REQUEST_PARAMETERS_ERROR("11"),
    NO_OPENAPI_SERVICE_ERROR("12"),
    SERVICE_ACCESS_DENIED_ERROR("20"),
    TEMPORARILY_DISABLE_THE_SERVICE_KEY_ERROR("21"),
    LIMITED_NUMBER_OF_SERVICE_REQUESTS_EXCEEDS_ERROR("22"),
    SERVICE_KEY_IS_NOT_REGISTERED_ERROR("30"),
    DEADLINE_HAS_EXPIRED_ERROR("31"),
    UNREGISTERED_IP_ERROR("32"),
    UNSIGNED_CALL_ERROR("33"),
    UNKNOWN_ERROR("99"),
    HTTP_ROUTING_ERROR("100"),
    ;

    private final String value;

    KmaResponseCode(String value) {
        this.value = value;
    }

    public static KmaResponseCode from(String value) {
        return Arrays.stream(values())
                .filter(kmaResponseCode -> kmaResponseCode.value.equals(value))
                .findFirst()
                .orElseThrow(() -> new WeatherRequestFatalException("기상청 응답 코드를 변환할 수 없습니다."));
    }

    public boolean isSuccessful() {
        return this == NORMAL_SERVICE;
    }

    public boolean isErrorAssociatedWithLocation() {
        return this == NO_DATA_ERROR;
    }

    public boolean isTemporaryExternalServerError() {
        return this == SERVICE_TIME_OUT || this == DB_ERROR || this == HTTP_ERROR ||
                this == SERVICE_ACCESS_DENIED_ERROR || this == TEMPORARILY_DISABLE_THE_SERVICE_KEY_ERROR ||
                this == SERVICE_KEY_IS_NOT_REGISTERED_ERROR || this == HTTP_ROUTING_ERROR;
    }

    public boolean isRetryable() {
        return this == SERVICE_TIME_OUT || this == DB_ERROR || this == HTTP_ERROR ||
                this == SERVICE_ACCESS_DENIED_ERROR || this == TEMPORARILY_DISABLE_THE_SERVICE_KEY_ERROR ||
                this == SERVICE_KEY_IS_NOT_REGISTERED_ERROR;

    }
}

package dandi.dandi.weather.adapter.out.kma.code;

import dandi.dandi.weather.application.port.out.WeatherRequestFatalException;

import java.util.Arrays;
import java.util.Optional;

public enum KmaResponseCode {

	NORMAL_SERVICE("NORMAL_SERVICE", "00"),
	APPLICATION_ERROR("APPLICATION_ERROR", "01"),
	DB_ERROR("DB_ERROR", "02"),
	NO_DATA_ERROR("NO_DATA_ERROR", "03"),
	HTTP_ERROR("HTTP_ERROR", "04"),
	SERVICE_TIME_OUT("SERVICE_TIME_OUT", "05"),
	INVALID_REQUEST_PARAMETER_ERROR("INVALID_REQUEST_PARAMETER_ERROR", "10"),
	NO_MANDATORY_REQUEST_PARAMETERS_ERROR("NO_MANDATORY_REQUEST_PARAMETERS_ERROR", "11"),
	NO_OPENAPI_SERVICE_ERROR("NO_OPENAPI_SERVICE_ERROR", "12"),
	SERVICE_ACCESS_DENIED_ERROR("SERVICE_ACCESS_DENIED_ERROR", "20"),
	TEMPORARILY_DISABLE_THE_SERVICE_KEY_ERROR("TEMPORARILY_DISABLE_THE_SERVICE_KEY_ERROR", "21"),
	LIMITED_NUMBER_OF_SERVICE_REQUESTS_EXCEEDS_ERROR("LIMITED_NUMBER_OF_SERVICE_REQUESTS_EXCEEDS_ERROR", "22"),
	SERVICE_KEY_IS_NOT_REGISTERED_ERROR("SERVICE_KEY_IS_NOT_REGISTERED_ERROR", "30"),
	DEADLINE_HAS_EXPIRED_ERROR("DEADLINE_HAS_EXPIRED_ERROR", "31"),
	UNREGISTERED_IP_ERROR("UNREGISTERED_IP_ERROR", "32"),
	UNSIGNED_CALL_ERROR("UNSIGNED_CALL_ERROR", "33"),
	UNKNOWN_ERROR("UNKNOWN_ERROR", "99"),
	;

	private final String errorMessage;
	private final String resultCode;

	KmaResponseCode(String errorMessage, String resultCode) {
		this.errorMessage = errorMessage;
		this.resultCode = resultCode;
	}

	public static KmaResponseCode from(String value) {
		return Arrays.stream(values())
			.filter(kmaResponseCode -> kmaResponseCode.resultCode.equals(value))
			.findFirst()
			.orElseThrow(() -> new WeatherRequestFatalException("기상청 응답 코드를 변환할 수 없습니다."));
	}

	public static Optional<KmaResponseCode> findByNameContainedInValueOrElseNull(String value) {
		return Arrays.stream(values())
				.filter(kmaResponseCode -> value.contains(kmaResponseCode.errorMessage))
				.findFirst();
	}

	public String getResultCode() {
		return resultCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public boolean isSuccessful() {
		return this == NORMAL_SERVICE;
	}

	public boolean isErrorAssociatedWithLocation() {
		return this == NO_DATA_ERROR;
	}

	public boolean isRetryable() {
		return this == APPLICATION_ERROR || this == SERVICE_TIME_OUT || this == DB_ERROR || this == HTTP_ERROR ||
			this == SERVICE_ACCESS_DENIED_ERROR || this == TEMPORARILY_DISABLE_THE_SERVICE_KEY_ERROR ||
			this == SERVICE_KEY_IS_NOT_REGISTERED_ERROR;
	}
}

package dandi.dandi.weather.adapter.out.kma.code;

import java.util.Arrays;

import dandi.dandi.weather.application.port.out.WeatherRequestFatalException;

public enum DataPortalResponse {

	HTTP_ROUTING_ERROR("HTTP ROUTING ERROR", true),
	;

	private final String errorMessage;
	private final boolean retryable;

	DataPortalResponse(String errorMessage, boolean retryable) {
		this.errorMessage = errorMessage;
		this.retryable = retryable;
	}

	public static DataPortalResponse fromResponseBody(String responseBody) {
		return Arrays.stream(values())
			.filter(response -> responseBody.contains(response.errorMessage))
			.findAny()
			.orElseThrow(() -> new WeatherRequestFatalException("처리할 수 없는 공공 데이터 포털 에러\r\n" + responseBody));
	}

	public boolean isRetryable() {
		return retryable;
	}
}

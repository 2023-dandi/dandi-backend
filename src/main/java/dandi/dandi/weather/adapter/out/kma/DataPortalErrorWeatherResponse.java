package dandi.dandi.weather.adapter.out.kma;

import java.util.List;

import dandi.dandi.weather.adapter.out.kma.dto.WeatherItem;

public class DataPortalErrorWeatherResponse implements WeatherResponsesI {

	private static final List<String> RETRYABLE_DATA_PORTAL_ERROR_MESSAGES = List.of(
		"HTTP ROUTING ERROR"
	);

	private final boolean retryable;
	private final String responseBody;

	private DataPortalErrorWeatherResponse(boolean retryable, String responseBody) {
		this.retryable = retryable;
		this.responseBody = responseBody;
	}

	public static DataPortalErrorWeatherResponse fromXmlContent(String xmlResponseBody) {
		boolean retryable = RETRYABLE_DATA_PORTAL_ERROR_MESSAGES.stream()
			.anyMatch(xmlResponseBody::contains);
		return new DataPortalErrorWeatherResponse(retryable, xmlResponseBody);
	}

	@Override
	public boolean isSuccessful() {
		return retryable;
	}

	@Override
	public boolean isRetryableError() {
		return true;
	}

	@Override
	public boolean isNoDataLocationError() {
		return false;
	}

	@Override
	public List<WeatherItem> getWeatherItems() {
		throw new IllegalStateException("공공 데이터 포털에서 내린 에러 응답에는 날씨 데이터가 존재하지 않습니다.");
	}

	@Override
	public String getResultMessage() {
		return responseBody;
	}
}

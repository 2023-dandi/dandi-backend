package dandi.dandi.weather.adapter.out.kma;

import java.util.List;

import dandi.dandi.weather.adapter.out.kma.code.DataPortalResponse;
import dandi.dandi.weather.adapter.out.kma.dto.WeatherItem;

public class DataPortalErrorWeatherResponse implements WeatherResponsesI {

	private final boolean retryable;
	private final String responseBody;

	private DataPortalErrorWeatherResponse(boolean retryable, String responseBody) {
		this.retryable = retryable;
		this.responseBody = responseBody;
	}

	public static DataPortalErrorWeatherResponse fromXmlContent(String xmlResponseBody) {
		boolean retryable = DataPortalResponse.fromResponseBody(xmlResponseBody)
			.isRetryable();
		return new DataPortalErrorWeatherResponse(retryable, xmlResponseBody);
	}

	@Override
	public boolean isSuccessful() {
		return retryable;
	}

	@Override
	public boolean isRetryableError() {
		return retryable;
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

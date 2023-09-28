package dandi.dandi.weather.adapter.out.kma.dto;

import dandi.dandi.weather.adapter.out.kma.code.KmaResponseCode;

public class WeatherResponseHeader {

    private static final String RESULT_MESSAGE_FORMAT = "기상청 응답 값 (%s:%s)";

    private String resultCode;
    private String resultMsg;

    public WeatherResponseHeader() {
    }

    public WeatherResponseHeader(String resultCode, String resultMsg) {
        this.resultCode = resultCode;
        this.resultMsg = resultMsg;
    }

    public String getResultCode() {
        return resultCode;
    }

    public String getResultMsg() {
        return resultMsg;
    }

    public boolean isSuccessful() {
        KmaResponseCode kmaResponseCode = KmaResponseCode.from(resultCode);
        return kmaResponseCode.isSuccessful();
    }

    public boolean isRetryableError() {
        KmaResponseCode kmaResponseCode = KmaResponseCode.from(resultCode);
        return kmaResponseCode.isRetryable();
    }

    public boolean isNoDataLocationError() {
        KmaResponseCode kmaResponseCode = KmaResponseCode.from(resultCode);
        return kmaResponseCode.isErrorAssociatedWithLocation();
    }

    public String getResultMessage() {
        return String.format(RESULT_MESSAGE_FORMAT, resultCode, resultMsg);
    }
}

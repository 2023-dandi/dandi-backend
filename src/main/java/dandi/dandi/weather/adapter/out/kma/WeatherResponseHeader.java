package dandi.dandi.weather.adapter.out.kma;

public class WeatherResponseHeader {

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
}

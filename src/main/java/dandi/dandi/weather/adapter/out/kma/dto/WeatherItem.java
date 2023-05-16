package dandi.dandi.weather.adapter.out.kma.dto;

public class WeatherItem {

    private String baseDate;
    private String baseTime;
    private String category;
    private String fcstDate;
    private String fcstTime;
    private String fcstValue;
    private String nx;
    private String ny;

    public WeatherItem() {
    }

    public WeatherItem(String baseDate, String baseTime, String category, String fcstDate, String fcstTime,
                       String fcstValue, String nx, String ny) {
        this.baseDate = baseDate;
        this.baseTime = baseTime;
        this.category = category;
        this.fcstDate = fcstDate;
        this.fcstTime = fcstTime;
        this.fcstValue = fcstValue;
        this.nx = nx;
        this.ny = ny;
    }

    public String getBaseDate() {
        return baseDate;
    }

    public String getBaseTime() {
        return baseTime;
    }

    public String getCategory() {
        return category;
    }

    public String getFcstDate() {
        return fcstDate;
    }

    public String getFcstTime() {
        return fcstTime;
    }

    public String getFcstValue() {
        return fcstValue;
    }

    public String getNx() {
        return nx;
    }

    public String getNy() {
        return ny;
    }
}

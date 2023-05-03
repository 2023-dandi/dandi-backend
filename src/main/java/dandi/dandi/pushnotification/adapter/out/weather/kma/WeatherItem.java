package dandi.dandi.pushnotification.adapter.out.weather.kma;

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

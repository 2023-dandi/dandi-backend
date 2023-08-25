package dandi.dandi.weather.domain;

public class WeatherLocation {

    private Long id;
    private double latitude;
    private double longitude;
    private String firstDistrict;
    private String secondDistrict;
    private String thirdDistrict;

    public Long getId() {
        return id;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}

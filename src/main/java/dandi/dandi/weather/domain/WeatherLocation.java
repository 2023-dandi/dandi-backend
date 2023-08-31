package dandi.dandi.weather.domain;

public class WeatherLocation {

    private Long id;
    private double latitude;
    private double longitude;
    private String firstDistrict;
    private String secondDistrict;
    private String thirdDistrict;

    public WeatherLocation(Long id, double latitude, double longitude,
                           String firstDistrict, String secondDistrict, String thirdDistrict) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.firstDistrict = firstDistrict;
        this.secondDistrict = secondDistrict;
        this.thirdDistrict = thirdDistrict;
    }

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

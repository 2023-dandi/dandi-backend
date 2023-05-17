package dandi.dandi.weather.adapter.out.kma.dto;

import java.util.Objects;

public class WeatherRequest {

    private String serviceKey;
    private String dataType;
    private String base_date;
    private String base_time;
    private int numOfRows;
    private int nx;
    private int ny;

    public WeatherRequest() {
    }

    public WeatherRequest(String serviceKey, String dataType, String base_date, String base_time, int numOfRows, int nx,
                          int ny) {
        this.serviceKey = serviceKey;
        this.dataType = dataType;
        this.base_date = base_date;
        this.base_time = base_time;
        this.numOfRows = numOfRows;
        this.nx = nx;
        this.ny = ny;
    }

    public WeatherRequest(String serviceKey, String dataType, String base_time, int numOfRows, int nx, int ny) {
        this(serviceKey, dataType, null, base_time, numOfRows, nx, ny);
    }

    public String getServiceKey() {
        return serviceKey;
    }

    public String getDataType() {
        return dataType;
    }

    public String getBase_date() {
        return base_date;
    }

    public String getBase_time() {
        return base_time;
    }

    public int getNumOfRows() {
        return numOfRows;
    }

    public int getNx() {
        return nx;
    }

    public int getNy() {
        return ny;
    }

    public WeatherRequest ofBaseDate(String baseDate) {
        return new WeatherRequest(serviceKey, dataType, baseDate, base_time, numOfRows, nx, ny);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof WeatherRequest)) {
            return false;
        }
        WeatherRequest that = (WeatherRequest) o;
        return numOfRows == that.numOfRows && nx == that.nx && ny == that.ny && Objects.equals(serviceKey,
                that.serviceKey) && Objects.equals(dataType, that.dataType) && Objects.equals(base_date,
                that.base_date) && Objects.equals(base_time, that.base_time);
    }

    @Override
    public int hashCode() {
        return Objects.hash(serviceKey, dataType, base_date, base_time, numOfRows, nx, ny);
    }
}

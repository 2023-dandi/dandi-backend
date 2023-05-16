package dandi.dandi.weather.adapter.out.kma;

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

    public WeatherRequest(String serviceKey, String dataType, String base_time, int numOfRows) {
        this(serviceKey, dataType, null, base_time, numOfRows, 0, 0);
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

    public WeatherRequest update(String baseDate, int nx, int ny) {
        return new WeatherRequest(serviceKey, dataType, baseDate, base_time, numOfRows, nx, ny);
    }
}

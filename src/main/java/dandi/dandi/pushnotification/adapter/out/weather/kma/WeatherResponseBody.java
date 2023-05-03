package dandi.dandi.pushnotification.adapter.out.weather.kma;

public class WeatherResponseBody {

    private String dataType;
    private WeatherItems items;
    private int numOfRows;
    private int pageNo;
    private int totalCount;

    public WeatherResponseBody() {
    }

    public String getDataType() {
        return dataType;
    }

    public WeatherItems getItems() {
        return items;
    }

    public int getNumOfRows() {
        return numOfRows;
    }

    public int getPageNo() {
        return pageNo;
    }

    public int getTotalCount() {
        return totalCount;
    }
}

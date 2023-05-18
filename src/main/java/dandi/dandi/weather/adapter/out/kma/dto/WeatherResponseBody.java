package dandi.dandi.weather.adapter.out.kma.dto;

public class WeatherResponseBody {

    private String dataType;
    private WeatherItems items;
    private int numOfRows;
    private int pageNo;
    private int totalCount;

    public WeatherResponseBody() {
    }

    public WeatherResponseBody(String dataType, WeatherItems items, int numOfRows, int pageNo, int totalCount) {
        this.dataType = dataType;
        this.items = items;
        this.numOfRows = numOfRows;
        this.pageNo = pageNo;
        this.totalCount = totalCount;
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

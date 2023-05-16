package dandi.dandi.weather.adapter.out.kma;

import java.util.List;

public class WeatherItems {

    private List<WeatherItem> item;

    public WeatherItems() {
    }

    public WeatherItems(List<WeatherItem> item) {
        this.item = item;
    }

    public List<WeatherItem> getItem() {
        return item;
    }
}

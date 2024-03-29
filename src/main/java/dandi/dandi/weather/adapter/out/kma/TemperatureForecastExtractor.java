package dandi.dandi.weather.adapter.out.kma;

import dandi.dandi.weather.adapter.out.kma.dto.Forecast;
import dandi.dandi.weather.adapter.out.kma.dto.WeatherItem;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class TemperatureForecastExtractor {

    private static final String MIN_TEMPERATURE = "TMN";
    private static final String MAX_TEMPERATURE = "TMX";
    private static final String TEMPERATURE = "TMP";

    public Forecast extract(List<WeatherItem> weatherItems) {
        int minTemperature = findMinTemperature(weatherItems);
        int maxTemperature = findMaxTemperature(weatherItems);
        return new Forecast(minTemperature, maxTemperature);
    }

    private int findMinTemperature(List<WeatherItem> weatherItems) {
        return weatherItems.stream()
                .filter(weatherItem -> weatherItem.getCategory().equals(MIN_TEMPERATURE))
                .findFirst()
                .map(WeatherItem::getFcstValue)
                .map(min -> (int) Math.round(Double.parseDouble(min)))
                .orElseGet(() -> extractMinTemperature(weatherItems));
    }

    private int extractMinTemperature(List<WeatherItem> weatherItems) {
        return extractSortedTemperature(weatherItems).get(0);
    }

    private int findMaxTemperature(List<WeatherItem> weatherItems) {
        return weatherItems.stream()
                .filter(weatherItem -> weatherItem.getCategory().equals(MAX_TEMPERATURE))
                .findFirst()
                .map(WeatherItem::getFcstValue)
                .map(min -> (int) Math.round(Double.parseDouble(min)))
                .orElseGet(() -> extractMaxTemperature(weatherItems));
    }

    private int extractMaxTemperature(List<WeatherItem> weatherItems) {
        List<Integer> sortedTemperatures = extractSortedTemperature(weatherItems);
        return sortedTemperatures.get(sortedTemperatures.size() - 1);
    }

    private List<Integer> extractSortedTemperature(List<WeatherItem> weatherItems) {
        return weatherItems
                .stream()
                .filter(weather -> weather.getCategory().equals(TEMPERATURE))
                .map(WeatherItem::getFcstValue)
                .map(min -> (int) Math.round(Double.parseDouble(min)))
                .sorted()
                .collect(Collectors.toUnmodifiableList());
    }
}

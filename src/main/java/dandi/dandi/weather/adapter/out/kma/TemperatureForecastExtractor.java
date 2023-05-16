package dandi.dandi.weather.adapter.out.kma;

import dandi.dandi.weather.application.port.out.WeatherForecastResponse;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class TemperatureForecastExtractor {

    private static final String MIN_TEMPERATURE = "TMN";
    private static final String MAX_TEMPERATURE = "TMX";
    private static final String TEMPERATURE = "TMP";

    public WeatherForecastResponse extract(List<WeatherItem> weatherItems) {
        int minTemperature = findMinTemperature(weatherItems);
        int maxTemperature = findMaxTemperature(weatherItems);
        return WeatherForecastResponse.ofSuccess(minTemperature, maxTemperature);
    }

    private int findMinTemperature(List<WeatherItem> weatherItems) {
        return weatherItems.stream()
                .filter(weatherItem -> weatherItem.getCategory().equals(MIN_TEMPERATURE))
                .map(WeatherItem::getFcstValue)
                .map(Integer::parseInt)
                .findFirst()
                .orElse(extractMinTemperature(weatherItems));
    }

    private int extractMinTemperature(List<WeatherItem> weatherItems) {
        return extractSortedTemperature(weatherItems).get(0);
    }

    private int findMaxTemperature(List<WeatherItem> weatherItems) {
        return weatherItems.stream()
                .filter(weatherItem -> weatherItem.getCategory().equals(MAX_TEMPERATURE))
                .map(WeatherItem::getFcstValue)
                .map(Integer::parseInt)
                .findFirst()
                .orElse(extractMaxTemperature(weatherItems));
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
                .map(Integer::parseInt)
                .sorted()
                .collect(Collectors.toUnmodifiableList());
    }
}

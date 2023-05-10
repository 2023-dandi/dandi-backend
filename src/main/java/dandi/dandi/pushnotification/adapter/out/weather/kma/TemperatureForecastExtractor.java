package dandi.dandi.pushnotification.adapter.out.weather.kma;

import dandi.dandi.pushnotification.application.port.out.weather.Temperature;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class TemperatureForecastExtractor {

    private static final String MIN_TEMPERATURE = "TMN";
    private static final String MAX_TEMPERATURE = "TMX";
    private static final String TEMPERATURE = "TMP";

    public Temperature extract(String forecastDate, List<WeatherItem> weatherItems) {
        int minTemperature = findMinTemperature(forecastDate, weatherItems);
        int maxTemperature = findMaxTemperature(forecastDate, weatherItems);
        return new Temperature(minTemperature, maxTemperature);
    }

    private int findMinTemperature(String forecastDate, List<WeatherItem> weatherItems) {
        return weatherItems.stream()
                .filter(weatherItem -> weatherItem.getFcstDate().equals(forecastDate))
                .filter(weatherItem -> weatherItem.getCategory().equals(MIN_TEMPERATURE))
                .map(WeatherItem::getFcstValue)
                .map(Integer::parseInt)
                .findFirst()
                .orElse(extractMinTemperature(forecastDate, weatherItems));
    }

    private int extractMinTemperature(String forecastDate, List<WeatherItem> weatherItems) {
        return extractSortedTemperature(forecastDate, weatherItems).get(0);
    }

    private int findMaxTemperature(String forecastDate, List<WeatherItem> weatherItems) {
        return weatherItems.stream()
                .filter(weatherItem -> weatherItem.getFcstDate().equals(forecastDate))
                .filter(weatherItem -> weatherItem.getCategory().equals(MAX_TEMPERATURE))
                .map(WeatherItem::getFcstValue)
                .map(Integer::parseInt)
                .findFirst()
                .orElse(extractMaxTemperature(forecastDate, weatherItems));
    }

    private int extractMaxTemperature(String forecastDate, List<WeatherItem> weatherItems) {
        List<Integer> sortedTemperatures = extractSortedTemperature(forecastDate, weatherItems);
        return sortedTemperatures.get(sortedTemperatures.size() - 1);
    }

    private List<Integer> extractSortedTemperature(String forecastDate, List<WeatherItem> weatherItems) {
        return weatherItems
                .stream()
                .filter(weather -> weather.getFcstDate().equals(forecastDate))
                .filter(weather -> weather.getCategory().equals(TEMPERATURE))
                .map(WeatherItem::getFcstValue)
                .map(Integer::parseInt)
                .sorted()
                .collect(Collectors.toUnmodifiableList());
    }
}

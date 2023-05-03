package dandi.dandi.pushnotification.adapter.out.weather.kma;

import dandi.dandi.pushnotification.application.port.out.weather.Temperature;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class TemperatureExtractor {

    private static final String TEMPERATURE = "TMP";

    public Temperature extract(String forecastDate, List<WeatherItem> weatherItems) {
        List<Integer> sortedTemperatures = extractSortedTemperature(forecastDate, weatherItems);
        int minTemperature = sortedTemperatures.get(0);
        int maxTemperature = sortedTemperatures.get(sortedTemperatures.size() - 1);
        return new Temperature(minTemperature, maxTemperature);
    }

    private List<Integer> extractSortedTemperature(String forecastDate, List<WeatherItem> weatherItems) {
        return weatherItems
                .stream()
                .filter(weather -> weather.getCategory().equals(TEMPERATURE))
                .filter(weather -> weather.getFcstDate().equals(forecastDate))
                .map(WeatherItem::getFcstValue)
                .map(Integer::parseInt)
                .sorted()
                .collect(Collectors.toUnmodifiableList());
    }
}

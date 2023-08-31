package dandi.dandi.weather.adapter.out.kma.extractor;

import dandi.dandi.weather.adapter.out.kma.dto.WeatherItem;
import dandi.dandi.weather.application.port.out.WeatherRequestFatalException;
import dandi.dandi.weather.domain.Weather;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class WeatherExtractors {

    private final List<WeatherExtractor> extractors;

    public WeatherExtractors(List<WeatherExtractor> extractors) {
        this.extractors = extractors;
    }

    public List<Weather> extract(List<WeatherItem> items) {
        Map<LocalDateTime, List<WeatherItem>> weatherItems = classifyByFsctDateTime(items);
        return weatherItems.entrySet()
                .stream()
                .map(this::generateWeather)
                .collect(Collectors.toUnmodifiableList());
    }

    private Map<LocalDateTime, List<WeatherItem>> classifyByFsctDateTime(List<WeatherItem> items) {
        Map<LocalDateTime, List<WeatherItem>> weatherItems = new HashMap<>();
        for (WeatherItem item : items) {
            weatherItems.computeIfAbsent(item.getDateTime(), ignored -> new ArrayList<>())
                    .add(item);
        }
        return weatherItems;
    }

    private Weather generateWeather(Map.Entry<LocalDateTime, List<WeatherItem>> weatherItems) {
        Weather.WeatherBuilder weatherBuilder = new Weather.WeatherBuilder(weatherItems.getKey());
        for (WeatherItem item : weatherItems.getValue()) {
            WeatherExtractor extractor = findExtractor(item.getCategory());
            extractor.setValue(weatherBuilder, item.getFcstValue());
        }
        return weatherBuilder.build();
    }

    private WeatherExtractor findExtractor(String category) {
        return extractors.stream()
                .filter(weatherExtractor -> weatherExtractor.hasCategoryCode(category))
                .findFirst()
                .orElseThrow(() -> new WeatherRequestFatalException("기상청 응답 카테고리 코드를 변환할 수 없습니다."));
    }
}

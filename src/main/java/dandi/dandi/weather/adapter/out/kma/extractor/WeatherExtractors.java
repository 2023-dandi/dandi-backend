package dandi.dandi.weather.adapter.out.kma.extractor;

import dandi.dandi.weather.adapter.out.kma.dto.WeatherItem;
import dandi.dandi.weather.application.port.out.WeatherRequestFatalException;
import dandi.dandi.weather.domain.Weather;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

import static dandi.dandi.weather.adapter.out.kma.KmaConstant.KMA_DATE_FORMATTER;
import static dandi.dandi.weather.adapter.out.kma.KmaConstant.KMA_TIME_FORMATTER;

@Component
public class WeatherExtractors {

    private final List<WeatherExtractor> extractors;

    public WeatherExtractors(List<WeatherExtractor> extractors) {
        this.extractors = extractors;
    }

    public List<Weather> extract(List<WeatherItem> items) {
        Map<FsctDateTimeKey, List<WeatherItem>> weatherItems = classifyByFsctDateTime(items);
        return weatherItems.entrySet()
                .stream()
                .map(this::generateWeather)
                .collect(Collectors.toUnmodifiableList());
    }

    private Map<FsctDateTimeKey, List<WeatherItem>> classifyByFsctDateTime(List<WeatherItem> items) {
        Map<FsctDateTimeKey, List<WeatherItem>> weatherItems = new HashMap<>();
        for (WeatherItem item : items) {
            FsctDateTimeKey key = new FsctDateTimeKey(item.getFcstDate(), item.getFcstTime());
            weatherItems.computeIfAbsent(key, ignored -> new ArrayList<>())
                    .add(item);
        }
        return weatherItems;
    }

    private Weather generateWeather(Map.Entry<FsctDateTimeKey, List<WeatherItem>> weatherItems) {
        LocalDate date = LocalDate.parse(weatherItems.getKey().fsctDate, KMA_DATE_FORMATTER);
        LocalTime time = LocalTime.parse(weatherItems.getKey().fsctTime, KMA_TIME_FORMATTER);
        Weather.WeatherBuilder weatherBuilder = new Weather.WeatherBuilder(date, time);
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

    static class FsctDateTimeKey {
        private final String fsctDate;
        private final String fsctTime;

        public FsctDateTimeKey(String fsctDate, String fsctTime) {
            this.fsctDate = fsctDate;
            this.fsctTime = fsctTime;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            FsctDateTimeKey that = (FsctDateTimeKey) o;
            return Objects.equals(fsctDate, that.fsctDate) && Objects.equals(fsctTime, that.fsctTime);
        }

        @Override
        public int hashCode() {
            return Objects.hash(fsctDate, fsctTime);
        }
    }
}

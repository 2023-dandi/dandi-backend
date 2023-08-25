package dandi.dandi.weather.adapter.out.kma.extractor;

import dandi.dandi.weather.domain.Weather;
import dandi.dandi.weather.domain.WindDirection;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

class WindDirectionExtractorTest {

    private final WindDirectionExtractor windDirectionExtractor = new WindDirectionExtractor();

    @DisplayName("기상청의 풍향 값을 8방위 값을 WeatherBuilder에 설정한다..")
    @Test
    void setValue_WindDirection() {
        Weather.WeatherBuilder weatherBuilder = new Weather.WeatherBuilder(LocalDate.MIN, LocalTime.MIN, 1L);

        windDirectionExtractor.setValue(weatherBuilder, "339");

        Weather weather = weatherBuilder.build();
        assertThat(weather.getWindDirection()).isEqualTo(WindDirection.NW);
    }
}
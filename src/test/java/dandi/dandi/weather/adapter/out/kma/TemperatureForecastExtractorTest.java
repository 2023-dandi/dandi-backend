package dandi.dandi.weather.adapter.out.kma;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import dandi.dandi.weather.adapter.out.kma.dto.TemperatureDto;
import dandi.dandi.weather.adapter.out.kma.dto.WeatherItem;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TemperatureForecastExtractorTest {

    private final TemperatureForecastExtractor temperatureForecastExtractor = new TemperatureForecastExtractor();

    @DisplayName("예보 날짜의 예보 값중 TMX, TMN을 통해 최고 최저 기온을 추출한다.")
    @Test
    void extract_TMX_TMN() {
        List<WeatherItem> weatherItems = List.of(
                generateWeatherItem("TMN", "1600", "7"),
                generateWeatherItem("TMX", "1600", "21")
        );

        TemperatureDto temperatureDto = temperatureForecastExtractor.extract(weatherItems);

        assertAll(
                () -> assertThat(temperatureDto.getMaxTemperature()).isEqualTo(21),
                () -> assertThat(temperatureDto.getMinTemperature()).isEqualTo(7)
        );
    }

    @DisplayName("예보 날짜의 예보 값중 TMX, TMN이 존재하지 않는다면 TMP 값들을 통해 최고 최저 기온을 추출한다.")
    @Test
    void extract_TMP() {
        List<WeatherItem> weatherItems = List.of(
                generateWeatherItem("TMP", "1100", "10"),
                generateWeatherItem("TMP", "1200", "11"),
                generateWeatherItem("TMP", "1300", "6"),
                generateWeatherItem("TMP", "1400", "13"),
                generateWeatherItem("TMP", "1500", "20"),
                generateWeatherItem("TMP", "1600", "7"),
                generateWeatherItem("TMP", "1800", "14"),
                generateWeatherItem("TMP", "1900", "16"),
                generateWeatherItem("POP", "1400", "13"),
                generateWeatherItem("REH", "1400", "13")
        );

        TemperatureDto temperatureDto = temperatureForecastExtractor.extract(weatherItems);

        assertAll(
                () -> assertThat(temperatureDto.getMaxTemperature()).isEqualTo(20),
                () -> assertThat(temperatureDto.getMinTemperature()).isEqualTo(6)
        );
    }

    private WeatherItem generateWeatherItem(String category, String fcstTime, String fcstValue) {
        return new WeatherItem("0503", "1100", category, "0504", fcstTime, fcstValue, "37", "126");
    }
}

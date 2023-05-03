package dandi.dandi.pushnotification.adapter.out.weather.kma;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import dandi.dandi.pushnotification.application.port.out.weather.Temperature;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TemperatureExtractorTest {

    private final TemperatureExtractor temperatureExtractor = new TemperatureExtractor();

    @DisplayName("예보 날짜의 최저 최고 기온을 추출한다.")
    @Test
    void extract() {
        List<WeatherItem> weatherItems = List.of(
                generateWeatherItem("TMP", "0503", "1100", "10"),
                generateWeatherItem("TMP", "0503", "1200", "30"),
                generateWeatherItem("TMP", "0503", "1300", "1"),
                generateWeatherItem("TMP", "0504", "1100", "10"),
                generateWeatherItem("TMP", "0504", "1200", "11"),
                generateWeatherItem("TMP", "0504", "1300", "9"),
                generateWeatherItem("TMP", "0504", "1400", "13"),
                generateWeatherItem("TMP", "0504", "1500", "21"),
                generateWeatherItem("TMP", "0504", "1600", "7"),
                generateWeatherItem("TMP", "0504", "1700", "13"),
                generateWeatherItem("TMP", "0504", "1800", "14"),
                generateWeatherItem("TMP", "0504", "1900", "16"),
                generateWeatherItem("POP", "0504", "1400", "13"),
                generateWeatherItem("REH", "0504", "1400", "13")
        );

        Temperature temperature = temperatureExtractor.extract("0504", weatherItems);

        assertAll(
                () -> assertThat(temperature.getMaxTemperature()).isEqualTo(21),
                () -> assertThat(temperature.getMinTemperature()).isEqualTo(7)
        );
    }

    private WeatherItem generateWeatherItem(String category, String fcstDate,
                                            String fcstTime, String fcstValue) {
        return new WeatherItem("0503", "1100", category, fcstDate, fcstTime, fcstValue, "37", "126");
    }
}

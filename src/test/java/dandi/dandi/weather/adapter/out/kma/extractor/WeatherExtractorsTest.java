package dandi.dandi.weather.adapter.out.kma.extractor;

import dandi.dandi.weather.adapter.out.kma.dto.WeatherItem;
import dandi.dandi.weather.domain.Weather;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static dandi.dandi.weather.adapter.out.kma.code.CategoryCode.*;
import static dandi.dandi.weather.domain.PrecipitationType.RAIN;
import static dandi.dandi.weather.domain.Sky.CLOUDY;
import static dandi.dandi.weather.domain.WindDirection.NW;
import static dandi.dandi.weather.domain.WindDirection.SE;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

class WeatherExtractorsTest {

    private final List<WeatherExtractor> extractors = List.of(
            new DoNothingExtractor(), new PrecipitationAmountExtractor(), new PrecipitationTypeExtractor(),
            new SkyTypeExtractor(), new WindSpeedExtractor(), new HumidityExtractor(), new WindDirectionExtractor(),
            new PrecipitationPossibilityExtractor(), new TemperatureExtractor()
    );

    private final WeatherExtractors weatherExtractors = new WeatherExtractors(extractors);

    @Test
    void extract() {
        long weatherLocationId = 1L;
        List<WeatherItem> weatherItems = generateWeatherItems();

        List<Weather> actual = weatherExtractors.extract(weatherItems);

        assertThat(actual).containsAll(generateExpectedWeathers());
    }

    private List<WeatherItem> generateWeatherItems() {
        String firstFsctTime = "0500";
        String secondFsctTime = "0600";
        return List.of(
                generateWeatherItem(firstFsctTime, TMP.name(), "15"),
                generateWeatherItem(firstFsctTime, POP.name(), "60"),
                generateWeatherItem(firstFsctTime, PTY.name(), "1"),
                generateWeatherItem(firstFsctTime, PCP.name(), "1.0mm"),
                generateWeatherItem(firstFsctTime, REH.name(), "70"),
                generateWeatherItem(firstFsctTime, VEC.name(), "165"),
                generateWeatherItem(firstFsctTime, WSD.name(), "2.0"),
                generateWeatherItem(firstFsctTime, SKY.name(), "3"),
                generateWeatherItem(secondFsctTime, TMP.name(), "16"),
                generateWeatherItem(secondFsctTime, POP.name(), "50"),
                generateWeatherItem(secondFsctTime, PTY.name(), "1"),
                generateWeatherItem(secondFsctTime, PCP.name(), "2.0mm"),
                generateWeatherItem(secondFsctTime, REH.name(), "80"),
                generateWeatherItem(secondFsctTime, VEC.name(), "339"),
                generateWeatherItem(secondFsctTime, WSD.name(), "1.5"),
                generateWeatherItem(secondFsctTime, SKY.name(), "3")
        );
    }

    private WeatherItem generateWeatherItem(String fsctTime, String category, String fsctValue) {
        return new WeatherItem("20230825", "0400", category, "20230825", fsctTime, fsctValue, "60", "128");
    }

    private List<Weather> generateExpectedWeathers() {
        return List.of(
                new Weather.WeatherBuilder(LocalDateTime.of(2023, 8, 25, 5, 0))
                        .temperature(15.0)
                        .precipitationPossibility(60)
                        .precipitationType(RAIN)
                        .precipitationAmount(1.0)
                        .humidity(70)
                        .windDirection(SE)
                        .windSpeed(2.0)
                        .sky(CLOUDY)
                        .build(),
                new Weather.WeatherBuilder(LocalDateTime.of(2023, 8, 25, 6, 0))
                        .temperature(16.0)
                        .precipitationPossibility(50)
                        .precipitationType(RAIN)
                        .precipitationAmount(2.0)
                        .humidity(80)
                        .windDirection(NW)
                        .windSpeed(1.5)
                        .sky(CLOUDY)
                        .build()
        );
    }
}

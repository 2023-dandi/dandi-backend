package dandi.dandi.weather.adapter.out.persistence.jpa;

import dandi.dandi.weather.domain.PrecipitationType;
import dandi.dandi.weather.domain.Sky;
import dandi.dandi.weather.domain.Weather;
import dandi.dandi.weather.domain.WindDirection;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "weather")
public class WeatherJpaEntity {

    @EmbeddedId
    LocationAndDateTime locationAndDateTime;
    private double temperature;

    @Enumerated(value = EnumType.STRING)
    private Sky sky;

    private int humidity;

    @Enumerated(value = EnumType.STRING)
    private PrecipitationType precipitationType;
    private int precipitationPossibility;
    private double precipitationAmount;

    @Enumerated(value = EnumType.STRING)
    private WindDirection windDirection;
    private double windSpeed;
    private LocalDateTime forecastedAt;

    protected WeatherJpaEntity() {
    }

    private WeatherJpaEntity(LocationAndDateTime locationAndDateTime, double temperature,
                             Sky sky, int humidity, PrecipitationType precipitationType, int precipitationPossibility,
                             double precipitationAmount, WindDirection windDirection, double windSpeed,
                             LocalDateTime forecastedAt) {
        this.locationAndDateTime = locationAndDateTime;
        this.temperature = temperature;
        this.sky = sky;
        this.humidity = humidity;
        this.precipitationType = precipitationType;
        this.precipitationPossibility = precipitationPossibility;
        this.precipitationAmount = precipitationAmount;
        this.windDirection = windDirection;
        this.windSpeed = windSpeed;
        this.forecastedAt = forecastedAt;
    }

    public static WeatherJpaEntity ofWeather(Weather weather, long weatherLocationId) {
        return new WeatherJpaEntity(new LocationAndDateTime(weatherLocationId, weather.getDateTime()), weather.getTemperature(),
                weather.getSky(), weather.getHumidity(), weather.getPrecipitationType(),
                weather.getPrecipitationPossibility(), weather.getPrecipitationAmount(), weather.getWindDirection(),
                weather.getWindSpeed(), weather.getForecastedAt());
    }

    public Weather toWeather() {
        return new Weather.WeatherBuilder(locationAndDateTime.getDateTime())
                .temperature(temperature)
                .sky(sky)
                .humidity(humidity)
                .precipitationType(precipitationType)
                .precipitationPossibility(precipitationPossibility)
                .precipitationAmount(precipitationAmount)
                .windDirection(windDirection)
                .windSpeed(windSpeed)
                .forecastedAt(forecastedAt)
                .build();
    }
}

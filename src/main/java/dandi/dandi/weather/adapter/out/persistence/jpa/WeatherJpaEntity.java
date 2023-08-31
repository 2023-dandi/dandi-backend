package dandi.dandi.weather.adapter.out.persistence.jpa;

import dandi.dandi.weather.domain.PrecipitationType;
import dandi.dandi.weather.domain.Sky;
import dandi.dandi.weather.domain.Weather;
import dandi.dandi.weather.domain.WindDirection;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "weather")
public class WeatherJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "weather_id")
    private Long id;

    private long weatherLocationId;
    private LocalDate date;
    private LocalTime time;
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

    protected WeatherJpaEntity() {
    }

    public WeatherJpaEntity(Long id, long weatherLocationId, LocalDate date, LocalTime time, double temperature,
                            Sky sky, int humidity, PrecipitationType precipitationType, int precipitationPossibility,
                            double precipitationAmount, WindDirection windDirection, double windSpeed) {
        this.id = id;
        this.weatherLocationId = weatherLocationId;
        this.date = date;
        this.time = time;
        this.temperature = temperature;
        this.sky = sky;
        this.humidity = humidity;
        this.precipitationType = precipitationType;
        this.precipitationPossibility = precipitationPossibility;
        this.precipitationAmount = precipitationAmount;
        this.windDirection = windDirection;
        this.windSpeed = windSpeed;
    }

    public static WeatherJpaEntity ofWeather(Weather weather, long weatherLocationId) {
        return new WeatherJpaEntity(null, weatherLocationId,
                weather.getDate(), weather.getTime(), weather.getTemperature(), weather.getSky(),
                weather.getHumidity(), weather.getPrecipitationType(), weather.getPrecipitationPossibility(),
                weather.getPrecipitationAmount(), weather.getWindDirection(), weather.getWindSpeed());
    }

    public Weather toWeather() {
        return new Weather.WeatherBuilder(date, time)
                .temperature(temperature)
                .sky(sky)
                .humidity(humidity)
                .precipitationType(precipitationType)
                .precipitationPossibility(precipitationPossibility)
                .precipitationAmount(precipitationAmount)
                .windDirection(windDirection)
                .windSpeed(windSpeed)
                .build();
    }
}

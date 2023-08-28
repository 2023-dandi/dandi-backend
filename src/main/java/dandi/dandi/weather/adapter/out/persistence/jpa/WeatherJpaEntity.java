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

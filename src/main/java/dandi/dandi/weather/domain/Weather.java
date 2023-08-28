package dandi.dandi.weather.domain;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

public class Weather {

    private final LocalDate date;
    private final LocalTime time;
    private final double temperature;
    private final Sky sky;
    private final int humidity;
    private final PrecipitationType precipitationType;
    private final int precipitationPossibility;
    private final double precipitationAmount;
    private final WindDirection windDirection;
    private final double windSpeed; // m/s

    public Weather(WeatherBuilder builder) {
        this.date = builder.date;
        this.time = builder.time;
        this.temperature = builder.temperature;
        this.sky = builder.sky;
        this.humidity = builder.humidity;
        this.precipitationPossibility = builder.precipitationPossibility;
        this.precipitationType = builder.precipitationType;
        this.precipitationAmount = builder.precipitationAmount;
        this.windDirection = builder.windDirection;
        this.windSpeed = builder.windSpeed;
    }

    public static class WeatherBuilder {
        private LocalDate date;
        private LocalTime time;
        private double temperature;
        private Sky sky;
        private int humidity;
        private int precipitationPossibility;
        private PrecipitationType precipitationType;
        private double precipitationAmount;
        private WindDirection windDirection;
        private double windSpeed;

        public WeatherBuilder(LocalDate date, LocalTime time) {
            this.date = date;
            this.time = time;
        }

        public WeatherBuilder temperature(double temperature) {
            this.temperature = temperature;
            return this;
        }

        public WeatherBuilder sky(Sky sky) {
            this.sky = sky;
            return this;
        }

        public WeatherBuilder humidity(int humidity) {
            this.humidity = humidity;
            return this;
        }

        public WeatherBuilder precipitationPossibility(int precipitationPossibility) {
            this.precipitationPossibility = precipitationPossibility;
            return this;
        }

        public WeatherBuilder precipitationType(PrecipitationType precipitationType) {
            this.precipitationType = precipitationType;
            return this;
        }

        public WeatherBuilder precipitationAmount(double precipitationAmount) {
            this.precipitationAmount = precipitationAmount;
            return this;
        }

        public WeatherBuilder windDirection(WindDirection windDirection) {
            this.windDirection = windDirection;
            return this;
        }

        public WeatherBuilder windSpeed(double windSpeed) {
            this.windSpeed = windSpeed;
            return this;
        }

        public Weather build() {
            return new Weather(this);
        }
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalTime getTime() {
        return time;
    }

    public double getTemperature() {
        return temperature;
    }

    public Sky getSky() {
        return sky;
    }

    public int getHumidity() {
        return humidity;
    }

    public PrecipitationType getPrecipitationType() {
        return precipitationType;
    }

    public int getPrecipitationPossibility() {
        return precipitationPossibility;
    }

    public double getPrecipitationAmount() {
        return precipitationAmount;
    }

    public WindDirection getWindDirection() {
        return windDirection;
    }

    public double getWindSpeed() {
        return windSpeed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Weather weather = (Weather) o;
        return Double.compare(temperature, weather.temperature) == 0 && humidity == weather.humidity && precipitationPossibility == weather.precipitationPossibility && Double.compare(precipitationAmount, weather.precipitationAmount) == 0 && Double.compare(windSpeed, weather.windSpeed) == 0 && Objects.equals(date, weather.date) && Objects.equals(time, weather.time) && sky == weather.sky && precipitationType == weather.precipitationType && windDirection == weather.windDirection;
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, time, temperature, sky, humidity, precipitationType, precipitationPossibility, precipitationAmount, windDirection, windSpeed);
    }
}

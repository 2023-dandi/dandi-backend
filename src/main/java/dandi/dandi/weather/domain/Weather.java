package dandi.dandi.weather.domain;

import java.time.LocalDateTime;
import java.util.Objects;

public class Weather implements Comparable<Weather> {

    private final LocalDateTime dateTime;
    private final double temperature;
    private final Sky sky;
    private final int humidity;
    private final PrecipitationType precipitationType;
    private final int precipitationPossibility;
    private final double precipitationAmount;
    private final WindDirection windDirection;
    private final double windSpeed; // m/s

    public Weather(WeatherBuilder builder) {
        this.dateTime = builder.dateTime;
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
        private LocalDateTime dateTime;
        private double temperature;
        private Sky sky;
        private int humidity;
        private int precipitationPossibility;
        private PrecipitationType precipitationType;
        private double precipitationAmount;
        private WindDirection windDirection;
        private double windSpeed;

        public WeatherBuilder(LocalDateTime dateTime) {
            this.dateTime = dateTime;
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

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    @Override
    public int compareTo(Weather another) {
        if (dateTime.isBefore(another.dateTime)) {
            return -1;
        } else if (dateTime.isAfter(another.dateTime)) {
            return 1;
        }
        return 0;
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
        return Double.compare(temperature, weather.temperature) == 0 && humidity == weather.humidity &&
                precipitationPossibility == weather.precipitationPossibility &&
                Double.compare(precipitationAmount, weather.precipitationAmount) == 0 &&
                Double.compare(windSpeed, weather.windSpeed) == 0 &&
                Objects.equals(dateTime, weather.dateTime) && sky == weather.sky &&
                precipitationType == weather.precipitationType && windDirection == weather.windDirection;
    }

    @Override
    public int hashCode() {
        return Objects.hash(dateTime, temperature, sky, humidity, precipitationType,
                precipitationPossibility, precipitationAmount, windDirection, windSpeed);
    }
}

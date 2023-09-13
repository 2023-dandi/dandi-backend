package dandi.dandi.weather.domain;

public class WeatherLocation {

    private final Long id;
    private final int x;
    private final int y;

    public WeatherLocation(Long id, int x, int y) {
        this.id = id;
        this.x = x;
        this.y = y;
    }

    public Long getId() {
        return id;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}

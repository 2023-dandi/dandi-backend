package dandi.dandi.weather.domain;

import java.util.List;

public class Weathers {

    private final long weatherLocationId;
    private final List<Weather> values;

    public Weathers(long weatherLocationId, List<Weather> values) {
        this.weatherLocationId = weatherLocationId;
        this.values = values;
    }
}

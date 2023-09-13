package dandi.dandi.weather.adapter.out.persistence.jpa;

import javax.persistence.*;

@Entity
@Table(name = "weather_location")
public class WeatherLocationJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "weather_location_id")
    private Long id;
    private int x;
    private int y;

    protected WeatherLocationJpaEntity() {
    }

    private WeatherLocationJpaEntity(Long id, int x, int y) {
        this.id = id;
        this.x = x;
        this.y = y;
    }

    public WeatherLocationJpaEntity(int x, int y) {
        this(null, x, y);
    }
}

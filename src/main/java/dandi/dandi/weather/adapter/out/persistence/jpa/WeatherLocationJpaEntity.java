package dandi.dandi.weather.adapter.out.persistence.jpa;

import javax.persistence.*;

@Entity
@Table(name = "weather_location")
public class WeatherLocationJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "weather_location_id")
    private Long id;
    private double latitude;
    private double longitude;
    private String firstDistrict;
    private String secondDistrict;
    private String thirdDistrict;

    protected WeatherLocationJpaEntity() {
    }
}

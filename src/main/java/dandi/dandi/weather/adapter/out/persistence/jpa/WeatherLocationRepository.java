package dandi.dandi.weather.adapter.out.persistence.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

public interface WeatherLocationRepository extends JpaRepository<WeatherLocationJpaEntity, Long> {
}

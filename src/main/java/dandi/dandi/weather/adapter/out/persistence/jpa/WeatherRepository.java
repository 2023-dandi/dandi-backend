package dandi.dandi.weather.adapter.out.persistence.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

public interface WeatherRepository extends JpaRepository<WeatherJpaEntity, Long> {
}

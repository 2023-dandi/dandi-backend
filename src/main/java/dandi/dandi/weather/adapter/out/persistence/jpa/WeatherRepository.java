package dandi.dandi.weather.adapter.out.persistence.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface WeatherRepository extends JpaRepository<WeatherJpaEntity, Long> {

	@Modifying
	@Query("DELETE FROM WeatherJpaEntity w WHERE w.weatherLocationId IN :ids")
	void deleteAllByWeatherLocationIds(Iterable<Long> ids);
}

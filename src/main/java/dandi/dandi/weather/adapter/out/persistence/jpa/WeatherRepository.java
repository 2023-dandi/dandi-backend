package dandi.dandi.weather.adapter.out.persistence.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface WeatherRepository extends JpaRepository<WeatherJpaEntity, LocationAndDateTime> {

	@Modifying
	@Query("DELETE FROM WeatherJpaEntity w WHERE w.locationAndDateTime.weatherLocationId IN :ids")
	void deleteAllByWeatherLocationIds(@Param("ids") Iterable<Long> ids);
}

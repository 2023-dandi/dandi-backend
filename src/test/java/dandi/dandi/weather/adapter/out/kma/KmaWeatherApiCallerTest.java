package dandi.dandi.weather.adapter.out.kma;

import static dandi.dandi.weather.adapter.out.kma.KmaConstant.*;

import java.time.LocalDate;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import dandi.dandi.weather.adapter.out.kma.dto.WeatherRequest;

@SpringBootTest
@Disabled
class KmaWeatherApiCallerTest {

	@Autowired
	private KmaWeatherApiCaller apiCaller;

	@Value("${weather.kma.service-key}")
	String kmaServiceKey;

	@Test
	void testKmaWeatherApi() {
		LocalDate baseDate = LocalDate.parse(LocalDate.now().toString(), KMA_DATE_FORMATTER);
		WeatherRequest weatherRequest = new WeatherRequest(kmaServiceKey, "JSON",
			baseDate.toString(), "2000", 990, 60, 127);
		apiCaller.getWeathers(weatherRequest);
	}
}

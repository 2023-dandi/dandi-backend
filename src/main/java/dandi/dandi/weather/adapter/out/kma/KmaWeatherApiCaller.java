package dandi.dandi.weather.adapter.out.kma;

import dandi.dandi.weather.adapter.out.kma.dto.WeatherRequest;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "${feign.client.kma.name}", url = "${feign.client.kma.url}",
        configuration = KmaWeatherFeignConfiguration.class)
public interface KmaWeatherApiCaller {

    @GetMapping
    WeatherResponses getWeathers(@SpringQueryMap(encoded = true) WeatherRequest weatherRequest);
}

package dandi.dandi.weather.adapter.out.kma;

import dandi.dandi.weather.adapter.out.kma.dto.WeatherRequest;
import dandi.dandi.weather.adapter.out.kma.dto.WeatherResponses;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "weather", url = "http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst",
        configuration = KmaWeatherFeignConfiguration.class)
public interface KmaWeatherApiCaller {

    @GetMapping
    WeatherResponses getWeathers(@SpringQueryMap(encoded = true) WeatherRequest weatherRequest);
}

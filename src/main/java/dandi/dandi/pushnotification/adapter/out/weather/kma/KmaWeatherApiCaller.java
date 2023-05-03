package dandi.dandi.pushnotification.adapter.out.weather.kma;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "weather", url = "http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst")
public interface KmaWeatherApiCaller {

    @GetMapping
    WeatherResponses getWeathers(@SpringQueryMap(encoded = true) WeatherRequest weatherRequest);
}

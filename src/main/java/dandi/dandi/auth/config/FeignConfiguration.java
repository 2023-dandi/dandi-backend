package dandi.dandi.auth.config;

import dandi.dandi.auth.adapter.out.oauth.apple.AppleApiCaller;
import dandi.dandi.weather.adapter.out.kma.KmaWeatherApiCaller;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(clients = {AppleApiCaller.class, KmaWeatherApiCaller.class})
public class FeignConfiguration {
}

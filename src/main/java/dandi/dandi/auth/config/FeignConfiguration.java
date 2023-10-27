package dandi.dandi.auth.config;

import dandi.dandi.auth.adapter.out.oauth.apple.client.AppleApiCaller;
import dandi.dandi.weather.adapter.out.kma.client.KmaWeatherApiCaller;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(clients = {AppleApiCaller.class, KmaWeatherApiCaller.class})
public class FeignConfiguration {
}

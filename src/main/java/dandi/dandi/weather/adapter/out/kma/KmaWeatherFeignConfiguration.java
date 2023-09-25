package dandi.dandi.weather.adapter.out.kma;

import feign.codec.Decoder;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.HttpMessageConverterCustomizer;
import org.springframework.context.annotation.Bean;

public class KmaWeatherFeignConfiguration {

    @Bean
    Decoder kmaDecoder(ObjectFactory<HttpMessageConverters> messageConverters,
                             ObjectProvider<HttpMessageConverterCustomizer> customizers) {
        return new XmlToJsonDecoder(messageConverters, customizers);
    }
}

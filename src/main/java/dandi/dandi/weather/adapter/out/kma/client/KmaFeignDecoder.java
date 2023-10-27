package dandi.dandi.weather.adapter.out.kma.client;

import dandi.dandi.weather.adapter.out.kma.DataPortalErrorWeatherResponse;
import dandi.dandi.weather.adapter.out.kma.WeatherResponses;
import dandi.dandi.weather.adapter.out.kma.code.KmaResponseCode;
import dandi.dandi.weather.adapter.out.kma.dto.KmaWeatherResponses;
import dandi.dandi.weather.adapter.out.kma.dto.WeatherResponse;
import dandi.dandi.weather.adapter.out.kma.dto.WeatherResponseHeader;
import feign.FeignException;
import feign.Response;
import feign.Util;
import feign.codec.Decoder;
import feign.optionals.OptionalDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.HttpMessageConverterCustomizer;
import org.springframework.cloud.openfeign.support.ResponseEntityDecoder;
import org.springframework.cloud.openfeign.support.SpringDecoder;

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.Collection;

public class KmaFeignDecoder implements Decoder {

    private static final Logger logger = LoggerFactory.getLogger(KmaFeignDecoder.class);
    private static final String CONTENT_TYPE = "content-type";
    private static final String TEXT_XML = "text/xml";
    private static final String TEXT_XML_EXCEPTION_MESSAGE_FORMAT = "공공 데이터 포털 에러 XML 응답\r\n%s";

    private final Decoder delegate;

    public KmaFeignDecoder(ObjectFactory<HttpMessageConverters> messageConverters,
                           ObjectProvider<HttpMessageConverterCustomizer> customizers) {
        delegate = new OptionalDecoder(new ResponseEntityDecoder(
                new SpringDecoder(messageConverters, customizers)));
    }

    @Override
    public Object decode(Response response, Type type) throws IOException, FeignException {
        if (hasTextXmlContentType(response)) {
            String xmlContent = decodeXmlResponseBody(response);
            return decodeXmlResponse(xmlContent);
        }
        return delegate.decode(response, KmaWeatherResponses.class);
    }

    private WeatherResponses decodeXmlResponse(String xmlContent) {
        return KmaResponseCode.findByNameContainedInValueOrElseNull(xmlContent)
                .map(this::generateKmaWeatherResponse)
                .orElseGet(() -> generateDataPortalErrorWeatherResponse(xmlContent));
    }

    private WeatherResponses generateKmaWeatherResponse(KmaResponseCode kmaResponseCode) {
        WeatherResponseHeader weatherResponseHeader =
                new WeatherResponseHeader(kmaResponseCode.getResultCode(), kmaResponseCode.getErrorMessage());
        return new KmaWeatherResponses(new WeatherResponse(weatherResponseHeader, null));
    }

    private DataPortalErrorWeatherResponse generateDataPortalErrorWeatherResponse(String xmlContent) {
        String exceptionMessage = String.format(TEXT_XML_EXCEPTION_MESSAGE_FORMAT, xmlContent);
        logger.info(exceptionMessage);
        return DataPortalErrorWeatherResponse.fromXmlContent(xmlContent);
    }

    private String decodeXmlResponseBody(Response response) throws IOException {
        Response.Body body = response.body();
        Reader bodyReader = body.asReader(StandardCharsets.UTF_8);
        return Util.toString(bodyReader);
    }

    private boolean hasTextXmlContentType(Response response) {
        Collection<String> contentTypes = response.headers().get(CONTENT_TYPE);
        return contentTypes.stream()
                .anyMatch(contentType -> contentType.contains(TEXT_XML));
    }
}

package dandi.dandi.weather.adapter.out.kma;

import dandi.dandi.weather.adapter.out.kma.dto.WeatherResponse;
import dandi.dandi.weather.adapter.out.kma.dto.WeatherResponseHeader;
import dandi.dandi.weather.adapter.out.kma.dto.WeatherResponses;
import dandi.dandi.weather.application.port.out.WeatherRequestFatalException;
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

public class XmlToJsonDecoder implements Decoder {

    private static final Logger logger = LoggerFactory.getLogger(XmlToJsonDecoder.class);
    private static final String CONTENT_TYPE = "content-type";
    private static final String TEXT_XML = "text/xml";
    private static final String HTTP_ROUTING_ERROR = "HTTP ROUTING ERROR";
    private static final WeatherResponses HTTP_ROUTING_ERROR_WEATHER_RESPONSES = new WeatherResponses(
            new WeatherResponse(new WeatherResponseHeader("100", "HTTP_ROUTING_ERROR"), null));

    private final Decoder delegate;

    public XmlToJsonDecoder(ObjectFactory<HttpMessageConverters> messageConverters,
                            ObjectProvider<HttpMessageConverterCustomizer> customizers) {
        delegate = new OptionalDecoder(new ResponseEntityDecoder(
                new SpringDecoder(messageConverters, customizers)));
    }

    @Override
    public Object decode(Response response, Type type) throws IOException, FeignException {
        if (hasXmlResponseBody(response)) {
            return decodeXmlResponseBody(response);
        }
        return delegate.decode(response, type);
    }

    private WeatherResponses decodeXmlResponseBody(Response response) throws IOException {
        Response.Body body = response.body();
        Reader bodyReader = body.asReader(StandardCharsets.UTF_8);
        String xmlContent = Util.toString(bodyReader);
        logger.info("공공 데이터 포털 실패 xml 응답\r\n{}", xmlContent);
        if (!xmlContent.contains(HTTP_ROUTING_ERROR)) {
            throw new WeatherRequestFatalException("Unable To Decode XML Response\r\n" + xmlContent);
        }
        return HTTP_ROUTING_ERROR_WEATHER_RESPONSES;
    }

    private boolean hasXmlResponseBody(Response response) {
        return response.headers().containsKey(CONTENT_TYPE) &&
                response.headers().get(CONTENT_TYPE).contains(TEXT_XML);
    }
}

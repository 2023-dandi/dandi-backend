package dandi.dandi.weather.adapter.out.kma;

import dandi.dandi.weather.application.port.out.WeatherRequestRetryableException;
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
        if (hasXmlResponseBody(response)) {
            String xmlContent = decodeXmlResponseBody(response);
            String exceptionMessage = String.format(TEXT_XML_EXCEPTION_MESSAGE_FORMAT, xmlContent);
            logger.info(exceptionMessage);
            throw new WeatherRequestRetryableException(exceptionMessage);
        }
        return delegate.decode(response, type);
    }

    private String decodeXmlResponseBody(Response response) throws IOException {
        Response.Body body = response.body();
        Reader bodyReader = body.asReader(StandardCharsets.UTF_8);
        return Util.toString(bodyReader);
    }

    private boolean hasXmlResponseBody(Response response) {
        return response.headers().containsKey(CONTENT_TYPE) &&
                response.headers().get(CONTENT_TYPE).contains(TEXT_XML);
    }
}

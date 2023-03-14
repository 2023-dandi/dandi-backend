package dandi.dandi.post.application.port.in;

import static dandi.dandi.post.PostFixture.ADDITIONAL_OUTFIT_FEELING_INDICES;
import static dandi.dandi.post.PostFixture.MAX_TEMPERATURE;
import static dandi.dandi.post.PostFixture.MIN_TEMPERATURE;
import static dandi.dandi.post.PostFixture.OUTFIT_FEELING_INDEX;
import static dandi.dandi.post.PostFixture.POST_IMAGE_URL;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

class PostRegisterCommandTest {

    private static final String MIN_TEMPERATURE_NULL_EXCEPTION_MESSAGE = "최저 기온이 null입니다.";
    private static final String MAX_TEMPERATURE_NULL_EXCEPTION_MESSAGE = "최고 기온이 null입니다.";
    private static final String POST_IMAGE_URL_NULL_OR_BLANK_EXCEPTION_MESSAGE = "게시글 사진이 null 혹은 빈문자열입니다.";
    private static final String OUTFIT_FEELING_INDEX_NULL_EXCEPTION_MESSAGE = "착장 느낌이 null입니다.";
    private static final String OUTFIT_FEELING_INDEX_RANGE_EXCEPTION_MESSAGE = "착장 느낌은 1 ~ 5의 값입니다.";

    @DisplayName("null인 최저 혹은 최고 기온으로 PostRegisterCommand를 생성하려하면 예외를 발생시킨다.")
    @ParameterizedTest
    @MethodSource("provideNullTemperatureAndExpectedExceptionMessage")
    void create_NullTemperatures(Double minTemperature, Double maxTemperature, String expectedExceptionMessage) {
        assertThatThrownBy(() ->
                new PostRegisterCommand(
                        minTemperature, maxTemperature, POST_IMAGE_URL,
                        OUTFIT_FEELING_INDEX, ADDITIONAL_OUTFIT_FEELING_INDICES))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(expectedExceptionMessage);
    }

    private static Stream<Arguments> provideNullTemperatureAndExpectedExceptionMessage() {
        return Stream.of(
                Arguments.of(null, MAX_TEMPERATURE, MIN_TEMPERATURE_NULL_EXCEPTION_MESSAGE),
                Arguments.of(MIN_TEMPERATURE, null, MAX_TEMPERATURE_NULL_EXCEPTION_MESSAGE)
        );
    }

    @DisplayName("Null 혹은 빈 문자열로 이뤄진 게시글 사진으로 PostRegisterCommand를 생성하려하면 예외를 발생시킨다.")
    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"", " "})
    void create_NullOrBlankPostImageUrl(String postImageUrl) {
        assertThatThrownBy(() ->
                new PostRegisterCommand(
                        MIN_TEMPERATURE, MAX_TEMPERATURE, postImageUrl,
                        OUTFIT_FEELING_INDEX, ADDITIONAL_OUTFIT_FEELING_INDICES))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(POST_IMAGE_URL_NULL_OR_BLANK_EXCEPTION_MESSAGE);
    }

    @DisplayName("null인 착장에 대한 느낌 Index으로 PostRegisterCommand를 생성하려하면 예외를 발생시킨다.")
    @ParameterizedTest
    @NullSource
    void create_NullOutfitFeeling(Long nullOutfitFeeling) {
        assertThatThrownBy(() ->
                new PostRegisterCommand(
                        MIN_TEMPERATURE, MAX_TEMPERATURE, POST_IMAGE_URL,
                        nullOutfitFeeling, ADDITIONAL_OUTFIT_FEELING_INDICES))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(OUTFIT_FEELING_INDEX_NULL_EXCEPTION_MESSAGE);
    }

    @DisplayName("1 ~ 5 가 아닌 값의 착창에 대한 느낌 Index로 PostRegisterCommand를 생성하려하면 예외를 발생시킨다.")
    @ParameterizedTest
    @ValueSource(longs = {0, 6})
    void create_InvalidRangeOutfitFeeling(Long invalidRangeOutfitFeeling) {
        assertThatThrownBy(() ->
                new PostRegisterCommand(
                        MIN_TEMPERATURE, MAX_TEMPERATURE, POST_IMAGE_URL,
                        invalidRangeOutfitFeeling, ADDITIONAL_OUTFIT_FEELING_INDICES))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(OUTFIT_FEELING_INDEX_RANGE_EXCEPTION_MESSAGE);
    }
}

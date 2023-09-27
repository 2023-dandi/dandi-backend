package dandi.dandi.weather.adapter.out.kma.code;

import static dandi.dandi.weather.adapter.out.kma.code.KmaResponseCode.*;
import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class KmaResponseCodeTest {

	@DisplayName("문자열을 받아 문자열에 포함되는 errorMessage를 가진 KmaResponseCode를 반환한다.")
	@ParameterizedTest
	@CsvSource({"abasdHTTP ROUTING ERRORadq12, HTTP_ROUTING_ERROR", "asdaNORMAL_SERVICEasdsa, NORMAL_SERVICE"})
	void findByNameOrElseNull(String value, KmaResponseCode expected) {
		KmaResponseCode actual = KmaResponseCode.findByNameContainedInValueOrElseNull(value);

		assertThat(actual).isEqualTo(expected);
	}

	@DisplayName("문자열을 받아 문자열에 포함되는 errorMessage를 가진 KmaResponseCode가 없다면 null을 반환한다.")
	@Test
	void findByNameOrElseNull_Null() {
		KmaResponseCode actual = KmaResponseCode.findByNameContainedInValueOrElseNull("abaadq");

		assertThat(actual).isNull();
	}
}
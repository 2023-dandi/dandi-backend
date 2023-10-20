package dandi.dandi.weather.adapter.out.kma.code;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static dandi.dandi.weather.adapter.out.kma.code.KmaResponseCode.NORMAL_SERVICE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class KmaResponseCodeTest {

	@DisplayName("문자열을 받아 문자열에 포함되는 errorMessage를 가진 KmaResponseCode를 반환한다.")
	@Test
	void findByNameOrElseNull() {
		String value = "asdaNORMAL_SERVICEasdsa";

		Optional<KmaResponseCode> actual = KmaResponseCode.findByNameContainedInValueOrElseNull(value);

		assertAll(
				() -> assertThat(actual).isPresent(),
				() -> assertThat(actual.get()).isEqualTo(NORMAL_SERVICE)
		);
	}

	@DisplayName("문자열을 받아 문자열에 포함되는 errorMessage를 가진 KmaResponseCode가 없다면 null을 반환한다.")
	@Test
	void findByNameOrElseNull_Null() {
		Optional<KmaResponseCode> actual = KmaResponseCode.findByNameContainedInValueOrElseNull("abaadq");

		assertThat(actual).isEmpty();
	}
}

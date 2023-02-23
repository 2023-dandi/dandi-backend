package dandi.dandi.member.domain.nicknamegenerator;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class RandomNicknameGeneratorTest {

    private final RandomNicknameGenerator randomNicknameGenerator = new RandomNicknameGenerator();

    @DisplayName("생성되는 닉네임의 길이는 23자 이하이다.")
    @Test
    void generate_Length_LowerThan23() {
        String randomNickname = randomNicknameGenerator.generate();

        assertThat(randomNickname.length()).isLessThan(23);
    }
}
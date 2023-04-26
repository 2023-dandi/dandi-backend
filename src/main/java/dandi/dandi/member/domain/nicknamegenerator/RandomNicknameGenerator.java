package dandi.dandi.member.domain.nicknamegenerator;

import java.util.List;
import java.util.Random;
import java.util.StringJoiner;
import org.springframework.stereotype.Component;

@Component
public class RandomNicknameGenerator implements NicknameGenerator {

    private static final List<String> COLORS =
            List.of("pink", "red", "blue", "orange", "yellow", "green", "blue", "navy", "purple");
    private static final List<String> CLOTHES = List.of("muffler", "umbrella", "gloves", "pants",
            "loafers", "shirt", "socks", "sweats", "sneakers", "tuxedo", "skirt", "scarf", "hoodie", "bag", "short");
    private static final List<String> ANIMALS = List.of("raccoon", "rabbit", "lion", "tiger", "cow",
            "cat", "dog", "giraffe", "zebra", "buffalo", "horse", "monkey");

    private static final String NICKNAME_WORD_DELIMITER = "_";

    private final Random random = new Random();

    @Override
    public String generate() {
        StringJoiner generatedNickname = new StringJoiner(NICKNAME_WORD_DELIMITER);
        generatedNickname.add(generateRandomIndex(COLORS));
        generatedNickname.add(generateRandomIndex(CLOTHES));
        generatedNickname.add(generateRandomIndex(ANIMALS));
        return generatedNickname.toString();
    }

    private String generateRandomIndex(List<String> randomNicknameElements) {
        int randomIndex = random.nextInt(randomNicknameElements.size());
        return randomNicknameElements.get(randomIndex);
    }
}

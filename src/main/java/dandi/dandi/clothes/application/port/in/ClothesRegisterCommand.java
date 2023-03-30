package dandi.dandi.clothes.application.port.in;

import dandi.dandi.clothes.domain.Category;
import dandi.dandi.clothes.domain.Clothes;
import dandi.dandi.clothes.domain.Season;
import java.util.List;
import java.util.stream.Collectors;

public class ClothesRegisterCommand {

    private String category;
    private List<String> seasons;
    private String clothesImageUrl;

    public ClothesRegisterCommand() {
    }

    public ClothesRegisterCommand(String category, List<String> seasons, String clothesImageUrl) {
        this.category = category;
        this.seasons = seasons;
        this.clothesImageUrl = clothesImageUrl;
    }

    public String getCategory() {
        return category;
    }

    public List<String> getSeasons() {
        return seasons;
    }

    public String getClothesImageUrl() {
        return clothesImageUrl;
    }

    public Clothes toClothes(Long memberId) {
        Category category = Category.from(this.category);
        List<Season> seasons = this.seasons.stream()
                .map(Season::from)
                .collect(Collectors.toUnmodifiableList());
        return Clothes.initial(memberId, category, seasons, clothesImageUrl);
    }
}

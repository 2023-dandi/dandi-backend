package dandi.dandi.clothes.domain;

import java.util.List;

public class Clothes {

    private final Long id;
    private final Category category;
    private final List<Season> seasons;
    private final String clothesImageUrl;

    public Clothes(Long id, Category category, List<Season> seasons, String clothesImageUrl) {
        this.id = id;
        this.category = category;
        this.seasons = seasons;
        this.clothesImageUrl = clothesImageUrl;
    }

    public static Clothes initial(Category category, List<Season> seasons, String clothesImageUrl) {
        return new Clothes(null, category, seasons, clothesImageUrl);
    }

    public Category getCategory() {
        return category;
    }

    public List<Season> getSeasons() {
        return seasons;
    }

    public String getClothesImageUrl() {
        return clothesImageUrl;
    }
}

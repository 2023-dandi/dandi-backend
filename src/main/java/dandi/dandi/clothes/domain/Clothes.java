package dandi.dandi.clothes.domain;

import java.util.List;
import java.util.stream.Collectors;

public class Clothes {

    private final Long id;
    private final Long memberId;
    private final Category category;
    private final List<Season> seasons;
    private final String clothesImageUrl;

    public Clothes(Long id, Long memberId, Category category, List<Season> seasons, String clothesImageUrl) {
        this.id = id;
        this.memberId = memberId;
        this.category = category;
        this.seasons = seasons;
        this.clothesImageUrl = clothesImageUrl;
    }

    public static Clothes initial(Long memberId, String category, List<String> seasons, String clothesImageUrl) {
        List<Season> clothesSeasons = seasons.stream()
                .map(Season::from)
                .collect(Collectors.toUnmodifiableList());
        return new Clothes(null, memberId, Category.from(category), clothesSeasons, clothesImageUrl);
    }

    public Long getId() {
        return id;
    }

    public Long getMemberId() {
        return memberId;
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

    public boolean isOwnedBy(Long memberId) {
        return this.memberId.equals(memberId);
    }
}

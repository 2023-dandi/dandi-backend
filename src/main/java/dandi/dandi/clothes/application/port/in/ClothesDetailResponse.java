package dandi.dandi.clothes.application.port.in;

import static dandi.dandi.common.constant.Constant.IMAGE_ACCESS_URL;

import dandi.dandi.clothes.domain.Clothes;
import dandi.dandi.clothes.domain.Season;
import java.util.List;
import java.util.stream.Collectors;

public class ClothesDetailResponse {

    private Long id;
    private String clothesImageUrl;
    private String category;
    private List<String> seasons;

    public ClothesDetailResponse() {
    }

    public ClothesDetailResponse(Clothes clothes) {
        this.id = clothes.getId();
        this.clothesImageUrl = System.getProperty(IMAGE_ACCESS_URL) + clothes.getClothesImageUrl();
        this.category = clothes.getCategory().name();
        this.seasons = clothes.getSeasons()
                .stream()
                .map(Season::name)
                .collect(Collectors.toUnmodifiableList());
    }

    public Long getId() {
        return id;
    }

    public String getClothesImageUrl() {
        return clothesImageUrl;
    }

    public String getCategory() {
        return category;
    }

    public List<String> getSeasons() {
        return seasons;
    }
}

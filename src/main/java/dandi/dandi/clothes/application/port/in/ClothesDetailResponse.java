package dandi.dandi.clothes.application.port.in;

import dandi.dandi.clothes.domain.Clothes;
import dandi.dandi.clothes.domain.Season;
import dandi.dandi.image.application.in.ImageResponse;
import java.util.List;
import java.util.stream.Collectors;

public class ClothesDetailResponse implements ImageResponse {

    private Long id;
    private String clothesImageUrl;
    private String category;
    private List<String> seasons;

    public ClothesDetailResponse() {
    }

    private ClothesDetailResponse(Long id, String clothesImageUrl, String category, List<String> seasons) {
        this.id = id;
        this.clothesImageUrl = clothesImageUrl;
        this.category = category;
        this.seasons = seasons;
    }

    public ClothesDetailResponse(Clothes clothes) {
        this.id = clothes.getId();
        this.clothesImageUrl = clothes.getClothesImageUrl();
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

    @Override
    public ImageResponse addImageAccessUrl(String imageAccessUrl) {
        return new ClothesDetailResponse(id, imageAccessUrl + clothesImageUrl, category, seasons);
    }
}

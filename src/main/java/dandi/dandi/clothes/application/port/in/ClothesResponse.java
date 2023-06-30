package dandi.dandi.clothes.application.port.in;

import dandi.dandi.clothes.domain.Clothes;
import dandi.dandi.image.application.in.ImageResponse;

public class ClothesResponse implements ImageResponse {

    private Long id;
    private String clothesImageUrl;

    public ClothesResponse() {
    }

    private ClothesResponse(Long id, String clothesImageUrl) {
        this.id = id;
        this.clothesImageUrl = clothesImageUrl;
    }

    public ClothesResponse(Clothes clothes) {
        this.id = clothes.getId();
        this.clothesImageUrl = clothes.getClothesImageUrl();
    }

    public Long getId() {
        return id;
    }

    public String getClothesImageUrl() {
        return clothesImageUrl;
    }

    @Override
    public ImageResponse addImageAccessUrl(String imageAccessUrl) {
        return new ClothesResponse(id, clothesImageUrl);
    }
}

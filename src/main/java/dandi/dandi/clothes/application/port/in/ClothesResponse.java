package dandi.dandi.clothes.application.port.in;

import static dandi.dandi.common.constant.Constant.IMAGE_ACCESS_URL;

import dandi.dandi.clothes.domain.Clothes;

public class ClothesResponse {

    private Long id;
    private String clothesImageUrl;

    public ClothesResponse() {
    }

    public ClothesResponse(Clothes clothes) {
        this.id = clothes.getId();
        this.clothesImageUrl = System.getProperty(IMAGE_ACCESS_URL) + clothes.getClothesImageUrl();
    }

    public Long getId() {
        return id;
    }

    public String getClothesImageUrl() {
        return clothesImageUrl;
    }
}

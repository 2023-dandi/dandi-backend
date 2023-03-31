package dandi.dandi.clothes.application.port.in;

public class ClothesResponse {

    private Long id;
    private String clothesImageUrl;

    public ClothesResponse(Long id, String clothesImageUrl) {
        this.id = id;
        this.clothesImageUrl = clothesImageUrl;
    }

    public Long getId() {
        return id;
    }

    public String getClothesImageUrl() {
        return clothesImageUrl;
    }
}

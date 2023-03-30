package dandi.dandi.clothes.application.port.in;

public class ClothesImageRegisterResponse {

    private String clothesImageUrl;

    public ClothesImageRegisterResponse() {
    }

    public ClothesImageRegisterResponse(String clothesImageUrl) {
        this.clothesImageUrl = clothesImageUrl;
    }

    public String getClothesImageUrl() {
        return clothesImageUrl;
    }
}

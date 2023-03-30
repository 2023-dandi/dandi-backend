package dandi.dandi.clothes.application.port.in;

import java.util.List;

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
}

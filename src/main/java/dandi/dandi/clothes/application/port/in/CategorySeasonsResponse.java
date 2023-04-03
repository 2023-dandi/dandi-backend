package dandi.dandi.clothes.application.port.in;

import java.util.List;

public class CategorySeasonsResponse {

    private String category;
    private List<String> seasons;

    public CategorySeasonsResponse(String category, List<String> seasons) {
        this.category = category;
        this.seasons = seasons;
    }

    public CategorySeasonsResponse() {
    }

    public String getCategory() {
        return category;
    }

    public List<String> getSeasons() {
        return seasons;
    }
}

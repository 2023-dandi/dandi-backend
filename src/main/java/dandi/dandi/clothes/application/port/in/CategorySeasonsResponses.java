package dandi.dandi.clothes.application.port.in;

import java.util.List;

public class CategorySeasonsResponses {

    private List<CategorySeasonsResponse> categories;

    public CategorySeasonsResponses() {
    }

    public CategorySeasonsResponses(List<CategorySeasonsResponse> categories) {
        this.categories = categories;
    }

    public List<CategorySeasonsResponse> getCategories() {
        return categories;
    }
}

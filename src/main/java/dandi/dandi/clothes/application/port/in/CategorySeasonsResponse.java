package dandi.dandi.clothes.application.port.in;

import dandi.dandi.clothes.domain.Season;
import java.util.Set;
import java.util.stream.Collectors;

public class CategorySeasonsResponse {

    private String category;
    private Set<String> seasons;

    public CategorySeasonsResponse(String category, Set<Season> seasons) {
        this.category = category;
        this.seasons = seasons.stream()
                .map(Season::name)
                .collect(Collectors.toUnmodifiableSet());
    }

    public CategorySeasonsResponse() {
    }

    public String getCategory() {
        return category;
    }

    public Set<String> getSeasons() {
        return seasons;
    }
}

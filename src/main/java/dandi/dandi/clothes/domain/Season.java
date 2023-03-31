package dandi.dandi.clothes.domain;

import java.util.Arrays;
import java.util.Set;

public enum Season {

    SPRING,
    SUMMER,
    FALL,
    WINTER,
    ;

    private static final String INVALID_SEASON_EXCEPTION_MESSAGE = "존재하지 않는 계절입니다.";
    private static final Set<Season> ALL_SEASONS = Set.of(values());

    public static Season from(String value) {
        return Arrays.stream(values())
                .filter(season -> season.name().equals(value))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException(INVALID_SEASON_EXCEPTION_MESSAGE));
    }

    public static Set<Season> getAllSeasons() {
        return ALL_SEASONS;
    }
}

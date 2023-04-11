package dandi.dandi.clothes.domain;

import static dandi.dandi.clothes.domain.Season.FALL;
import static dandi.dandi.clothes.domain.Season.SPRING;
import static dandi.dandi.clothes.domain.Season.SUMMER;
import static dandi.dandi.clothes.domain.Season.WINTER;

import dandi.dandi.advice.InternalServerException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Set;

public enum Month {

    JANUARY(Set.of(WINTER)),
    FEBRUARY(Set.of(WINTER)),
    MARCH(Set.of(WINTER, SPRING)),
    APRIL(Set.of(SPRING)),
    MAY(Set.of(SPRING, SUMMER)),
    JUNE(Set.of(SUMMER)),
    JULY(Set.of(SUMMER)),
    AUGUST(Set.of(SUMMER)),
    SEPTEMBER(Set.of(SUMMER, FALL)),
    OCTOBER(Set.of(FALL)),
    NOVEMBER(Set.of(FALL, WINTER)),
    DECEMBER(Set.of(WINTER)),
    ;

    private final Set<Season> seasons;

    Month(Set<Season> seasons) {
        this.seasons = seasons;
    }

    public static Month fromDate(LocalDate localDate) {
        return Arrays.stream(values())
                .filter(month -> month.name().equals(localDate.getMonth().name()))
                .findAny()
                .orElseThrow(InternalServerException::weatherNotFound);
    }

    public Set<Season> getSeasons() {
        return seasons;
    }
}

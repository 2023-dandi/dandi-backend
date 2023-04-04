package dandi.dandi.clothes.domain;

import java.util.Arrays;
import java.util.Set;

public enum Category {

    TOP,
    BOTTOM,
    OUTER,
    ONE_PIECE,
    SHOES,
    CAP,
    BAG,
    ETC,
    ;

    private static final String INVALID_CATEGORY_EXCEPTION_MESSAGE = "존재하지 않는 옷 카테고리입니다.";
    private static final Set<Category> ALL_CATEGORIES = Set.of(values());

    public static Set<Category> getAllCategories() {
        return ALL_CATEGORIES;
    }

    public static Category from(String value) {
        return Arrays.stream(values())
                .filter(category -> category.name().equals(value))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException(INVALID_CATEGORY_EXCEPTION_MESSAGE));
    }
}

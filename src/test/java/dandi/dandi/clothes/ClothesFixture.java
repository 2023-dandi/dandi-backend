package dandi.dandi.clothes;

import static dandi.dandi.clothes.domain.Category.TOP;
import static dandi.dandi.clothes.domain.Season.SUMMER;
import static dandi.dandi.member.MemberTestFixture.MEMBER_ID;
import static dandi.dandi.utils.TestImageUtils.IMAGE_ACCESS_URL;

import dandi.dandi.clothes.domain.Clothes;
import java.util.List;

public class ClothesFixture {

    public static final Long CLOTHES_ID = 1L;
    public static final String CLOTHES_CATEGORY = "TOP";
    public static final List<String> CLOTHES_SEASONS = List.of("SPRING", "SUMMER");
    public static final String CLOTHES_IMAGE_URL = "clothes/clothesImageUrl";
    public static final String CLOTHES_IMAGE_FULL_URL = IMAGE_ACCESS_URL + CLOTHES_IMAGE_URL;

    public static final Clothes CLOTHES =
            new Clothes(CLOTHES_ID, MEMBER_ID, TOP, List.of(SUMMER), CLOTHES_IMAGE_URL);
}

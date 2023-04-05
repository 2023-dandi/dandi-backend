package dandi.dandi.post;

import static dandi.dandi.member.MemberTestFixture.MEMBER;
import static dandi.dandi.utils.TestImageUtils.IMAGE_ACCESS_URL;

import dandi.dandi.post.domain.Post;
import dandi.dandi.post.domain.Temperatures;
import dandi.dandi.post.domain.WeatherFeeling;
import java.time.LocalDate;
import java.util.List;

public class PostFixture {

    public static final String POST_IMAGE_DIR = "post/";
    public static final Long POST_ID = 1L;
    public static final double MIN_TEMPERATURE = 20.0;
    public static final double MAX_TEMPERATURE = 30.0;
    public static final Temperatures TEMPERATURES = new Temperatures(MIN_TEMPERATURE, MAX_TEMPERATURE);
    public static final String POST_IMAGE_URL = "postImageUrl";
    public static final String POST_IMAGE_FULL_URL = IMAGE_ACCESS_URL + POST_IMAGE_DIR + POST_IMAGE_URL;
    public static final Long OUTFIT_FEELING_INDEX = 1L;
    public static final List<Long> ADDITIONAL_OUTFIT_FEELING_INDICES = List.of(1L, 2L);
    public static final WeatherFeeling WEATHER_FEELING =
            new WeatherFeeling(OUTFIT_FEELING_INDEX, ADDITIONAL_OUTFIT_FEELING_INDICES);
    private static final List<Long> POST_LIKING_MEMBER_IDS = List.of(1L, 2L);

    public static final Post TEST_POST = new Post(
            POST_ID, MEMBER, TEMPERATURES, POST_IMAGE_URL, WEATHER_FEELING, LocalDate.now(),
            POST_LIKING_MEMBER_IDS);
}

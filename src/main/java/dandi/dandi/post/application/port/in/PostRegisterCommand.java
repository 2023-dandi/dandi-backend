package dandi.dandi.post.application.port.in;

import dandi.dandi.common.validation.SelfValidating;
import java.util.List;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class PostRegisterCommand extends SelfValidating<PostRegisterCommand> {

    private static final String MIN_TEMPERATURE_NULL_EXCEPTION_MESSAGE = "최저 기온이 null입니다.";
    private static final String MAX_TEMPERATURE_NULL_EXCEPTION_MESSAGE = "최고 기온이 null입니다.";
    private static final String POST_IMAGE_URL_NULL_OR_BLANK_EXCEPTION_MESSAGE = "게시글 사진이 null 혹은 빈문자열입니다.";
    private static final String OUTFIT_FEELING_INDEX_NULL_EXCEPTION_MESSAGE = "착장 느낌이 null입니다.";
    private static final String OUTFIT_FEELING_INDEX_RANGE_EXCEPTION_MESSAGE = "착장 느낌은 1 ~ 5의 값입니다.";

    @NotNull(message = MIN_TEMPERATURE_NULL_EXCEPTION_MESSAGE)
    private final Double minTemperature;

    @NotNull(message = MAX_TEMPERATURE_NULL_EXCEPTION_MESSAGE)
    private final Double maxTemperature;

    @NotNull(message = POST_IMAGE_URL_NULL_OR_BLANK_EXCEPTION_MESSAGE)
    @NotBlank(message = POST_IMAGE_URL_NULL_OR_BLANK_EXCEPTION_MESSAGE)
    private final String postImageUrl;

    @NotNull(message = OUTFIT_FEELING_INDEX_NULL_EXCEPTION_MESSAGE)
    @Min(value = 1, message = OUTFIT_FEELING_INDEX_RANGE_EXCEPTION_MESSAGE)
    @Max(value = 5, message = OUTFIT_FEELING_INDEX_RANGE_EXCEPTION_MESSAGE)
    private final Long feelingIndex;

    private final List<Long> additionalFeelingIndexes;

    public PostRegisterCommand(Double minTemperature, Double maxTemperature, String postImageUrl,
                               Long feelingIndex, List<Long> additionalFeelingIndexes) {
        this.minTemperature = minTemperature;
        this.maxTemperature = maxTemperature;
        this.postImageUrl = postImageUrl;
        this.feelingIndex = feelingIndex;
        this.additionalFeelingIndexes = additionalFeelingIndexes;
        this.validateSelf();
    }

    public Double getMinTemperature() {
        return minTemperature;
    }

    public Double getMaxTemperature() {
        return maxTemperature;
    }

    public String getPostImageUrl() {
        return postImageUrl;
    }

    public Long getFeelingIndex() {
        return feelingIndex;
    }

    public List<Long> getAdditionalFeelingIndexes() {
        return additionalFeelingIndexes;
    }
}

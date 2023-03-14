package dandi.dandi.post.domain;

import java.util.List;

public class WeatherFeeling {

    private final Long feelingIndex;
    private final List<Long> additionalFeelingIndices;

    public WeatherFeeling(Long feelingIndex, List<Long> additionalFeelingIndices) {
        this.feelingIndex = feelingIndex;
        this.additionalFeelingIndices = additionalFeelingIndices;
    }

    public Long getFeelingIndex() {
        return feelingIndex;
    }

    public List<Long> getAdditionalFeelingIndices() {
        return additionalFeelingIndices;
    }
}

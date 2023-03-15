package dandi.dandi.post.web.in;

import java.util.List;

public class FeelingRequest {

    private Long feelingIndex;
    private List<Long> additionalFeelingIndices;

    public FeelingRequest() {
    }

    public FeelingRequest(Long feelingIndex, List<Long> additionalFeelingIndices) {
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

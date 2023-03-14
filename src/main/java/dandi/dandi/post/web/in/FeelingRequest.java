package dandi.dandi.post.web.in;

import java.util.List;

public class FeelingRequest {

    private Long feelingIndex;
    private List<Long> additionalFeelingIndexes;

    public FeelingRequest() {
    }

    public FeelingRequest(Long feelingIndex, List<Long> additionalFeelingIndexes) {
        this.feelingIndex = feelingIndex;
        this.additionalFeelingIndexes = additionalFeelingIndexes;
    }

    public Long getFeelingIndex() {
        return feelingIndex;
    }

    public List<Long> getAdditionalFeelingIndexes() {
        return additionalFeelingIndexes;
    }
}

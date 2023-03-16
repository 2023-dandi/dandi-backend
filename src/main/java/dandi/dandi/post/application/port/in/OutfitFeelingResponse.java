package dandi.dandi.post.application.port.in;

import java.util.List;

public class OutfitFeelingResponse {

    private long feelingIndex;
    private List<Long> additionalFeelingIndices;

    public OutfitFeelingResponse() {
    }

    public OutfitFeelingResponse(long feelingIndex, List<Long> additionalFeelingIndices) {
        this.feelingIndex = feelingIndex;
        this.additionalFeelingIndices = additionalFeelingIndices;
    }

    public long getFeelingIndex() {
        return feelingIndex;
    }

    public List<Long> getAdditionalFeelingIndices() {
        return additionalFeelingIndices;
    }
}

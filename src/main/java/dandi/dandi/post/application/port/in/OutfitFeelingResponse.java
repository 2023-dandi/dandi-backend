package dandi.dandi.post.application.port.in;

import java.util.List;

public class OutfitFeelingResponse {

    private long feelingIndex;
    private List<Long> additionalFeelingIndex;

    public OutfitFeelingResponse() {
    }

    public OutfitFeelingResponse(long feelingIndex, List<Long> additionalFeelingIndex) {
        this.feelingIndex = feelingIndex;
        this.additionalFeelingIndex = additionalFeelingIndex;
    }

    public long getFeelingIndex() {
        return feelingIndex;
    }

    public List<Long> getAdditionalFeelingIndex() {
        return additionalFeelingIndex;
    }
}

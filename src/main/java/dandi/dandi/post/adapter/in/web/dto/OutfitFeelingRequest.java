package dandi.dandi.post.adapter.in.web.dto;

import java.util.List;

public class OutfitFeelingRequest {

    private Long feelingIndex;
    private List<Long> additionalFeelingIndices;

    public OutfitFeelingRequest() {
    }

    public OutfitFeelingRequest(Long feelingIndex, List<Long> additionalFeelingIndices) {
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

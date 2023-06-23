package dandi.dandi.post.adapter.in.web.dto;

import dandi.dandi.post.application.port.in.PostRegisterCommand;

public class PostRegisterRequest {

    private String postImageUrl;
    private TemperatureRequest temperatures;
    private OutfitFeelingRequest outfitFeelings;

    public PostRegisterRequest() {
    }

    public PostRegisterRequest(String postImageUrl, TemperatureRequest temperatures,
                               OutfitFeelingRequest outfitFeelings) {
        this.postImageUrl = postImageUrl;
        this.temperatures = temperatures;
        this.outfitFeelings = outfitFeelings;
    }

    public String getPostImageUrl() {
        return postImageUrl;
    }

    public TemperatureRequest getTemperatures() {
        return temperatures;
    }

    public OutfitFeelingRequest getOutfitFeelings() {
        return outfitFeelings;
    }

    public PostRegisterCommand toCommand() {
        return new PostRegisterCommand(
                temperatures.getMin(),
                temperatures.getMax(), postImageUrl,
                outfitFeelings.getFeelingIndex(),
                outfitFeelings.getAdditionalFeelingIndices()
        );
    }
}

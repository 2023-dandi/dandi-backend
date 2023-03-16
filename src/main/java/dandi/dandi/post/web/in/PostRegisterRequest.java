package dandi.dandi.post.web.in;

import dandi.dandi.post.application.port.in.PostRegisterCommand;

public class PostRegisterRequest {

    private String postImageUrl;
    private TemperatureRequest temperatures;
    private FeelingRequest outfitFeelings;

    public PostRegisterRequest() {
    }

    public PostRegisterRequest(String postImageUrl, TemperatureRequest temperatures,
                               FeelingRequest outfitFeelings) {
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

    public FeelingRequest getOutfitFeelings() {
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

package dandi.dandi.post.web.in;

import dandi.dandi.post.application.port.in.PostRegisterCommand;

public class PostRegisterRequest {

    private String postImageUrl;
    private TemperatureRequest temperatureRequest;
    private FeelingRequest feelingRequest;

    public PostRegisterRequest() {
    }

    public PostRegisterRequest(String postImageUrl, TemperatureRequest temperatureRequest,
                               FeelingRequest feelingRequest) {
        this.postImageUrl = postImageUrl;
        this.temperatureRequest = temperatureRequest;
        this.feelingRequest = feelingRequest;
    }

    public String getPostImageUrl() {
        return postImageUrl;
    }

    public TemperatureRequest getTemperatureRequest() {
        return temperatureRequest;
    }

    public FeelingRequest getFeelingRequest() {
        return feelingRequest;
    }

    public PostRegisterCommand toCommand() {
        return new PostRegisterCommand(
                temperatureRequest.getMin(),
                temperatureRequest.getMax(), postImageUrl,
                feelingRequest.getFeelingIndex(),
                feelingRequest.getAdditionalFeelingIndexes()
        );
    }
}

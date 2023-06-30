package dandi.dandi.member.application.port.in;

import dandi.dandi.image.application.in.ImageResponse;

public class ProfileImageUpdateResponse implements ImageResponse {

    private String profileImageUrl;

    public ProfileImageUpdateResponse() {
    }

    public ProfileImageUpdateResponse(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    @Override
    public ImageResponse addImageAccessUrl(String imageAccessUrl) {
        return new ProfileImageUpdateResponse(imageAccessUrl + profileImageUrl);
    }
}

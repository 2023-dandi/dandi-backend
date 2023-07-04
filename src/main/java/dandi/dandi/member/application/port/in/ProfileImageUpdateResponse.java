package dandi.dandi.member.application.port.in;

import static dandi.dandi.common.constant.Constant.IMAGE_ACCESS_URL;

public class ProfileImageUpdateResponse {

    private String profileImageUrl;

    public ProfileImageUpdateResponse() {
    }

    public ProfileImageUpdateResponse(String profileImageUrl) {
        this.profileImageUrl = System.getProperty(IMAGE_ACCESS_URL) + profileImageUrl;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }
}

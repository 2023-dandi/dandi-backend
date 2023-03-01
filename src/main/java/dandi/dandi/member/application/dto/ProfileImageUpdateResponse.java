package dandi.dandi.member.application.dto;

public class ProfileImageUpdateResponse {

    private String profileImageUrl;

    public ProfileImageUpdateResponse() {
    }

    public ProfileImageUpdateResponse(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }
}

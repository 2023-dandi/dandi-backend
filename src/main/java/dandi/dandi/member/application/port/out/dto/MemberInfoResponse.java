package dandi.dandi.member.application.port.out.dto;

import dandi.dandi.member.domain.Member;
import io.swagger.v3.oas.annotations.media.Schema;

public class MemberInfoResponse {

    @Schema(example = "member nickname")
    private String nickname;

    @Schema(example = "37.5064393")
    private double latitude;

    @Schema(example = "126.963687")
    private double longitude;

    @Schema(example = "profileDir/profileImage.jpg")
    private String profileImageUrl;

    public MemberInfoResponse() {
    }

    public MemberInfoResponse(Member member) {
        this.nickname = member.getNickname();
        this.latitude = member.getLatitude();
        this.longitude = member.getLongitude();
        this.profileImageUrl = member.getProfileImgUrl();
    }

    public String getNickname() {
        return nickname;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }
}

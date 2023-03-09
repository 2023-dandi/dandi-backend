package dandi.dandi.member.application.port.in;

import dandi.dandi.member.domain.Member;
import io.swagger.v3.oas.annotations.media.Schema;

public class MemberInfoResponse {

    private String nickname;
    private double latitude;
    private double longitude;
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

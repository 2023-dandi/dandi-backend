package dandi.dandi.member.application.port.in;

import dandi.dandi.image.application.in.ImageResponse;
import dandi.dandi.member.domain.Member;

public class MemberInfoResponse implements ImageResponse {

    private String nickname;
    private double latitude;
    private double longitude;
    private String profileImageUrl;
    private int postCount;

    public MemberInfoResponse() {
    }

    private MemberInfoResponse(String nickname, double latitude, double longitude, String profileImageUrl,
                               int postCount) {
        this.nickname = nickname;
        this.latitude = latitude;
        this.longitude = longitude;
        this.profileImageUrl = profileImageUrl;
        this.postCount = postCount;
    }

    public MemberInfoResponse(Member member, int postCount) {
        this.nickname = member.getNickname();
        this.latitude = member.getLatitude();
        this.longitude = member.getLongitude();
        this.profileImageUrl = member.getProfileImgUrl();
        this.postCount = postCount;
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

    public int getPostCount() {
        return postCount;
    }

    @Override
    public ImageResponse addImageAccessUrl(String imageAccessUrl) {
        return new MemberInfoResponse(nickname, latitude, longitude, imageAccessUrl + profileImageUrl, postCount);
    }
}

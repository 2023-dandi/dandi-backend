package dandi.dandi.member.application.port.in;

import dandi.dandi.member.domain.Member;

public class MemberInfoResponse {

    private String nickname;
    private double latitude;
    private double longitude;
    private String profileImageUrl;

    public MemberInfoResponse() {
    }

    private MemberInfoResponse(String nickname, double latitude, double longitude, String profileImageUrl) {
        this.nickname = nickname;
        this.latitude = latitude;
        this.longitude = longitude;
        this.profileImageUrl = profileImageUrl;
    }

    public MemberInfoResponse(Member member) {
        this.nickname = member.getNickname();
        this.latitude = member.getLatitude();
        this.longitude = member.getLongitude();
        this.profileImageUrl = member.getProfileImgUrl();
    }

    public static MemberInfoResponse fromCustomProfileImageMember(Member member) {
        return new MemberInfoResponse(member.getNickname(), member.getLatitude(),
                member.getLongitude(), member.getProfileImgUrl());
    }

    public static MemberInfoResponse fromInitialProfileImageMember(Member member) {
        return new MemberInfoResponse(member.getNickname(), member.getLatitude(),
                member.getLongitude(), null);
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

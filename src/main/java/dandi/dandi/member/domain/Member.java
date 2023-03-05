package dandi.dandi.member.domain;

public class Member {

    private Long id;

    private String oAuthId;

    private Nickname nickname;

    private Location location;

    private String profileImgUrl;

    public Member(Long id, String oAuthId, Nickname nickname, Location location, String profileImgUrl) {
        this.id = id;
        this.oAuthId = oAuthId;
        this.nickname = nickname;
        this.location = location;
        this.profileImgUrl = profileImgUrl;
    }

    public static Member initial(String oAuthId, String nickname, String initialProfileImageUrl) {
        return new Member(null, oAuthId, Nickname.from(nickname), Location.initial(), initialProfileImageUrl);
    }

    public Long getId() {
        return id;
    }

    public String getOAuthId() {
        return oAuthId;
    }

    public String getNickname() {
        return nickname.getValue();
    }

    public double getLatitude() {
        return location.getLatitude();
    }

    public double getLongitude() {
        return location.getLongitude();
    }

    public boolean hasProfileImgUrl(String profileImgUrl) {
        return this.profileImgUrl.equals(profileImgUrl);
    }

    public String getProfileImgUrl() {
        return profileImgUrl;
    }
}

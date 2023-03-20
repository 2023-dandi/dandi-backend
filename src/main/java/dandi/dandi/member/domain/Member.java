package dandi.dandi.member.domain;

public class Member {

    private final Long id;
    private final String oAuthId;
    private final String nickname;
    private final Location location;
    private final String profileImgUrl;

    public Member(Long id, String oAuthId, String nickname, Location location, String profileImgUrl) {
        this.id = id;
        this.oAuthId = oAuthId;
        this.nickname = nickname;
        this.location = location;
        this.profileImgUrl = profileImgUrl;
    }

    public static Member initial(String oAuthId, String nickname, String initialProfileImageUrl) {
        return new Member(null, oAuthId, nickname, Location.initial(), initialProfileImageUrl);
    }

    public Long getId() {
        return id;
    }

    public boolean hasId(Long id) {
        return this.id.equals(id);
    }

    public String getOAuthId() {
        return oAuthId;
    }

    public String getNickname() {
        return nickname;
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

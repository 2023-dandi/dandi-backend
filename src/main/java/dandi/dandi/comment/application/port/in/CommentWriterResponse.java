package dandi.dandi.comment.application.port.in;

import dandi.dandi.member.domain.Member;

public class CommentWriterResponse {

    private Long id;
    private String nickname;
    private String profileImageUrl;

    public CommentWriterResponse() {
    }

    public CommentWriterResponse(Member member, String imageAccessUrl) {
        this.id = member.getId();
        this.nickname = member.getNickname();
        this.profileImageUrl = imageAccessUrl + member.getProfileImgUrl();
    }

    public Long getId() {
        return id;
    }

    public String getNickname() {
        return nickname;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }
}

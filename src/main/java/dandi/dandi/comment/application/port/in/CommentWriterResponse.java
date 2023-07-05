package dandi.dandi.comment.application.port.in;

import static dandi.dandi.common.constant.Constant.IMAGE_ACCESS_URL;

import dandi.dandi.member.domain.Member;

public class CommentWriterResponse {

    private Long id;
    private String nickname;
    private String profileImageUrl;

    public CommentWriterResponse() {
    }

    public CommentWriterResponse(Member member) {
        this.id = member.getId();
        this.nickname = member.getNickname();
        this.profileImageUrl = System.getProperty(IMAGE_ACCESS_URL) + member.getProfileImgUrl();
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

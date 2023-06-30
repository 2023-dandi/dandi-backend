package dandi.dandi.comment.application.port.in;

import dandi.dandi.member.domain.Member;

public class CommentWriterResponse {

    private Long id;
    private String nickname;
    private String profileImageUrl;

    public CommentWriterResponse() {
    }

    public CommentWriterResponse(Long id, String nickname, String profileImageUrl) {
        this.id = id;
        this.nickname = nickname;
        this.profileImageUrl = profileImageUrl;
    }

    public CommentWriterResponse(Member member) {
        this.id = member.getId();
        this.nickname = member.getNickname();
        this.profileImageUrl = member.getProfileImgUrl();
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

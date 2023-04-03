package dandi.dandi.comment.domain;

import dandi.dandi.member.domain.Member;
import java.time.LocalDate;

public class Comment {

    private final Long id;
    private final String content;
    private final Member writer;
    private final LocalDate createdAt;

    public Comment(Long id, String content, Member writer, LocalDate createdAt) {
        this.id = id;
        this.content = content;
        this.writer = writer;
        this.createdAt = createdAt;
    }

    public static Comment initial(String content) {
        return new Comment(null, content, null, null);
    }

    public String getContent() {
        return content;
    }
}

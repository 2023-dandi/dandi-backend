package dandi.dandi.comment.adapter.persistence;

import dandi.dandi.comment.domain.Comment;
import dandi.dandi.member.domain.Member;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "comment")
@EntityListeners(AuditingEntityListener.class)
public class CommentJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    private Long memberId;

    private Long postId;

    private String content;

    @CreatedDate
    private LocalDateTime createdAt;

    public CommentJpaEntity() {
    }

    private CommentJpaEntity(Long id, Long memberId, Long postId, String content, LocalDateTime createdAt) {
        this.id = id;
        this.memberId = memberId;
        this.postId = postId;
        this.content = content;
        this.createdAt = createdAt;
    }

    public static CommentJpaEntity of(Comment comment, Long postId, Long memberId) {
        return new CommentJpaEntity(null, memberId, postId, comment.getContent(), null);
    }

    public Long getId() {
        return id;
    }

    public Long getMemberId() {
        return memberId;
    }

    public String getContent() {
        return content;
    }

    public Comment toComment(Member member) {
        return new Comment(
                id,
                content,
                member,
                createdAt.toLocalDate()
        );
    }
}

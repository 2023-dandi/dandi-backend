package dandi.dandi.comment.adapter.out.persistence.jpa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "comment_report")
public class CommentReportJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_report_id")
    private Long id;

    private Long memberId;
    private Long commentId;

    protected CommentReportJpaEntity() {
    }

    public CommentReportJpaEntity(Long memberId, Long commentId) {
        this.memberId = memberId;
        this.commentId = commentId;
    }
}

package dandi.dandi.commentreport.adapter;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentReportRepository extends JpaRepository<CommentReportJpaEntity, Long> {

    boolean existsByMemberIdAndCommentId(Long memberId, Long commentId);
}

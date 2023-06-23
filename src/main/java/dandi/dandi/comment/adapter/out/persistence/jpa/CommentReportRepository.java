package dandi.dandi.comment.adapter.out.persistence.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentReportRepository extends JpaRepository<CommentReportJpaEntity, Long> {

    boolean existsByMemberIdAndCommentId(Long memberId, Long commentId);
}

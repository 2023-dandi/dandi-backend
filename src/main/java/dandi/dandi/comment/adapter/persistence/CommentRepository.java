package dandi.dandi.comment.adapter.persistence;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CommentRepository extends JpaRepository<CommentJpaEntity, Long> {

    @Query("SELECT c FROM CommentJpaEntity c WHERE c.postId = :postId "
            + "AND c.memberId NOT IN "
            + "(SELECT mb.blockedMemberId FROM MemberBlockJpaEntity mb WHERE mb.blockingMemberId = :memberId ) "
            + "AND c.memberId NOT IN "
            + "(SELECT mb.blockingMemberId FROM MemberBlockJpaEntity mb WHERE mb.blockedMemberId = :memberId )"
            + "AND c.id NOT IN "
            + "(SELECT cr.commentId FROM CommentReportJpaEntity cr WHERE cr.memberId = :memberId)")
    Slice<CommentJpaEntity> findByPostId(Long memberId, Long postId, Pageable pageable);

    boolean existsById(Long id);
}

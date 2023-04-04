package dandi.dandi.comment.adapter.persistence;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<CommentJpaEntity, Long> {

    Slice<CommentJpaEntity> findByPostId(Long postId, Pageable pageable);
}

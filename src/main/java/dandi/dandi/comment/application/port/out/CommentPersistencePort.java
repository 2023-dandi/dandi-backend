package dandi.dandi.comment.application.port.out;

import dandi.dandi.comment.domain.Comment;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface CommentPersistencePort {

    void save(Comment comment, Long postId, Long memberId);

    Slice<Comment> findByPostId(Long postId, Pageable pageable);

    Optional<Comment> findById(Long id);
}

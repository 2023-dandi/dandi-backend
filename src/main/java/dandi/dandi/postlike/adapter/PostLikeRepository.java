package dandi.dandi.postlike.adapter;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostLikeRepository extends JpaRepository<PostLikeJpaEntity, Long> {

    Optional<PostLikeJpaEntity> findByMemberIdAndPostId(Long memberId, Long postId);

    boolean existsByMemberIdAndPostId(Long memberId, Long postId);

    List<PostLikeJpaEntity> findByPostId(Long postId);
}

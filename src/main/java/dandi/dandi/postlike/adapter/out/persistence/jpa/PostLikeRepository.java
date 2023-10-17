package dandi.dandi.postlike.adapter.out.persistence.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PostLikeRepository extends JpaRepository<PostLikeJpaEntity, Long> {

    @Query("SELECT pl FROM PostLikeJpaEntity pl WHERE pl.postLikeKey.postId = :postId AND pl.postLikeKey.memberId = :memberId")
    Optional<PostLikeJpaEntity> findByMemberIdAndPostId(Long memberId, Long postId);

    @Query("SELECT pl FROM PostLikeJpaEntity pl WHERE pl.postLikeKey.postId = :postId")
    List<PostLikeJpaEntity> findByPostId(Long postId);

    @Modifying
    @Query("DELETE PostLikeJpaEntity pl WHERE pl.postLikeKey.postId = :postId AND pl.postLikeKey.memberId = :memberId")
    void deleteByPostIdAndMemberId(Long postId, Long memberId);
}

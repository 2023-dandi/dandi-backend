package dandi.dandi.postlike.application.port.out;

import dandi.dandi.postlike.domain.PostLike;

import java.util.Optional;

public interface PostLikePersistencePort {

    Optional<PostLike> findByMemberIdAndPostId(Long memberId, Long postId);

    void save(PostLike postLike);

    void deleteByPostIdAndMemberId(Long postId, Long memberId);

    boolean existsByPostIdAndMemberId(Long memberId, Long postId);
}

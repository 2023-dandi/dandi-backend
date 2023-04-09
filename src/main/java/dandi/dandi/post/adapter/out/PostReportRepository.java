package dandi.dandi.post.adapter.out;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PostReportRepository extends JpaRepository<PostReportJpaEntity, Long> {

    boolean existsByMemberIdAndPostId(Long memberId, Long postId);
}

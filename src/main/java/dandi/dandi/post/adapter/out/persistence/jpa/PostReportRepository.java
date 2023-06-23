package dandi.dandi.post.adapter.out.persistence.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PostReportRepository extends JpaRepository<PostReportJpaEntity, Long> {

    boolean existsByMemberIdAndPostId(Long memberId, Long postId);
}

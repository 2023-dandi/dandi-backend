package dandi.dandi.postreport.adapter.persistence;

import dandi.dandi.post.adapter.out.PostReportJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostReportRepository extends JpaRepository<PostReportJpaEntity, Long> {
}

package dandi.dandi.postreport.adapter.persistence;

import dandi.dandi.post.adapter.out.PostReportJpaEntity;
import dandi.dandi.postreport.application.port.out.PostReportPersistencePort;
import org.springframework.stereotype.Component;

@Component
public class PostReportPersistenceAdapter implements PostReportPersistencePort {

    private final PostReportRepository postReportRepository;

    public PostReportPersistenceAdapter(PostReportRepository postReportRepository) {
        this.postReportRepository = postReportRepository;
    }

    @Override
    public void savePostReportOf(Long memberId, Long postId) {
        PostReportJpaEntity postReportJpaEntity = new PostReportJpaEntity(memberId, postId);
        postReportRepository.save(postReportJpaEntity);
    }
}

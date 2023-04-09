package dandi.dandi.post.adapter.out;

import dandi.dandi.post.application.port.out.PostReportPersistencePort;
import org.springframework.stereotype.Component;

@Component
public class PostReportPersistenceAdapter implements PostReportPersistencePort {

    private final PostReportRepository postReportRepository;

    public PostReportPersistenceAdapter(PostReportRepository postReportRepository) {
        this.postReportRepository = postReportRepository;
    }

    @Override
    public boolean existsByMemberIdAndPostId(Long memberId, Long postId) {
        return postReportRepository.existsByMemberIdAndPostId(memberId, postId);
    }

    @Override
    public void savePostReportOf(Long memberId, Long postId) {
        PostReportJpaEntity postReportJpaEntity = new PostReportJpaEntity(memberId, postId);
        postReportRepository.save(postReportJpaEntity);
    }
}

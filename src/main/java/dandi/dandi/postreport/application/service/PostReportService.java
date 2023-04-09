package dandi.dandi.postreport.application.service;

import dandi.dandi.common.exception.NotFoundException;
import dandi.dandi.post.application.port.out.PostPersistencePort;
import dandi.dandi.postreport.application.port.in.PostReportUseCase;
import dandi.dandi.postreport.application.port.out.PostReportPersistencePort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PostReportService implements PostReportUseCase {

    private final PostPersistencePort postPersistencePort;
    private final PostReportPersistencePort postReportPersistencePort;

    public PostReportService(PostPersistencePort postPersistencePort,
                             PostReportPersistencePort postReportPersistencePort) {
        this.postPersistencePort = postPersistencePort;
        this.postReportPersistencePort = postReportPersistencePort;
    }

    @Override
    @Transactional
    public void reportPost(Long memberId, Long postId) {
        validatePostExistence(postId);
        validateAlreadyReported(memberId, postId);
    }

    private void validatePostExistence(Long postId) {
        if (!postPersistencePort.existsById(postId)) {
            throw NotFoundException.post();
        }
    }

    private void validateAlreadyReported(Long memberId, Long postId) {
        if (postReportPersistencePort.existsByMemberIdAndPostId(memberId, postId)) {
            throw new IllegalStateException("이미 신고한 게시글입니다.");
        }
    }
}

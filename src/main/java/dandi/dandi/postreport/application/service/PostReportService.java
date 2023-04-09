package dandi.dandi.postreport.application.service;

import dandi.dandi.common.exception.NotFoundException;
import dandi.dandi.post.application.port.out.PostPersistencePort;
import dandi.dandi.postreport.application.port.in.PostReportUseCase;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PostReportService implements PostReportUseCase {

    private final PostPersistencePort postPersistencePort;

    public PostReportService(PostPersistencePort postPersistencePort) {
        this.postPersistencePort = postPersistencePort;
    }

    @Override
    @Transactional
    public void reportPost(Long memberId, Long postId) {
        validatePostExistence(postId);
    }

    private void validatePostExistence(Long postId) {
        if (!postPersistencePort.existsById(postId)) {
            throw NotFoundException.post();
        }
    }
}

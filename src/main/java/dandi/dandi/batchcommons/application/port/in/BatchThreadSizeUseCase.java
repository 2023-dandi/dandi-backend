package dandi.dandi.batchcommons.application.port.in;

import org.springframework.transaction.annotation.Transactional;

public interface BatchThreadSizeUseCase {
    @Transactional
    void updateBatchThreadSizeByName(BatchThreadSizeUpdateCommand command);
}

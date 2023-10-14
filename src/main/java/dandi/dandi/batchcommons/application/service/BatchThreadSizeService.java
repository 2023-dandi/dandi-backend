package dandi.dandi.batchcommons.application.service;

import dandi.dandi.batchcommons.application.port.in.BatchThreadSizeUpdateCommand;
import dandi.dandi.batchcommons.application.port.in.BatchThreadSizeUseCase;
import dandi.dandi.batchcommons.application.port.out.BatchThreadSizePersistencePort;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
public class BatchThreadSizeService implements BatchThreadSizeUseCase {

    private final String batchAdminKey;
    private final BatchThreadSizePersistencePort batchThreadSizePersistencePort;

    public BatchThreadSizeService(@Value("${spring.batch.admin-key}") String batchAdminKey,
                                  BatchThreadSizePersistencePort batchThreadSizePersistencePort) {
        this.batchAdminKey = batchAdminKey;
        this.batchThreadSizePersistencePort = batchThreadSizePersistencePort;
    }

    @Override
    @Transactional
    public void updateBatchThreadSizeByName(BatchThreadSizeUpdateCommand command) {
        validateBatchAdminKey(command.getBatchAdminKey());
        validateChunkSize(command.getBatchThreadSize());
        batchThreadSizePersistencePort.updateBatchThreadSizeByName(command.getName(), command.getBatchThreadSize());
    }

    private void validateChunkSize(Integer batchThreadSize) {
        if (Objects.isNull(batchThreadSize)) {
            throw new IllegalArgumentException("batchThreadSize가 null입니다.");
        }
        if (batchThreadSize < 0) {
            throw new IllegalArgumentException("batchThreadSize는 1 이상이어야 합니다.");
        }
    }

    private void validateBatchAdminKey(String batchAdminKey) {
        if (!batchAdminKey.equals(this.batchAdminKey)) {
            throw new IllegalArgumentException("batch-key가 잘못되었습니다.");
        }
    }
}

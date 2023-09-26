package dandi.dandi.batchcommons.application.service;

import dandi.dandi.batchcommons.application.port.in.ChunkSizeUpdateCommand;
import dandi.dandi.batchcommons.application.port.in.ChunkSizeUseCase;
import dandi.dandi.batchcommons.application.port.out.ChunkSizePersistencePort;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
public class ChunkSizeService implements ChunkSizeUseCase {

    private final String batchAdminKey;
    private final ChunkSizePersistencePort chunkSizePersistencePort;

    public ChunkSizeService(@Value("${spring.batch.admin-key}") String batchAdminKey,
                            ChunkSizePersistencePort chunkSizePersistencePort) {
        this.batchAdminKey = batchAdminKey;
        this.chunkSizePersistencePort = chunkSizePersistencePort;
    }

    @Override
    @Transactional
    public void updateChunkSizeByName(ChunkSizeUpdateCommand command) {
        validateBatchAdminKey(command.getBatchAdminKey());
        validateChunkSize(command.getChunkSize());
        chunkSizePersistencePort.updateChunkSizeByName(command.getName(), command.getChunkSize());
    }

    private void validateChunkSize(Integer chunkSize) {
        if (Objects.isNull(chunkSize)) {
            throw new IllegalArgumentException("chunkSize가 null입니다.");
        }
        if (chunkSize < 0) {
            throw new IllegalArgumentException("chunkSize는 1 이상이어야 합니다.");
        }
    }

    private void validateBatchAdminKey(String batchAdminKey) {
        if (!batchAdminKey.equals(this.batchAdminKey)) {
            throw new IllegalArgumentException("batch-key가 잘못되었습니다.");
        }
    }
}

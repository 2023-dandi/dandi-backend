package dandi.dandi.batchcommons.adapter.out.persistence.jpa.batchthreadsize;

import dandi.dandi.batchcommons.application.port.out.BatchThreadSizePersistencePort;
import dandi.dandi.common.exception.NotFoundException;
import org.springframework.stereotype.Component;

@Component
public class BatchThreadSizePersistenceAdapter implements BatchThreadSizePersistencePort {

    private final BatchThreadSizeRepository batchThreadSizeRepository;

    public BatchThreadSizePersistenceAdapter(BatchThreadSizeRepository batchThreadSizeRepository) {
        this.batchThreadSizeRepository = batchThreadSizeRepository;
    }

    @Override
    public int findBatchThreadSizeByName(String name) {
        return batchThreadSizeRepository.findByName(name)
                .orElseThrow(() -> NotFoundException.batchThreadSize(name))
                .getValue();
    }

    @Override
    public void updateBatchThreadSizeByName(String name, int batchThreadSize) throws NotFoundException {
        batchThreadSizeRepository.updateBatchThreadSizeByName(name, batchThreadSize);
    }
}

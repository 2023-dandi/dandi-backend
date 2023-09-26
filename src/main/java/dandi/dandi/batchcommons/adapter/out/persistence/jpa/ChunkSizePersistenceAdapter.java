package dandi.dandi.batchcommons.adapter.out.persistence.jpa;

import dandi.dandi.batchcommons.application.port.out.ChunkSizePersistencePort;
import dandi.dandi.common.exception.NotFoundException;
import org.springframework.stereotype.Component;

@Component
public class ChunkSizePersistenceAdapter implements ChunkSizePersistencePort {

    private final ChunkSizeRepository chunkSizeRepository;

    public ChunkSizePersistenceAdapter(ChunkSizeRepository chunkSizeRepository) {
        this.chunkSizeRepository = chunkSizeRepository;
    }

    @Override
    public int findChunkSizeByName(String name) {
        return chunkSizeRepository.findByName(name)
                .orElseThrow(() -> NotFoundException.chunkSize(name))
                .getValue();
    }

    @Override
    public void updateChunkSizeByName(String name, int chunkSize) throws NotFoundException {
        chunkSizeRepository.updateChunkSizeByName(name, chunkSize);
    }
}

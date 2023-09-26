package dandi.dandi.batchcommons.application.port.out;

import dandi.dandi.common.exception.NotFoundException;

public interface ChunkSizePersistencePort {

    int findChunkSizeByName(String name) throws NotFoundException;
}

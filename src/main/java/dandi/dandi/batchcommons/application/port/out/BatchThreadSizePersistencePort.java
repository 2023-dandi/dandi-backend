package dandi.dandi.batchcommons.application.port.out;

import dandi.dandi.common.exception.NotFoundException;

public interface BatchThreadSizePersistencePort {

    int findChunkSizeByName(String name);

    void updateChunkSizeByName(String name, int batchThreadSize) throws NotFoundException;
}

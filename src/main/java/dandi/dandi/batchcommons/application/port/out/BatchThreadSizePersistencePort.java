package dandi.dandi.batchcommons.application.port.out;

public interface BatchThreadSizePersistencePort {

    int findBatchThreadSizeByName(String name);

    void updateBatchThreadSizeByName(String name, int batchThreadSize);
}

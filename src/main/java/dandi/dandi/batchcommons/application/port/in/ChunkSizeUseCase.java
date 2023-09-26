package dandi.dandi.batchcommons.application.port.in;

public interface ChunkSizeUseCase {

    void updateChunkSizeByName(ChunkSizeUpdateCommand command);
}

package dandi.dandi.batchcommons.adapter.out.persistence.jpa.chunksize;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Profile({"local"})
public class ChunkSizeDBInitializer implements InitializingBean {

    private final ChunkSizeRepository chunkSizeRepository;

    public ChunkSizeDBInitializer(ChunkSizeRepository chunkSizeRepository) {
        this.chunkSizeRepository = chunkSizeRepository;
    }

    @Override
    public void afterPropertiesSet() {
        ChunkSizeJpaEntity weatherBatchChunkSize = new ChunkSizeJpaEntity("weatherBatch", 300);
        ChunkSizeJpaEntity unusedImageDeletionBatchChunkSize = new ChunkSizeJpaEntity("unusedImageDeletion", 100);
        chunkSizeRepository.saveAll(List.of(weatherBatchChunkSize, unusedImageDeletionBatchChunkSize));
    }
}

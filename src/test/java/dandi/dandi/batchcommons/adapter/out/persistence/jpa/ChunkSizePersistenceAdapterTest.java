package dandi.dandi.batchcommons.adapter.out.persistence.jpa;

import dandi.dandi.common.PersistenceAdapterTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

class ChunkSizePersistenceAdapterTest extends PersistenceAdapterTest {

    @Autowired
    private ChunkSizeRepository chunkSizeRepository;

    @Autowired
    private ChunkSizePersistenceAdapter chunkSizePersistenceAdapter;

    @DisplayName("이름에 따른 ChunkSize를 찾을 수 있다.")
    @Test
    void findChunkSizeByName() {
        String name = "job";
        int value = 30;
        chunkSizeRepository.save(new ChunkSizeJpaEntity(name, value));

        int actual = chunkSizePersistenceAdapter.findChunkSizeByName(name);

        assertThat(actual).isEqualTo(value);
    }
}

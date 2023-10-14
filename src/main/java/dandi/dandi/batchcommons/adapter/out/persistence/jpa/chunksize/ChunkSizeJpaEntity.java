package dandi.dandi.batchcommons.adapter.out.persistence.jpa.chunksize;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "chunk_size")
public class ChunkSizeJpaEntity {

    @Id
    private String name;
    private Integer value;

    public ChunkSizeJpaEntity(String name, Integer value) {
        this.name = name;
        this.value = value;
    }

    public ChunkSizeJpaEntity() {
    }

    public Integer getValue() {
        return value;
    }
}

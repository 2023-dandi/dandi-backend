package dandi.dandi.batchcommons.adapter.out.persistence.jpa.chunksize;

import javax.persistence.*;

@Entity
@Table(name = "chunk_size")
public class ChunkSizeJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chunk_size_id")
    private Long id;

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

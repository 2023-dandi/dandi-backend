package dandi.dandi.batchcommons.adapter.out.persistence.jpa.batchthreadsize;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "batch_thread_size")
public class BatchThreadSizeJpaEntity {

    @Id
    private String name;
    private Integer value;

    public BatchThreadSizeJpaEntity() {
    }

    public BatchThreadSizeJpaEntity(String name, Integer value) {
        this.name = name;
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }
}

package dandi.dandi.batchcommons.adapter.out.persistence.jpa.batchthreadsize;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile({"local"})
public class BatchThreadSizeDBInitializer implements InitializingBean {

    private final BatchThreadSizeRepository batchThreadSizeRepository;

    public BatchThreadSizeDBInitializer(BatchThreadSizeRepository batchThreadSizeRepository) {
        this.batchThreadSizeRepository = batchThreadSizeRepository;
    }

    @Override
    public void afterPropertiesSet() {
        BatchThreadSizeJpaEntity batchThreadSizeJpaEntity = new BatchThreadSizeJpaEntity("weatherBatch", 3);
        batchThreadSizeRepository.save(batchThreadSizeJpaEntity);
    }
}

package dandi.dandi.image.adapter.out.persistence.jpa;

import dandi.dandi.image.application.out.UnusedImagePersistencePort;
import org.springframework.stereotype.Component;

@Component
public class UnusedImagePersistenceAdapter implements UnusedImagePersistencePort {

    private final UnusedImageRepository unusedImageRepository;

    public UnusedImagePersistenceAdapter(UnusedImageRepository unusedImageRepository) {
        this.unusedImageRepository = unusedImageRepository;
    }

    @Override
    public void save(String imageUrl) {
        UnusedImageJpaEntity unusedImageJpaEntity = new UnusedImageJpaEntity(imageUrl);
        unusedImageRepository.save(unusedImageJpaEntity);
    }
}

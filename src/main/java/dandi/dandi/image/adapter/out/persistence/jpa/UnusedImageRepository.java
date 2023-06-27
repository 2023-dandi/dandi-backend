package dandi.dandi.image.adapter.out.persistence.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UnusedImageRepository extends JpaRepository<UnusedImageJpaEntity, Long> {

    void deleteByImageUrl(String imageUrl);
}

package dandi.dandi.clothes.application.port.out.persistence;

import dandi.dandi.clothes.domain.Clothes;
import java.util.Optional;

public interface ClothesPersistencePort {

    void save(Clothes clothes, Long memberId);

    Optional<Clothes> findById(Long id);
}

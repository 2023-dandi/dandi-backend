package dandi.dandi.post.adapter.out;

import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PostRepository extends JpaRepository<PostJpaEntity, Long> {

    @Query(value = "SELECT p FROM PostJpaEntity p "
            + "JOIN FETCH p.additionalFeelingIndicesJpaEntities WHERE p.id = :id")
    Optional<PostJpaEntity> findByIdWithAdditionalFeelingIndicesJpaEntities(Long id);

    boolean existsById(Long id);

    Slice<PostJpaEntity> findAllByMemberId(Long memberId, Pageable pageable);
}

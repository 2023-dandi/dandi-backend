package dandi.dandi.post.adapter.out;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PostRepository extends JpaRepository<PostJpaEntity, Long> {

    boolean existsById(Long id);

    Slice<PostJpaEntity> findAllByMemberId(Long memberId, Pageable pageable);

    @Query("SELECT p FROM PostJpaEntity p WHERE "
            + "p.minTemperature BETWEEN :minTemperatureMinSearchCondition AND :minTemperatureMaxSearchCondition AND "
            + "p.maxTemperature BETWEEN :maxTemperatureMinSearchCondition AND :maxTemperatureMaxSearchCondition AND "
            + "p.id NOT IN (SELECT pr.postId FROM PostReportJpaEntity pr WHERE pr.memberId = :memberId) AND "
            + "p.memberId NOT IN (SELECT mb.blockedMemberId FROM MemberBlockEntity mb where mb.blockingMemberId = :memberId) AND "
            + "p.memberId NOT IN (SELECT mb.blockingMemberId FROM MemberBlockEntity mb WHERE mb.blockedMemberId = :memberId )")
    Slice<PostJpaEntity> findByTemperature(Long memberId, Double minTemperatureMinSearchCondition,
                                           Double minTemperatureMaxSearchCondition,
                                           Double maxTemperatureMinSearchCondition,
                                           Double maxTemperatureMaxSearchCondition,
                                           Pageable pageable);

    @Query("SELECT p FROM PostJpaEntity p WHERE p.memberId = :memberId AND "
            + "p.minTemperature BETWEEN :minTemperatureMinSearchCondition AND :minTemperatureMaxSearchCondition "
            + "AND "
            + "p.maxTemperature BETWEEN :maxTemperatureMinSearchCondition AND :maxTemperatureMaxSearchCondition")
    Slice<PostJpaEntity> findByMemberIdAndTemperature(Long memberId,
                                                      Double minTemperatureMinSearchCondition,
                                                      Double minTemperatureMaxSearchCondition,
                                                      Double maxTemperatureMinSearchCondition,
                                                      Double maxTemperatureMaxSearchCondition,
                                                      Pageable pageable);

    @Query("SELECT  p from PostJpaEntity p "
            + "inner join PostLikeJpaEntity pl on p.id = pl.postId "
            + "where pl.memberId = :memberId")
    Slice<PostJpaEntity> findLikedPostsByMemberId(Long memberId, Pageable pageable);

    int countByMemberId(Long memberId);
}

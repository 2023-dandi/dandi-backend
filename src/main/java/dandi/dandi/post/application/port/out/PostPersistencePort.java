package dandi.dandi.post.application.port.out;

import dandi.dandi.post.domain.Post;
import dandi.dandi.post.domain.TemperatureSearchCondition;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface PostPersistencePort {

    Long save(Post post, Long memberId);

    Optional<Post> findById(Long postId);

    boolean existsById(Long postId);

    void deleteById(Long postId);

    Slice<Post> findByMemberId(Long memberId, Pageable pageable);

    Slice<Post> findByTemperature(TemperatureSearchCondition temperatureSearchCondition, Pageable pageable);

    Slice<Post> findByMemberIdAndTemperature(Long memberId, TemperatureSearchCondition temperatureSearchCondition,
                                             Pageable pageable);
}

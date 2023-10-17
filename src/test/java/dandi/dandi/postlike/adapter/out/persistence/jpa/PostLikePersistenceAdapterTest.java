package dandi.dandi.postlike.adapter.out.persistence.jpa;

import dandi.dandi.common.PersistenceAdapterTest;
import dandi.dandi.post.adapter.out.persistence.jpa.PostPersistenceAdapter;
import dandi.dandi.post.domain.Post;
import dandi.dandi.postlike.domain.PostLike;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import java.util.Optional;

import static dandi.dandi.member.MemberTestFixture.MEMBER_ID;
import static dandi.dandi.post.PostFixture.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

class PostLikePersistenceAdapterTest extends PersistenceAdapterTest {

    @Autowired
    private PostLikePersistenceAdapter postLikePersistenceAdapter;

    @Autowired
    private PostPersistenceAdapter postPersistenceAdapter;

    @Autowired
    private EntityManager entityManager;

    @DisplayName("게시글 좋아요를 저장할 수 있다.")
    @Test
    void save() {
        Post post = Post.initial(TEMPERATURES, POST_IMAGE_URL, WEATHER_FEELING);
        postPersistenceAdapter.save(post, MEMBER_ID);
        PostLike postLike = PostLike.initial(MEMBER_ID, POST_ID);

        assertThatCode(() -> postLikePersistenceAdapter.save(postLike))
                .doesNotThrowAnyException();
    }

    @DisplayName("회원과 게시글에 해당하는 게시글 좋아요를 찾을 수 있다.")
    @ParameterizedTest
    @CsvSource({"1, 1, true", "1, 2, false", "2, 1, false"})
    void findByMemberIdAndPostId(Long memberId, Long postId, boolean expectedPresentation) {
        Post post = Post.initial(TEMPERATURES, POST_IMAGE_URL, WEATHER_FEELING);
        postPersistenceAdapter.save(post, MEMBER_ID);
        PostLike postLike = PostLike.initial(MEMBER_ID, POST_ID);
        postLikePersistenceAdapter.save(postLike);
        entityManager.flush();

        Optional<PostLike> actual = postLikePersistenceAdapter.findByMemberIdAndPostId(memberId, postId);

        assertThat(actual.isPresent()).isEqualTo(expectedPresentation);
    }

    @DisplayName("id로 게시글 좋아요를 삭제할 수 있다.")
    @Test
    void deleteById() {
        Post post = Post.initial(TEMPERATURES, POST_IMAGE_URL, WEATHER_FEELING);
        postPersistenceAdapter.save(post, MEMBER_ID);
        PostLike postLike = PostLike.initial(MEMBER_ID, POST_ID);
        postLikePersistenceAdapter.save(postLike);
        PostLike savedPostLike = postLikePersistenceAdapter.findByMemberIdAndPostId(MEMBER_ID, POST_ID).get();
        entityManager.flush();

        postLikePersistenceAdapter.deleteByPostIdAndMemberId(savedPostLike.getPostId(), savedPostLike.getMemberId());

        Optional<PostLike> foundAfterDeletion = postLikePersistenceAdapter.findByMemberIdAndPostId(MEMBER_ID, POST_ID);
        assertThat(foundAfterDeletion).isEmpty();
    }

    @DisplayName("회원과 게시글에 해당하는 게시글 좋아요가 존재하는지 반환할 수 있다.")
    @ParameterizedTest
    @CsvSource({"1, 1, true", "1, 2, false", "2, 1, false"})
    void existsByPostIdAndMemberId(Long memberId, Long postId, boolean expected) {
        Post post = Post.initial(TEMPERATURES, POST_IMAGE_URL, WEATHER_FEELING);
        postPersistenceAdapter.save(post, MEMBER_ID);
        PostLike postLike = PostLike.initial(MEMBER_ID, POST_ID);
        postLikePersistenceAdapter.save(postLike);
        entityManager.flush();

        boolean actual = postLikePersistenceAdapter.existsByPostIdAndMemberId(memberId, postId);

        assertThat(actual).isEqualTo(expected);
    }
}

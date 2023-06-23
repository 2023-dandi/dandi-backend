package dandi.dandi.post.adapter.out.persistence.jpa;

import static dandi.dandi.member.MemberTestFixture.INITIAL_PROFILE_IMAGE_URL;
import static dandi.dandi.member.MemberTestFixture.MEMBER_ID;
import static dandi.dandi.member.MemberTestFixture.NICKNAME;
import static dandi.dandi.member.MemberTestFixture.OAUTH_ID;
import static dandi.dandi.post.PostFixture.POST_IMAGE_URL;
import static dandi.dandi.post.PostFixture.TEMPERATURES;
import static dandi.dandi.post.PostFixture.WEATHER_FEELING;
import static dandi.dandi.utils.PaginationUtils.CREATED_AT_DESC_TEST_SIZE_PAGEABLE;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import dandi.dandi.common.PersistenceAdapterTest;
import dandi.dandi.member.application.port.out.MemberBlockPersistencePort;
import dandi.dandi.member.application.port.out.MemberPersistencePort;
import dandi.dandi.member.domain.Member;
import dandi.dandi.post.domain.Post;
import dandi.dandi.post.domain.TemperatureSearchCondition;
import dandi.dandi.post.domain.Temperatures;
import dandi.dandi.post.domain.WeatherFeeling;
import dandi.dandi.postlike.adapter.out.persistence.jpa.PostLikePersistenceAdapter;
import dandi.dandi.postlike.domain.PostLike;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Slice;

class PostPersistenceAdapterTest extends PersistenceAdapterTest {

    @Autowired
    private PostPersistenceAdapter postPersistenceAdapter;

    @Autowired
    private MemberPersistencePort memberPersistencePort;

    @Autowired
    private MemberBlockPersistencePort memberBlockPersistencePort;

    @Autowired
    private PostLikePersistenceAdapter postLikePersistenceAdapter;

    @Autowired
    private PostReportPersistenceAdapter postReportPersistenceAdapter;

    @PersistenceContext
    private EntityManager entityManager;

    @DisplayName("게시글을 저장할 수 있다.")
    @Test
    void save() {
        Post post = Post.initial(TEMPERATURES, POST_IMAGE_URL, WEATHER_FEELING);

        assertThatCode(() -> postPersistenceAdapter.save(post, MEMBER_ID))
                .doesNotThrowAnyException();
    }

    @DisplayName("id로 게시글을 찾을 수 있다.")
    @Test
    void findById() {
        Long memberId = saveMember(NICKNAME);
        Post post = Post.initial(TEMPERATURES, POST_IMAGE_URL, WEATHER_FEELING);
        Long savedPostId = postPersistenceAdapter.save(post, memberId);

        Optional<Post> actual = postPersistenceAdapter.findById(savedPostId);

        assertThat(actual.get().getId()).isEqualTo(savedPostId);
    }

    @DisplayName("id로 게시글을 찾을 수 있다.")
    @Test
    void findById_A() {
        Long memberId = saveMember(NICKNAME);
        Post post = Post.initial(TEMPERATURES, POST_IMAGE_URL, new WeatherFeeling(1L, new ArrayList<>()));
        Long savedPostId = postPersistenceAdapter.save(post, memberId);

        Optional<Post> actual = postPersistenceAdapter.findById(savedPostId);

        assertThat(actual.get().getId()).isEqualTo(savedPostId);
    }

    @DisplayName("존재하는 id의 게시글인지 반환할 수 있다.")
    @ParameterizedTest
    @CsvSource({"1, true", "2, false"})
    void existsById(Long postId, boolean expected) {
        Post post = Post.initial(TEMPERATURES, POST_IMAGE_URL, WEATHER_FEELING);
        postPersistenceAdapter.save(post, MEMBER_ID);

        boolean actual = postPersistenceAdapter.existsById(postId);

        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("id로 게시글을 삭제할 수 있다.")
    @Test
    void deleteById() {
        Post post = Post.initial(TEMPERATURES, POST_IMAGE_URL, WEATHER_FEELING);
        Long savedPostId = postPersistenceAdapter.save(post, MEMBER_ID);

        postPersistenceAdapter.deleteById(savedPostId);

        Optional<Post> found = postPersistenceAdapter.findById(savedPostId);
        assertThat(found).isEmpty();
    }

    @DisplayName("회원이 작성한 게시글을 작성일 기준 내림차순으로 페이징 처리해서 반환할 수 있다.")
    @ParameterizedTest
    @CsvSource({"11, false", "10, true"})
    void findByMemberId(int postSaveCount, boolean expectedLastPage) {
        Long memberId = saveMember(NICKNAME);
        Post post = Post.initial(TEMPERATURES, POST_IMAGE_URL, WEATHER_FEELING);
        for (int i = 0; i < postSaveCount; i++) {
            postPersistenceAdapter.save(post, memberId);
        }

        Slice<Post> actual = postPersistenceAdapter.findByMemberId(memberId, CREATED_AT_DESC_TEST_SIZE_PAGEABLE);

        boolean allWrittenByMember = actual.stream()
                .allMatch(it -> it.isWrittenBy(memberId));
        List<Post> posts = actual.getContent();
        assertAll(
                () -> assertThat(actual.isLast()).isEqualTo(expectedLastPage),
                () -> assertThat(posts).hasSize(CREATED_AT_DESC_TEST_SIZE_PAGEABLE.getPageSize()),
                () -> assertThat(allWrittenByMember).isTrue(),
                () -> assertThat(isDescendingDirectionByCreatedAt(posts)).isTrue()
        );
    }

    @DisplayName("기온에 따라 자신이 차단한 사용자와 자신을 차단한 사용자를 제외한 모든 사용자의 게시글들과 신고하지 않은 게시글들을 작성일 기준 내림차순으로 페이징 처리해서 반환할 수 있다.(반환되는 게시글 존재)")
    @Test
    void findByTemperature_NotEmptyResult() {
        Post post = Post.initial(new Temperatures(10.0, 20.0), POST_IMAGE_URL, WEATHER_FEELING);
        Long firstMemberId = saveMember(NICKNAME);
        Long secondMemberId = saveMember("nickname123");
        Long thirdMemberId = saveMember("nickname456");
        Long fourthMemberId = saveMember("nickname789");
        Long savedFirstPostId = postPersistenceAdapter.save(post, firstMemberId);
        Long savedSecondPostId = postPersistenceAdapter.save(post, secondMemberId);
        postPersistenceAdapter.save(post, thirdMemberId);
        postPersistenceAdapter.save(post, fourthMemberId);
        postReportPersistenceAdapter.savePostReportOf(firstMemberId, savedSecondPostId);
        memberBlockPersistencePort.saveMemberBlockOf(thirdMemberId, firstMemberId);
        memberBlockPersistencePort.saveMemberBlockOf(firstMemberId, fourthMemberId);

        TemperatureSearchCondition temperatureSearchCondition =
                new TemperatureSearchCondition(9.0, 12.0, 19.0, 21.0);

        Slice<Post> actual = postPersistenceAdapter.findByTemperature(
                firstMemberId, temperatureSearchCondition, CREATED_AT_DESC_TEST_SIZE_PAGEABLE);

        assertAll(
                () -> assertThat(actual).hasSize(1),
                () -> assertThat(actual.getContent().get(0).getId()).isEqualTo(savedFirstPostId)
        );
    }

    @DisplayName("기온에 따른 모든 사용자의 게시글들을 작성일 기준 내림차순으로 페이징 처리해서 반환할 수 있다.(반환되는 게시글 존재 x)")
    @ParameterizedTest
    @CsvSource({"9.0, 12.0, 16.0, 19.0", "7.0, 9.0, 20.0, 22.0"})
    void findByTemperature_EmptyResult(double minTemperatureMinSearchCondition,
                                       double minTemperatureMaxSearchCondition,
                                       double maxTemperatureMinSearchCondition,
                                       double maxTemperatureMaxSearchCondition) {
        Long firstMemberId = saveMember(NICKNAME);
        Post firstPost = Post.initial(new Temperatures(10.0, 20.0), POST_IMAGE_URL, WEATHER_FEELING);
        Post secondPost = Post.initial(new Temperatures(11.0, 20.0), POST_IMAGE_URL, WEATHER_FEELING);
        Post thirdPost = Post.initial(new Temperatures(13.0, 22.0), POST_IMAGE_URL, WEATHER_FEELING);
        postPersistenceAdapter.save(firstPost, firstMemberId);
        postPersistenceAdapter.save(secondPost, firstMemberId);
        postPersistenceAdapter.save(thirdPost, firstMemberId);
        TemperatureSearchCondition temperatureSearchCondition = new TemperatureSearchCondition(
                minTemperatureMinSearchCondition, minTemperatureMaxSearchCondition,
                maxTemperatureMinSearchCondition, maxTemperatureMaxSearchCondition);

        Slice<Post> actual = postPersistenceAdapter.findByTemperature(
                firstMemberId, temperatureSearchCondition, CREATED_AT_DESC_TEST_SIZE_PAGEABLE);

        assertThat(actual).isEmpty();
    }

    @DisplayName("기온과 작성자에 따른 게시글들을 작성일 기준 내림차순으로 페이징 처리해서 반환할 수 있다.")
    @ParameterizedTest
    @CsvSource({"1, 2", "2, 1"})
    void findByMemberIdAndTemperature(Long memberId, int expectedPostSize) {
        Long firstMemberId = saveMember(NICKNAME);
        Long secondMemberId = saveMember("nickname123");
        Post firstPost = Post.initial(new Temperatures(10.0, 20.0), POST_IMAGE_URL, WEATHER_FEELING);
        Post secondPost = Post.initial(new Temperatures(11.0, 20.0), POST_IMAGE_URL, WEATHER_FEELING);
        Post thirdPost = Post.initial(new Temperatures(11.0, 20.0), POST_IMAGE_URL, WEATHER_FEELING);
        Post fourthPost = Post.initial(new Temperatures(13.0, 22.0), POST_IMAGE_URL, WEATHER_FEELING);
        postPersistenceAdapter.save(firstPost, firstMemberId);
        postPersistenceAdapter.save(secondPost, firstMemberId);
        postPersistenceAdapter.save(thirdPost, secondMemberId);
        postPersistenceAdapter.save(fourthPost, firstMemberId);
        TemperatureSearchCondition temperatureSearchCondition =
                new TemperatureSearchCondition(9.0, 12.0, 19.0, 21.0);

        Slice<Post> actual = postPersistenceAdapter.findByMemberIdAndTemperature(
                memberId, temperatureSearchCondition, CREATED_AT_DESC_TEST_SIZE_PAGEABLE);

        assertAll(
                () -> assertThat(actual).hasSize(expectedPostSize),
                () -> assertThat(isDescendingDirectionByCreatedAt(actual.getContent())).isTrue()
        );
    }

    @DisplayName("사용자가 좋아요 누른 게시글을 찾을 수 있다.")
    @Test
    void findLikedPosts() {
        Long firstMemberId = saveMember(NICKNAME);
        Long secondMemberId = saveMember("nickname2");
        Post firstPost = Post.initial(new Temperatures(10.0, 20.0), POST_IMAGE_URL, WEATHER_FEELING);
        Post secondPost = Post.initial(new Temperatures(11.0, 20.0), POST_IMAGE_URL, WEATHER_FEELING);
        Post thirdPost = Post.initial(new Temperatures(13.0, 22.0), POST_IMAGE_URL, WEATHER_FEELING);
        Long savedFirstPostId = postPersistenceAdapter.save(firstPost, firstMemberId);
        Long savedSecondPostId = postPersistenceAdapter.save(secondPost, firstMemberId);
        postPersistenceAdapter.save(thirdPost, firstMemberId);
        postLikePersistenceAdapter.save(PostLike.initial(secondMemberId, savedFirstPostId));
        postLikePersistenceAdapter.save(PostLike.initial(secondMemberId, savedSecondPostId));

        Slice<Post> likedPosts = postPersistenceAdapter.findLikedPosts(secondMemberId,
                CREATED_AT_DESC_TEST_SIZE_PAGEABLE);

        assertAll(
                () -> assertThat(likedPosts).hasSize(2),
                () -> assertThat(likedPosts.getContent().get(0).getId()).isEqualTo(2L),
                () -> assertThat(likedPosts.getContent().get(1).getId()).isEqualTo(1L)
        );
    }

    private boolean isDescendingDirectionByCreatedAt(List<Post> posts) {
        if (posts.size() < 2) {
            return true;
        }
        int lastPostIndex = posts.size() - 1;
        return posts.get(0).getId() > posts.get(lastPostIndex).getId();
    }

    private Long saveMember(String nickname) {
        return memberPersistencePort.save(Member.initial(OAUTH_ID, nickname, INITIAL_PROFILE_IMAGE_URL))
                .getId();
    }
}

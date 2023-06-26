package dandi.dandi.post.adapter.out.persistence.jpa;

import static dandi.dandi.member.MemberTestFixture.MEMBER_ID;
import static dandi.dandi.post.PostFixture.POST_IMAGE_URL;
import static dandi.dandi.post.PostFixture.TEMPERATURES;
import static dandi.dandi.post.PostFixture.WEATHER_FEELING;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import dandi.dandi.common.PersistenceAdapterTest;
import dandi.dandi.post.domain.Post;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class MemberPostPersistenceAdapterTest extends PersistenceAdapterTest {

    @Autowired
    private PostPersistenceAdapter postPersistenceAdapter;

    @Autowired
    private MemberPostPersistenceAdapter memberPostPersistenceAdapter;

    @DisplayName("회원이 작성한 게시글의 개수를 조회할 수 있다.")
    @Test
    void countPostByMemberId() {
        Post post = Post.initial(TEMPERATURES, POST_IMAGE_URL, WEATHER_FEELING);
        postPersistenceAdapter.save(post, MEMBER_ID);
        postPersistenceAdapter.save(post, MEMBER_ID);

        int actual = memberPostPersistenceAdapter.countPostByMemberId(MEMBER_ID);

        assertThat(actual).isEqualTo(2);
    }
}

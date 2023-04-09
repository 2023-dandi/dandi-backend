package dandi.dandi.postreport.adapter.persistence;

import static dandi.dandi.member.MemberTestFixture.MEMBER_ID;
import static dandi.dandi.post.PostFixture.POST_ID;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import dandi.dandi.common.PersistenceAdapterTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;

class PostReportPersistenceAdapterTest extends PersistenceAdapterTest {

    @Autowired
    private PostReportPersistenceAdapter postReportPersistenceAdapter;

    @DisplayName("사용자와 게시글에 따른 게시글 신고를 저장할 수 있다.")
    @Test
    void save() {
        assertThatCode(() -> postReportPersistenceAdapter.savePostReportOf(MEMBER_ID, POST_ID))
                .doesNotThrowAnyException();
    }

    @DisplayName("사용자와 게시글에 따른 게시글 신고 여부를 찾을 수 있다.")
    @ParameterizedTest
    @CsvSource({"1, 1, true", "1, 2, false", "2, 1, false"})
    void existsByMemberIdAndPostId(Long memberId, Long postId, boolean expected) {
        postReportPersistenceAdapter.savePostReportOf(MEMBER_ID, POST_ID);

        boolean actual = postReportPersistenceAdapter.existsByMemberIdAndPostId(memberId, postId);

        assertThat(actual).isEqualTo(expected);
    }
}

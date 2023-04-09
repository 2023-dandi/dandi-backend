package dandi.dandi.commentreport.adapter;

import static dandi.dandi.comment.CommentFixture.COMMENT_ID;
import static dandi.dandi.member.MemberTestFixture.MEMBER_ID;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import dandi.dandi.common.PersistenceAdapterTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;

class CommentReportPersistenceAdapterTest extends PersistenceAdapterTest {

    @Autowired
    private CommentReportPersistenceAdapter commentReportPersistenceAdapter;

    @DisplayName("사용자와 댓글에 해당하는 신고 이력을 저장할 수 있다.")
    @Test
    void saveReportOf() {
        assertThatCode(() -> commentReportPersistenceAdapter.saveReportOf(MEMBER_ID, COMMENT_ID))
                .doesNotThrowAnyException();
    }

    @DisplayName("사용자와 댓글에 해당 하는 신고 이력 여부를 찾을 수 있다.")
    @ParameterizedTest
    @CsvSource({"1, 1, true", "1, 2, false", "2, 1, false"})
    void existsByMemberIdAndCommentId(Long memberId, Long commentId, boolean expected) {
        commentReportPersistenceAdapter.saveReportOf(MEMBER_ID, COMMENT_ID);

        boolean actual = commentReportPersistenceAdapter.existsByMemberIdAndCommentId(memberId, commentId);

        assertThat(actual).isEqualTo(expected);
    }
}

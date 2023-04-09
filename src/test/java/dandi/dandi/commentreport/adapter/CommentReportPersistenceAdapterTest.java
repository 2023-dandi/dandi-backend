package dandi.dandi.commentreport.adapter;

import static dandi.dandi.comment.CommentFixture.COMMENT_ID;
import static dandi.dandi.member.MemberTestFixture.MEMBER_ID;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;

import dandi.dandi.common.PersistenceAdapterTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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
}

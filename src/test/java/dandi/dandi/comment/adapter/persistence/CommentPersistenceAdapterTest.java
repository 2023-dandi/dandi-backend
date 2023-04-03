package dandi.dandi.comment.adapter.persistence;

import static dandi.dandi.comment.CommentFixture.COMMENT_CONTENT;
import static dandi.dandi.member.MemberTestFixture.MEMBER_ID;
import static dandi.dandi.post.PostFixture.POST_ID;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;

import dandi.dandi.comment.domain.Comment;
import dandi.dandi.common.PersistenceAdapterTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class CommentPersistenceAdapterTest extends PersistenceAdapterTest {

    @Autowired
    private CommentPersistenceAdapter commentPersistenceAdapter;

    @DisplayName("댓글을 저장할 수 있다.")
    @Test
    void save() {
        Comment comment = Comment.initial(COMMENT_CONTENT);
        assertThatCode(() -> commentPersistenceAdapter.save(comment, POST_ID, MEMBER_ID))
                .doesNotThrowAnyException();
    }
}
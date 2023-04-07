package dandi.dandi.comment.adapter.persistence;

import static dandi.dandi.comment.CommentFixture.COMMENT_CONTENT;
import static dandi.dandi.member.MemberTestFixture.INITIAL_PROFILE_IMAGE_URL;
import static dandi.dandi.member.MemberTestFixture.MEMBER_ID;
import static dandi.dandi.member.MemberTestFixture.NICKNAME;
import static dandi.dandi.member.MemberTestFixture.OAUTH_ID;
import static dandi.dandi.post.PostFixture.POST_ID;
import static dandi.dandi.utils.PaginationUtils.CREATED_AT_DESC_TEST_SIZE_PAGEABLE;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import dandi.dandi.comment.domain.Comment;
import dandi.dandi.common.PersistenceAdapterTest;
import dandi.dandi.member.adapter.out.persistence.MemberPersistenceAdapter;
import dandi.dandi.member.domain.Member;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Slice;

class CommentPersistenceAdapterTest extends PersistenceAdapterTest {

    @Autowired
    private CommentPersistenceAdapter commentPersistenceAdapter;

    @Autowired
    private MemberPersistenceAdapter memberPersistenceAdapter;

    @DisplayName("댓글을 저장할 수 있다.")
    @Test
    void save() {
        Comment comment = Comment.initial(COMMENT_CONTENT);

        Long commentId = commentPersistenceAdapter.save(comment, POST_ID, MEMBER_ID);

        assertThat(commentId).isNotNull();
    }

    @DisplayName("게시글에 해당하는 댓글들을 찾을 수 있다.")
    @Test
    void findByPostId() {
        Long memberId = memberPersistenceAdapter.save(Member.initial(OAUTH_ID, NICKNAME, INITIAL_PROFILE_IMAGE_URL))
                .getId();
        Comment comment = Comment.initial(COMMENT_CONTENT);
        commentPersistenceAdapter.save(comment, POST_ID, memberId);
        commentPersistenceAdapter.save(comment, POST_ID, memberId);
        commentPersistenceAdapter.save(comment, 2L, memberId);

        Slice<Comment> comments = commentPersistenceAdapter.findByPostId(POST_ID, CREATED_AT_DESC_TEST_SIZE_PAGEABLE);

        assertThat(comments).hasSize(2);
    }

    @DisplayName("id에 해당하는 댓글을 찾을 수 있다.")
    @Test
    void findById() {
        Long memberId = memberPersistenceAdapter.save(Member.initial(OAUTH_ID, NICKNAME, INITIAL_PROFILE_IMAGE_URL))
                .getId();
        Comment comment = Comment.initial(COMMENT_CONTENT);
        Long commentId = commentPersistenceAdapter.save(comment, POST_ID, memberId);

        Optional<Comment> actual = commentPersistenceAdapter.findById(commentId);

        assertThat(actual.get().getId()).isEqualTo(commentId);
    }

    @DisplayName("id에 해당하는 댓글을 삭제할 수 있다.")
    @Test
    void deleteById() {
        Long memberId = memberPersistenceAdapter.save(Member.initial(OAUTH_ID, NICKNAME, INITIAL_PROFILE_IMAGE_URL))
                .getId();
        Comment comment = Comment.initial(COMMENT_CONTENT);
        Long commentId = commentPersistenceAdapter.save(comment, POST_ID, memberId);
        Optional<Comment> foundBeforeDeletion = commentPersistenceAdapter.findById(commentId);

        commentPersistenceAdapter.deleteById(commentId);

        Optional<Comment> foundAfterDeletion = commentPersistenceAdapter.findById(commentId);
        assertAll(
                () -> assertThat(foundBeforeDeletion).isPresent(),
                () -> assertThat(foundAfterDeletion).isEmpty()
        );
    }
}

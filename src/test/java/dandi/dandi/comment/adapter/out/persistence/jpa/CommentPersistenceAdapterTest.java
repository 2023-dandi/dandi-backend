package dandi.dandi.comment.adapter.out.persistence.jpa;

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
import dandi.dandi.member.adapter.out.persistence.jpa.MemberBlockPersistenceAdapter;
import dandi.dandi.member.adapter.out.persistence.jpa.MemberPersistenceAdapter;
import dandi.dandi.member.domain.Member;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Slice;

class CommentPersistenceAdapterTest extends PersistenceAdapterTest {

    @Autowired
    private CommentPersistenceAdapter commentPersistenceAdapter;

    @Autowired
    private MemberPersistenceAdapter memberPersistenceAdapter;

    @Autowired
    private MemberBlockPersistenceAdapter memberBlockPersistenceAdapter;

    @Autowired
    private CommentReportPersistenceAdapter commentReportPersistenceAdapter;

    @DisplayName("댓글을 저장할 수 있다.")
    @Test
    void save() {
        Comment comment = Comment.initial(COMMENT_CONTENT);

        Long commentId = commentPersistenceAdapter.save(comment, POST_ID, MEMBER_ID);

        assertThat(commentId).isNotNull();
    }

    @DisplayName("게시글에 해당하고 신고하지 않고 차단하거나 차단 당한 사용자가 작성하지 않은 댓글들을 찾을 수 있다.")
    @Test
    void findByPostId() {
        Long firstMemberId = memberPersistenceAdapter.save(Member.initial(
                OAUTH_ID, NICKNAME, INITIAL_PROFILE_IMAGE_URL)).getId();
        Long secondMemberId = memberPersistenceAdapter.save(Member.initial(
                OAUTH_ID, "nickname2", INITIAL_PROFILE_IMAGE_URL)).getId();
        Long thirdMemberId = memberPersistenceAdapter.save(Member.initial(
                OAUTH_ID, "nickname3", INITIAL_PROFILE_IMAGE_URL)).getId();
        Long fourthMemberId = memberPersistenceAdapter.save(Member.initial(
                OAUTH_ID, "nickname4", INITIAL_PROFILE_IMAGE_URL)).getId();
        Comment comment = Comment.initial(COMMENT_CONTENT);
        Long firstSavedCommentId = commentPersistenceAdapter.save(comment, POST_ID, firstMemberId);
        commentPersistenceAdapter.save(comment, POST_ID, secondMemberId);
        Long thirdSavedCommentId = commentPersistenceAdapter.save(comment, POST_ID, thirdMemberId);
        commentPersistenceAdapter.save(comment, POST_ID, fourthMemberId);
        commentPersistenceAdapter.save(comment, 2L, firstMemberId);
        memberBlockPersistenceAdapter.saveMemberBlockOf(firstMemberId, secondMemberId);
        memberBlockPersistenceAdapter.saveMemberBlockOf(fourthMemberId, firstMemberId);
        commentReportPersistenceAdapter.saveReportOf(firstMemberId, thirdSavedCommentId);

        Slice<Comment> comments = commentPersistenceAdapter.findByPostId(
                firstMemberId, POST_ID, CREATED_AT_DESC_TEST_SIZE_PAGEABLE);

        assertAll(
                () -> assertThat(comments).hasSize(1),
                () -> assertThat(comments.getContent().get(0).getId()).isEqualTo(firstSavedCommentId)
        );
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

    @DisplayName("id에 해당하는 댓글이 존재하는지 찾을 수 있다.")
    @ParameterizedTest
    @CsvSource({"1, true", "2, false"})
    void existsById(Long id, boolean expected) {
        Long memberId = memberPersistenceAdapter.save(Member.initial(OAUTH_ID, NICKNAME, INITIAL_PROFILE_IMAGE_URL))
                .getId();
        Comment comment = Comment.initial(COMMENT_CONTENT);
        commentPersistenceAdapter.save(comment, POST_ID, memberId);

        boolean actual = commentPersistenceAdapter.existsById(id);

        assertThat(actual).isEqualTo(expected);
    }
}

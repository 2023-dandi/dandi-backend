package dandi.dandi.comment.adapter.persistence;

import static dandi.dandi.comment.CommentFixture.COMMENT_CONTENT;
import static dandi.dandi.member.MemberTestFixture.INITIAL_PROFILE_IMAGE_URL;
import static dandi.dandi.member.MemberTestFixture.MEMBER_ID;
import static dandi.dandi.member.MemberTestFixture.NICKNAME;
import static dandi.dandi.member.MemberTestFixture.OAUTH_ID;
import static dandi.dandi.post.PostFixture.POST_ID;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.springframework.data.domain.Sort.Direction.DESC;

import dandi.dandi.comment.domain.Comment;
import dandi.dandi.common.PersistenceAdapterTest;
import dandi.dandi.member.adapter.out.persistence.MemberPersistenceAdapter;
import dandi.dandi.member.domain.Member;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
        assertThatCode(() -> commentPersistenceAdapter.save(comment, POST_ID, MEMBER_ID))
                .doesNotThrowAnyException();
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
        Pageable pageable = PageRequest.of(0, 10, DESC, "createdAt");

        Slice<Comment> comments = commentPersistenceAdapter.findByPostId(POST_ID, pageable);

        assertThat(comments).hasSize(2);
    }

    @DisplayName("id에 해당하는 댓글을 찾을 수 있다.")
    @Test
    void findById() {
        Long memberId = memberPersistenceAdapter.save(Member.initial(OAUTH_ID, NICKNAME, INITIAL_PROFILE_IMAGE_URL))
                .getId();
        Comment comment = Comment.initial(COMMENT_CONTENT);
        commentPersistenceAdapter.save(comment, POST_ID, memberId);
        Long commentId = 1L;

        Optional<Comment> actual = commentPersistenceAdapter.findById(commentId);

        assertThat(actual.get().getId()).isEqualTo(commentId);
    }
}

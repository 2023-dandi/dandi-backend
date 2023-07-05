package dandi.dandi.comment.application.service;

import static dandi.dandi.member.MemberTestFixture.MEMBER;
import static dandi.dandi.member.MemberTestFixture.MEMBER2;
import static dandi.dandi.member.MemberTestFixture.MEMBER_ID;
import static dandi.dandi.post.PostFixture.POST;
import static dandi.dandi.post.PostFixture.POST_ID;
import static dandi.dandi.utils.PaginationUtils.CREATED_AT_DESC_TEST_SIZE_PAGEABLE;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;

import dandi.dandi.comment.application.port.in.CommentResponses;
import dandi.dandi.comment.application.port.out.CommentPersistencePort;
import dandi.dandi.comment.domain.Comment;
import dandi.dandi.common.exception.NotFoundException;
import dandi.dandi.post.application.port.out.PostPersistencePort;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.SliceImpl;

@ExtendWith(MockitoExtension.class)
class CommentQueryServiceAdapterTest {

    private final PostPersistencePort postPersistencePort = Mockito.mock(PostPersistencePort.class);
    private final CommentPersistencePort commentPersistencePort = Mockito.mock(CommentPersistencePort.class);
    private final CommentQueryServiceAdapter commentQueryServiceAdapter =
            new CommentQueryServiceAdapter(postPersistencePort, commentPersistencePort);

    @DisplayName("게시글의 댓글들을 조회할 수 있다.")
    @Test
    void getComments() {
        when(postPersistencePort.findById(POST_ID))
                .thenReturn(Optional.of(POST));
        List<Comment> comments = List.of(
                new Comment(2L, "댓글 내용2", MEMBER, LocalDate.now()),
                new Comment(1L, "댓글 내용1", MEMBER2, LocalDate.now()));
        when(commentPersistencePort.findByPostId(MEMBER_ID, POST_ID, CREATED_AT_DESC_TEST_SIZE_PAGEABLE))
                .thenReturn(new SliceImpl<>(comments, CREATED_AT_DESC_TEST_SIZE_PAGEABLE, false));

        CommentResponses commentResponses = commentQueryServiceAdapter.getComments(MEMBER_ID, POST_ID,
                CREATED_AT_DESC_TEST_SIZE_PAGEABLE);

        assertAll(
                () -> assertThat(commentResponses.getComments().get(0).isPostWriter()).isTrue(),
                () -> assertThat(commentResponses.getComments().get(0).isMine()).isTrue(),
                () -> assertThat(commentResponses.getComments().get(1).isPostWriter()).isFalse(),
                () -> assertThat(commentResponses.getComments().get(1).isMine()).isFalse()
        );
    }

    @DisplayName("존재하지 않는 게시글의 댓글들을 조회하려하면 예외를 발생시킨다.")
    @Test
    void getComments_NotFoundPost() {
        when(postPersistencePort.findById(POST_ID))
                .thenReturn(Optional.empty());

        assertThatThrownBy(
                () -> commentQueryServiceAdapter.getComments(MEMBER_ID, POST_ID, CREATED_AT_DESC_TEST_SIZE_PAGEABLE))
                .isInstanceOf(NotFoundException.class)
                .hasMessage(NotFoundException.post().getMessage());
    }
}

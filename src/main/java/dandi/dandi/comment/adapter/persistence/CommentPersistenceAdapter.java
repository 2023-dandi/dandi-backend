package dandi.dandi.comment.adapter.persistence;

import dandi.dandi.advice.InternalServerException;
import dandi.dandi.comment.application.port.out.CommentPersistencePort;
import dandi.dandi.comment.domain.Comment;
import dandi.dandi.member.adapter.out.persistence.MemberRepository;
import dandi.dandi.member.domain.Member;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Component;

@Component
public class CommentPersistenceAdapter implements CommentPersistencePort {

    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;

    public CommentPersistenceAdapter(CommentRepository commentRepository, MemberRepository memberRepository) {
        this.commentRepository = commentRepository;
        this.memberRepository = memberRepository;
    }

    @Override
    public void save(Comment comment, Long postId, Long memberId) {
        CommentJpaEntity commentJpaEntity = CommentJpaEntity.of(comment, postId, memberId);
        commentRepository.save(commentJpaEntity);
    }

    @Override
    public Slice<Comment> findByPostId(Long postId, Pageable pageable) {
        Slice<CommentJpaEntity> commentJpaEntities = commentRepository.findByPostId(postId, pageable);
        List<Comment> comments = commentJpaEntities.stream()
                .map(commentJpaEntity -> commentJpaEntity.toComment(findMember(commentJpaEntity.getMemberId())))
                .collect(Collectors.toUnmodifiableList());
        return new SliceImpl<>(comments, pageable, commentJpaEntities.hasNext());
    }

    @Override
    public Optional<Comment> findById(Long id) {
        return commentRepository.findById(id)
                .map(commentJpaEntity -> commentJpaEntity.toComment(findMember(commentJpaEntity.getMemberId())));
    }

    private Member findMember(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> InternalServerException.withdrawnMemberPost(memberId))
                .toMember();
    }
}

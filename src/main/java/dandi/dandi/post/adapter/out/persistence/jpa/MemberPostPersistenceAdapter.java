package dandi.dandi.post.adapter.out.persistence.jpa;

import dandi.dandi.member.application.port.out.MemberPostPersistencePort;
import org.springframework.stereotype.Component;

@Component
public class MemberPostPersistenceAdapter implements MemberPostPersistencePort {

    private final PostRepository postRepository;

    public MemberPostPersistenceAdapter(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Override
    public int countPostByMemberId(Long memberId) {
        return postRepository.countByMemberId(memberId);
    }
}

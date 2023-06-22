package dandi.dandi.member.adapter.out.persistence.jpa;

import dandi.dandi.post.adapter.out.PostRepository;
import dandi.dandi.post.application.port.out.MemberPostPersistencePort;
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

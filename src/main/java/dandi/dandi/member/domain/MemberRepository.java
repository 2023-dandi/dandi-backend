package dandi.dandi.member.domain;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MemberRepository extends JpaRepository<Member, Long> {

    @Query("SELECT m FROM Member m WHERE m.oAuthId = :oAuthId")
    Optional<Member> findByOAuthId(String oAuthId);
}

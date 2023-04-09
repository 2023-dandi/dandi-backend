package dandi.dandi.member.adapter.out.persistence;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;

import dandi.dandi.common.PersistenceAdapterTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class MemberBlockPersistenceAdapterTest extends PersistenceAdapterTest {

    @Autowired
    private MemberBlockPersistenceAdapter memberBlockPersistenceAdapter;

    @DisplayName("회원을 차단할 수 있다.")
    @Test
    void saveMemberBlockOf() {
        Long blockingMemberId = 1L;
        Long blockedMemberId = 2L;

        assertThatCode(() -> memberBlockPersistenceAdapter.saveMemberBlockOf(blockingMemberId, blockedMemberId))
                .doesNotThrowAnyException();
    }
}

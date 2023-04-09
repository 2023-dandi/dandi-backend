package dandi.dandi.member.adapter.out.persistence;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;

import dandi.dandi.common.PersistenceAdapterTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
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

    @DisplayName("차단한 사용자의 id와 차단 당한 사용자의 id에 따른 차단 내역이 존재하는지 찾을 수 있다.")
    @ParameterizedTest
    @CsvSource({"1, 2, true", "1, 3, false", "3, 2, false"})
    void existsByBlockingMemberIdAndBlockedMemberId(Long blockingMemberId, Long blockedMemberId, boolean expected) {
        memberBlockPersistenceAdapter.saveMemberBlockOf(1L, 2L);

        boolean actual = memberBlockPersistenceAdapter
                .existsByBlockingMemberIdAndBlockedMemberId(blockingMemberId, blockedMemberId);

        assertThat(actual).isEqualTo(expected);
    }
}

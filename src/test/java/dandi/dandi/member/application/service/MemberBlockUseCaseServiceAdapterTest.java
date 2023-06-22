package dandi.dandi.member.application.service;

import static dandi.dandi.member.MemberTestFixture.MEMBER_ID;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import dandi.dandi.common.exception.NotFoundException;
import dandi.dandi.member.application.port.in.MemberBlockCommand;
import dandi.dandi.member.application.port.out.MemberBlockPersistencePort;
import dandi.dandi.member.application.port.out.MemberPersistencePort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MemberBlockUseCaseServiceAdapterTest {

    @Mock
    private MemberBlockPersistencePort memberBlockPersistencePort;
    @Mock
    private MemberPersistencePort memberPersistencePort;
    @InjectMocks
    private MemberBlockUseCaseServiceAdapter memberBlockUseCaseServiceAdapter;

    @DisplayName("존재하지 않는 id의 사용자를 차단하려고 하면 예외를 발생시킨다.")
    @Test
    void blockMember_NotFound() {
        Long blockedMemberId = 2L;
        when(memberPersistencePort.existsById(blockedMemberId))
                .thenReturn(false);

        assertThatThrownBy(
                () -> memberBlockUseCaseServiceAdapter.blockMember(MEMBER_ID, new MemberBlockCommand(blockedMemberId)))
                .isInstanceOf(NotFoundException.class)
                .hasMessage(NotFoundException.member().getMessage());
    }

    @DisplayName("이미 차단한 사용자를 차단하려고 하면 예외를 발생시킨다.")
    @Test
    void blockMember_AlreadyBlocked() {
        Long blockedMemberId = 2L;
        when(memberPersistencePort.existsById(blockedMemberId))
                .thenReturn(true);
        when(memberBlockPersistencePort.existsByBlockingMemberIdAndBlockedMemberId(MEMBER_ID, blockedMemberId))
                .thenReturn(true);

        assertThatThrownBy(
                () -> memberBlockUseCaseServiceAdapter.blockMember(MEMBER_ID, new MemberBlockCommand(blockedMemberId)))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("이미 차단한 사용자입니다.");
    }

    @DisplayName("다른 사용자를 차단할 수 있다.")
    @Test
    void blockMember() {
        Long blockedMemberId = 2L;
        when(memberPersistencePort.existsById(blockedMemberId))
                .thenReturn(true);
        when(memberBlockPersistencePort.existsByBlockingMemberIdAndBlockedMemberId(MEMBER_ID, blockedMemberId))
                .thenReturn(false);

        memberBlockUseCaseServiceAdapter.blockMember(MEMBER_ID, new MemberBlockCommand(blockedMemberId));

        verify(memberBlockPersistencePort).saveMemberBlockOf(MEMBER_ID, blockedMemberId);
    }
}

package dandi.dandi.member.application;

import static dandi.dandi.member.MemberTestFixture.INITIAL_PROFILE_IMAGE_URL;
import static dandi.dandi.member.MemberTestFixture.MEMBER;
import static dandi.dandi.member.MemberTestFixture.MEMBER_ID;
import static dandi.dandi.member.MemberTestFixture.NICKNAME;
import static dandi.dandi.member.MemberTestFixture.OAUTH_ID;
import static dandi.dandi.utils.TestImageUtils.IMAGE_ACCESS_URL;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import dandi.dandi.auth.exception.UnauthorizedException;
import dandi.dandi.common.exception.NotFoundException;
import dandi.dandi.member.application.port.in.LocationUpdateCommand;
import dandi.dandi.member.application.port.in.MemberBlockCommand;
import dandi.dandi.member.application.port.in.MemberInfoResponse;
import dandi.dandi.member.application.port.in.NicknameUpdateCommand;
import dandi.dandi.member.application.port.out.MemberPersistencePort;
import dandi.dandi.member.application.service.MemberService;
import dandi.dandi.member.domain.Member;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    private final MemberPersistencePort memberPersistencePort = Mockito.mock(MemberPersistencePort.class);
    private final MemberService memberService =
            new MemberService(memberPersistencePort, IMAGE_ACCESS_URL);

    @DisplayName("기본 프로필 이미지의 회원 정보를 반환할 수 있다.")
    @Test
    void findMemberInfo() {
        Long memberId = 1L;
        when(memberPersistencePort.findById(memberId))
                .thenReturn(Optional.of(Member.initial(OAUTH_ID, NICKNAME, INITIAL_PROFILE_IMAGE_URL)));

        MemberInfoResponse memberInfoResponse = memberService.findMemberInfo(memberId);

        assertAll(
                () -> assertThat(memberInfoResponse.getNickname()).isEqualTo(NICKNAME),
                () -> assertThat(memberInfoResponse.getLatitude()).isEqualTo(0.0),
                () -> assertThat(memberInfoResponse.getLongitude()).isEqualTo(0.0),
                () -> assertThat(memberInfoResponse.getProfileImageUrl())
                        .startsWith(IMAGE_ACCESS_URL + INITIAL_PROFILE_IMAGE_URL)
        );
    }

    @DisplayName("존재하지 않는 회원의 정보를 반환하려하면 예외를 발생시킨다.")
    @Test
    void findMemberInfo_NonExistentMember() {
        Long nonExistentMemberId = 1L;
        when(memberPersistencePort.findById(nonExistentMemberId))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> memberService.findMemberInfo(nonExistentMemberId))
                .isInstanceOf(UnauthorizedException.class)
                .hasMessage(UnauthorizedException.notExistentMember().getMessage());
    }

    @DisplayName("회원의 닉네임을 변경할 수 있다.")
    @Test
    void updateNickname() {
        String newNickname = "newNickname";
        NicknameUpdateCommand nicknameUpdateCommand = new NicknameUpdateCommand(newNickname);
        when(memberPersistencePort.findById(MEMBER.getId()))
                .thenReturn(Optional.of(MEMBER));

        memberService.updateNickname(MEMBER.getId(), nicknameUpdateCommand);

        verify(memberPersistencePort).updateNickname(MEMBER.getId(), newNickname);
    }

    @DisplayName("이미 존재하는 닉네임으로 변경하려하면 예외를 발생시킨다.")
    @Test
    void updateNickname_DuplicationNicknameException() {
        String newNickname = "newNickname";
        NicknameUpdateCommand nicknameUpdateCommand = new NicknameUpdateCommand(newNickname);
        when(memberPersistencePort.findById(MEMBER.getId()))
                .thenReturn(Optional.of(MEMBER));
        doThrow(new DataIntegrityViolationException("unique constraint violation"))
                .when(memberPersistencePort).updateNickname(MEMBER.getId(), newNickname);

        assertThatThrownBy(() -> memberService.updateNickname(MEMBER.getId(), nicknameUpdateCommand))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이미 존재하는 닉네임입니다.");
    }

    @DisplayName("회원의 위치 정보를 변경할 수 있다.")
    @Test
    void updateLocation() {
        double latitude = 1.0;
        double longitude = 1.0;
        LocationUpdateCommand locationUpdateCommand = new LocationUpdateCommand(latitude, longitude);
        when(memberPersistencePort.findById(MEMBER.getId()))
                .thenReturn(Optional.of(MEMBER));

        memberService.updateLocation(MEMBER.getId(), locationUpdateCommand);

        verify(memberPersistencePort).updateLocation(MEMBER.getId(), latitude, latitude);
    }

    @DisplayName("존재하지 않는 id의 사용자를 차단하려고 하면 예외를 발생시킨다.")
    @Test
    void blockMember_NotFound() {
        Long blockedMemberId = 2L;
        when(memberPersistencePort.existsById(blockedMemberId))
                .thenReturn(false);

        assertThatThrownBy(() -> memberService.blockMember(MEMBER_ID, new MemberBlockCommand(blockedMemberId)))
                .isInstanceOf(NotFoundException.class)
                .hasMessage(NotFoundException.member().getMessage());
    }
}

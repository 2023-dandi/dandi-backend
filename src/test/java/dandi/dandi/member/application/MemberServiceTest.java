package dandi.dandi.member.application;

import static dandi.dandi.member.MemberTestFixture.DISTRICT;
import static dandi.dandi.member.MemberTestFixture.DISTRICT_VALUE;
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
import dandi.dandi.member.application.port.out.MemberBlockPersistencePort;
import dandi.dandi.member.application.port.out.MemberPersistencePort;
import dandi.dandi.member.application.service.MemberService;
import dandi.dandi.member.domain.DistrictParser;
import dandi.dandi.member.domain.Location;
import dandi.dandi.member.domain.Member;
import dandi.dandi.post.application.port.out.MemberPostPersistencePort;
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
    private final MemberBlockPersistencePort memberBlockPersistencePort =
            Mockito.mock(MemberBlockPersistencePort.class);
    private final MemberPostPersistencePort memberPostPersistencePort = Mockito.mock(MemberPostPersistencePort.class);
    private final DistrictParser districtParser = new DistrictParser();
    private final MemberService memberService = new MemberService(memberPersistencePort, memberBlockPersistencePort,
            memberPostPersistencePort, districtParser, IMAGE_ACCESS_URL);

    @DisplayName("기본 프로필 이미지의 회원 정보를 반환할 수 있다.")
    @Test
    void findMemberInfo() {
        Long memberId = 1L;
        when(memberPersistencePort.findById(memberId))
                .thenReturn(Optional.of(Member.initial(OAUTH_ID, NICKNAME, INITIAL_PROFILE_IMAGE_URL)));
        when(memberPostPersistencePort.countPostByMemberId(memberId))
                .thenReturn(5);

        MemberInfoResponse memberInfoResponse = memberService.findMemberInfo(memberId);

        assertAll(
                () -> assertThat(memberInfoResponse.getNickname()).isEqualTo(NICKNAME),
                () -> assertThat(memberInfoResponse.getPostCount()).isEqualTo(5),
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
        LocationUpdateCommand locationUpdateCommand = new LocationUpdateCommand(latitude, longitude, DISTRICT_VALUE);
        when(memberPersistencePort.findById(MEMBER.getId()))
                .thenReturn(Optional.of(MEMBER));

        memberService.updateLocation(MEMBER.getId(), locationUpdateCommand);

        verify(memberPersistencePort).updateLocation(MEMBER.getId(), new Location(latitude, longitude, DISTRICT));
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

    @DisplayName("이미 차단한 사용자를 차단하려고 하면 예외를 발생시킨다.")
    @Test
    void blockMember_AlreadyBlocked() {
        Long blockedMemberId = 2L;
        when(memberPersistencePort.existsById(blockedMemberId))
                .thenReturn(true);
        when(memberBlockPersistencePort.existsByBlockingMemberIdAndBlockedMemberId(MEMBER_ID, blockedMemberId))
                .thenReturn(true);

        assertThatThrownBy(() -> memberService.blockMember(MEMBER_ID, new MemberBlockCommand(blockedMemberId)))
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

        memberService.blockMember(MEMBER_ID, new MemberBlockCommand(blockedMemberId));

        verify(memberBlockPersistencePort).saveMemberBlockOf(MEMBER_ID, blockedMemberId);
    }
}

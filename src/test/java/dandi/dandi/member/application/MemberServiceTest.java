package dandi.dandi.member.application;

import static dandi.dandi.member.MemberTestFixture.INITIAL_PROFILE_IMAGE_URL;
import static dandi.dandi.member.MemberTestFixture.NICKNAME;
import static dandi.dandi.member.MemberTestFixture.OAUTH_ID;
import static dandi.dandi.member.MemberTestFixture.TEST_MEMBER;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import dandi.dandi.auth.exception.UnauthorizedException;
import dandi.dandi.member.application.port.in.dto.LocationUpdateRequest;
import dandi.dandi.member.application.port.in.dto.NicknameUpdateRequest;
import dandi.dandi.member.application.port.out.MemberPersistencePort;
import dandi.dandi.member.application.port.out.dto.MemberInfoResponse;
import dandi.dandi.member.application.service.MemberService;
import dandi.dandi.member.domain.Member;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @Mock
    private MemberPersistencePort memberPersistencePort;

    @InjectMocks
    private MemberService memberService;

    @DisplayName("회원의 정보를 반환할 수 있다.")
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
                () -> assertThat(memberInfoResponse.getProfileImageUrl()).isEqualTo(INITIAL_PROFILE_IMAGE_URL)
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
        NicknameUpdateRequest nicknameUpdateRequest = new NicknameUpdateRequest(newNickname);
        when(memberPersistencePort.findById(TEST_MEMBER.getId()))
                .thenReturn(Optional.of(TEST_MEMBER));

        memberService.updateNickname(TEST_MEMBER.getId(), nicknameUpdateRequest);

        verify(memberPersistencePort).updateNickname(TEST_MEMBER.getId(), newNickname);
    }

    @DisplayName("회원의 위치 정보를 변경할 수 있다.")
    @Test
    void updateLocation() {
        double latitude = 1.0;
        double longitude = 1.0;
        LocationUpdateRequest locationUpdateRequest = new LocationUpdateRequest(latitude, longitude);
        when(memberPersistencePort.findById(TEST_MEMBER.getId()))
                .thenReturn(Optional.of(TEST_MEMBER));

        memberService.updateLocation(TEST_MEMBER.getId(), locationUpdateRequest);

        verify(memberPersistencePort).updateLocation(TEST_MEMBER.getId(), latitude, latitude);
    }
}

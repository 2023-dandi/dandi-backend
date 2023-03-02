package dandi.dandi.member.application;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import dandi.dandi.auth.exception.UnauthorizedException;
import dandi.dandi.member.application.dto.LocationUpdateRequest;
import dandi.dandi.member.application.dto.MemberInfoResponse;
import dandi.dandi.member.application.dto.NicknameUpdateRequest;
import dandi.dandi.member.domain.Member;
import dandi.dandi.member.domain.MemberRepository;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private MemberService memberService;

    @DisplayName("회원의 정보를 반환할 수 있다.")
    @Test
    void findMemberInfo() {
        Long memberId = 1L;
        String nickname = "nickname";
        when(memberRepository.findById(memberId))
                .thenReturn(Optional.of(Member.initial("oAuthId", nickname, "profileImageUrl")));

        MemberInfoResponse memberInfoResponse = memberService.findMemberInfo(memberId);

        assertAll(
                () -> assertThat(memberInfoResponse.getNickname()).isEqualTo(nickname),
                () -> assertThat(memberInfoResponse.getLatitude()).isEqualTo(0.0),
                () -> assertThat(memberInfoResponse.getLongitude()).isEqualTo(0.0)
        );
    }

    @DisplayName("존재하지 않는 회원의 정보를 반환하려하면 예외를 발생시킨다.")
    @Test
    void findMemberInfo_NonExistentMember() {
        Long nonExistentMemberId = 1L;
        when(memberRepository.findById(nonExistentMemberId))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> memberService.findMemberInfo(nonExistentMemberId))
                .isInstanceOf(UnauthorizedException.class)
                .hasMessage("존재하지 않는 사용자의 토큰입니다.");
    }

    @DisplayName("회원의 닉네임을 변경할 수 있다.")
    @Test
    void updateNickname() {
        Long memberId = 1L;
        Member member = Mockito.mock(Member.class);
        String newNickname = "newNickname";
        NicknameUpdateRequest nicknameUpdateRequest = new NicknameUpdateRequest(newNickname);
        when(memberRepository.findById(memberId))
                .thenReturn(Optional.of(member));

        memberService.updateNickname(memberId, nicknameUpdateRequest);

        verify(member, only()).updateNickname(newNickname);
    }

    @DisplayName("회원의 위치 정보를 변경할 수 있다.")
    @Test
    void updateLocation() {
        Long memberId = 1L;
        Member member = Mockito.mock(Member.class);
        double latitude = 1.0;
        double longitude = 1.0;
        LocationUpdateRequest locationUpdateRequest = new LocationUpdateRequest(latitude, longitude);
        when(memberRepository.findById(memberId))
                .thenReturn(Optional.of(member));

        memberService.updateLocation(memberId, locationUpdateRequest);

        verify(member, only()).updateLocation(latitude, latitude);
    }
}

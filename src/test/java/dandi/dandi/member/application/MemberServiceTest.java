package dandi.dandi.member.application;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;

import dandi.dandi.auth.exception.UnauthorizedException;
import dandi.dandi.member.application.dto.MemberInfoResponse;
import dandi.dandi.member.domain.Member;
import dandi.dandi.member.domain.MemberRepository;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class MemberServiceTest {

    private final MemberRepository memberRepository = Mockito.mock(MemberRepository.class);
    private final MemberService memberService = new MemberService(memberRepository);

    @DisplayName("회원의 정보를 반환할 수 있다.")
    @Test
    void findMemberInfo() {
        Long memberId = 1L;
        String nickname = "nickname";
        when(memberRepository.findById(memberId))
                .thenReturn(Optional.of(Member.initial("oAuthId", nickname)));

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
}

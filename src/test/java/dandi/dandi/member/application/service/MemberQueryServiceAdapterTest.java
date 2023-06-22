package dandi.dandi.member.application.service;

import static dandi.dandi.member.MemberTestFixture.INITIAL_PROFILE_IMAGE_URL;
import static dandi.dandi.member.MemberTestFixture.NICKNAME;
import static dandi.dandi.member.MemberTestFixture.OAUTH_ID;
import static dandi.dandi.utils.TestImageUtils.IMAGE_ACCESS_URL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;

import dandi.dandi.auth.exception.UnauthorizedException;
import dandi.dandi.member.application.port.in.MemberInfoResponse;
import dandi.dandi.member.application.port.in.NicknameDuplicationCheckResponse;
import dandi.dandi.member.application.port.out.MemberPersistencePort;
import dandi.dandi.member.domain.Member;
import dandi.dandi.post.application.port.out.MemberPostPersistencePort;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class MemberQueryServiceAdapterTest {

    private final MemberPersistencePort memberPersistencePort = Mockito.mock(MemberPersistencePort.class);
    private final MemberPostPersistencePort memberPostPersistencePort = Mockito.mock(MemberPostPersistencePort.class);
    private final MemberQueryServiceAdapter memberQueryServiceAdapter =
            new MemberQueryServiceAdapter(memberPersistencePort, memberPostPersistencePort, IMAGE_ACCESS_URL);

    @DisplayName("기본 프로필 이미지의 회원 정보를 반환할 수 있다.")
    @Test
    void findMemberInfo() {
        Long memberId = 1L;
        when(memberPersistencePort.findById(memberId))
                .thenReturn(Optional.of(Member.initial(OAUTH_ID, NICKNAME, INITIAL_PROFILE_IMAGE_URL)));
        when(memberPostPersistencePort.countPostByMemberId(memberId))
                .thenReturn(5);

        MemberInfoResponse memberInfoResponse = memberQueryServiceAdapter.findMemberInfo(memberId);

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

        assertThatThrownBy(() -> memberQueryServiceAdapter.findMemberInfo(nonExistentMemberId))
                .isInstanceOf(UnauthorizedException.class)
                .hasMessage(UnauthorizedException.notExistentMember().getMessage());
    }

    @DisplayName("중복된 닉네임이 있는지 반환한다.")
    @Test
    void checkDuplication() {
        Long memberId = 1L;
        String nickname = "nickname";
        when(memberPersistencePort.existsMemberByNicknameExceptMine(memberId, nickname))
                .thenReturn(true);

        NicknameDuplicationCheckResponse actual = memberQueryServiceAdapter.checkDuplication(memberId, nickname);

        assertThat(actual.isDuplicated()).isTrue();
    }
}

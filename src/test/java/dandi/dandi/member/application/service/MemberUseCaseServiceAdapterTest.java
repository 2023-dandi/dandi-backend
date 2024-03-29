package dandi.dandi.member.application.service;

import static dandi.dandi.member.MemberTestFixture.DISTRICT;
import static dandi.dandi.member.MemberTestFixture.DISTRICT_VALUE;
import static dandi.dandi.member.MemberTestFixture.MEMBER;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import dandi.dandi.member.application.port.in.LocationUpdateCommand;
import dandi.dandi.member.application.port.in.NicknameUpdateCommand;
import dandi.dandi.member.application.port.out.MemberPersistencePort;
import dandi.dandi.member.domain.DistrictParser;
import dandi.dandi.member.domain.Location;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

@ExtendWith(MockitoExtension.class)
class MemberUseCaseServiceAdapterTest {

    private final MemberPersistencePort memberPersistencePort = Mockito.mock(MemberPersistencePort.class);
    private final DistrictParser districtParser = new DistrictParser();
    private final MemberUseCaseServiceAdapter memberService =
            new MemberUseCaseServiceAdapter(memberPersistencePort, districtParser);

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

        verify(memberPersistencePort).updateLocation(MEMBER.getId(), Location.of(latitude, longitude, DISTRICT));
    }
}

package dandi.dandi.member.application.service;

import dandi.dandi.auth.exception.UnauthorizedException;
import dandi.dandi.member.application.port.in.LocationUpdateCommand;
import dandi.dandi.member.application.port.in.MemberUseCaseServicePort;
import dandi.dandi.member.application.port.in.NicknameUpdateCommand;
import dandi.dandi.member.application.port.out.MemberPersistencePort;
import dandi.dandi.member.domain.DistrictParser;
import dandi.dandi.member.domain.Location;
import dandi.dandi.member.domain.Member;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MemberUseCaseServiceAdapter implements MemberUseCaseServicePort {

    private final MemberPersistencePort memberPersistencePort;
    private final DistrictParser districtParser;

    public MemberUseCaseServiceAdapter(MemberPersistencePort memberPersistencePort,
                                       DistrictParser districtParser) {
        this.memberPersistencePort = memberPersistencePort;
        this.districtParser = districtParser;
    }

    @Override
    public void updateNickname(Long memberId, NicknameUpdateCommand nicknameUpdateCommand) {
        Member member = findMember(memberId);
        try {
            memberPersistencePort.updateNickname(member.getId(), nicknameUpdateCommand.getNickname());
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("이미 존재하는 닉네임입니다.");
        }
    }

    @Override
    public void updateLocation(Long memberId, LocationUpdateCommand locationUpdateCommand) {
        Member member = findMember(memberId);
        Location location = new Location(locationUpdateCommand.getLatitude(), locationUpdateCommand.getLongitude(),
                districtParser.parse(locationUpdateCommand.getDistrict()));
        memberPersistencePort.updateLocation(member.getId(), location);
    }

    private Member findMember(Long memberId) {
        return memberPersistencePort.findById(memberId)
                .orElseThrow(UnauthorizedException::notExistentMember);
    }
}

package dandi.dandi.member.application.port.out.dto;

import dandi.dandi.member.domain.Member;

public interface MemberPersistenceDto {

    Member toMember();
}

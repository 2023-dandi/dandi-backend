package dandi.dandi.member;

import dandi.dandi.member.adapter.out.persistence.MemberJpaEntity;
import dandi.dandi.member.domain.Location;
import dandi.dandi.member.domain.Member;

public class MemberTestFixture {

    public static final Long MEMBER_ID = 1L;
    public static final String OAUTH_ID = "oAuthId";
    public static final String NICKNAME = "memberNickname";
    public static final String INITIAL_PROFILE_IMAGE_URL = "profile/default.jpg";

    public static final Member MEMBER = new Member(
            MEMBER_ID, OAUTH_ID, NICKNAME, new Location(0.0, 0.0), INITIAL_PROFILE_IMAGE_URL);
    public static final Member MEMBER2 = new Member(
            2L, "oAuthId2", "nickname2", new Location(0.0, 0.0), INITIAL_PROFILE_IMAGE_URL);
    public static final MemberJpaEntity MEMBER_JPA_ENTITY = MemberJpaEntity.fromMember(new Member(
            MEMBER_ID, OAUTH_ID, NICKNAME, new Location(0.0, 0.0), INITIAL_PROFILE_IMAGE_URL));

    public static final String DISTRICT = "서울특별시 동작구";
}

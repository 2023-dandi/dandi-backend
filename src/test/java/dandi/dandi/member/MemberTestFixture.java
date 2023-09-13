package dandi.dandi.member;

import dandi.dandi.member.adapter.out.persistence.jpa.MemberEntity;
import dandi.dandi.member.domain.District;
import dandi.dandi.member.domain.Location;
import dandi.dandi.member.domain.Member;

public class MemberTestFixture {

    public static final Long MEMBER_ID = 1L;
    public static final String OAUTH_ID = "oAuthId";
    public static final String NICKNAME = "memberNickname";
    public static final String INITIAL_PROFILE_IMAGE_URL = "profile/default.jpg";
    public static final String DISTRICT_VALUE = "서울특별시 동작구";

    public static final District DISTRICT = new District("서울특별시", "동작구");

    public static final Member MEMBER = new Member(
            MEMBER_ID, OAUTH_ID, NICKNAME, Location.of(0.0, 0.0, DISTRICT), INITIAL_PROFILE_IMAGE_URL);
    public static final Member MEMBER2 = new Member(
            2L, "oAuthId2", "nickname2", Location.of(0.0, 0.0, DISTRICT), INITIAL_PROFILE_IMAGE_URL);
    public static final MemberEntity MEMBER_JPA_ENTITY = MemberEntity.fromMember(new Member(
            MEMBER_ID, OAUTH_ID, NICKNAME, Location.of(0.0, 0.0, DISTRICT), INITIAL_PROFILE_IMAGE_URL));

}

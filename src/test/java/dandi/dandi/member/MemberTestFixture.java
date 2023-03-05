package dandi.dandi.member;

import dandi.dandi.member.adapter.out.persistence.MemberJpaEntity;
import dandi.dandi.member.domain.Location;
import dandi.dandi.member.domain.Member;
import dandi.dandi.member.domain.Nickname;

public class MemberTestFixture {

    public static final String OAUTH_ID = "oAuthId";
    public static final String NICKNAME = "memberNickname";
    public static final String INITIAL_PROFILE_IMAGE_URL = "profile/profile.jpg";

    public static final Member TEST_MEMBER = new Member(
            1L, OAUTH_ID, Nickname.from(NICKNAME), new Location(0.0, 0.0), INITIAL_PROFILE_IMAGE_URL);
    public static final MemberJpaEntity TEST_MEMBER_JPA_ENTITY = MemberJpaEntity.fromMember(new Member(
            1L, OAUTH_ID, Nickname.from(NICKNAME), new Location(0.0, 0.0), INITIAL_PROFILE_IMAGE_URL));
}
